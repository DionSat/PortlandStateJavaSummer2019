package edu.pdx.cs410j.dion.firstapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private List<String> FILE_NAME = new ArrayList<>();
    AppointmentBook appBk;
    EditText mOwnerText;
    EditText mOwnerButtonText;
    EditText mDescriptionText;
    EditText mStartDateText;
    EditText mEndDateText;
    Button getButton;
    Button getOwnerButton;
    Button createAppButton;
    String st = "";
    String filenames = "filenames.txt";
    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
    Date beginDate = null;
    Date endDate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);

        getButton = findViewById(R.id.getAllButton);
        getOwnerButton = findViewById(R.id.getAllByOwnerButton);
        createAppButton = findViewById(R.id.createButton);
        mOwnerText = findViewById(R.id.ownerText);
        mDescriptionText = findViewById(R.id.descriptionText);
        mStartDateText = findViewById(R.id.startDateText);
        mEndDateText = findViewById(R.id.endDateText);
        mOwnerButtonText = findViewById(R.id.getByOwnerEntered);

        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFileNames(filenames);
                Intent intent = new Intent(MainActivity.this, Activity2.class);
                for(String file : FILE_NAME) {
                    appBk = load(file);
                    StringBuilder build = new StringBuilder(appBk.toString());
                    ArrayList<Appointment> appointments = appBk.getAppointments();
                    build.append("\n====== Appointment Book ======\n\nOwner Name: ");
                    build.append(appBk.getOwnerName());
                    build.append("\n\n------------------------------\nAppointments:\n");

                    for (AbstractAppointment app : appointments) {
                        try {
                            beginDate = format.parse(app.getBeginTimeString());
                        } catch (ParseException e) {
                            System.out.println("Date format incorrect");
                        }
                        try {
                            endDate = format.parse(app.getEndTimeString());
                        } catch (ParseException e) {
                            System.out.println("Date format incorrect");
                        }
                        int duration = (int) ((endDate.getTime() - beginDate.getTime()) / (1000 * 60));
                        build.append("\n Appointment: ");
                        build.append(app + "\nDuration: " + duration + " minutes.\n\n\n");
                    }
                    st += build.toString();
                }
                intent.putExtra("Value", st);
                startActivity(intent);
            }
        });

        getOwnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appBk = load(mOwnerButtonText.getText().toString().trim() + ".txt");
                Intent i = new Intent(MainActivity.this, Activity2.class);
                StringBuilder build = new StringBuilder(appBk.toString());
                ArrayList<Appointment> appointments = appBk.getAppointments();
                build.append("\n====== Appointment Book ======\n\nOwner Name: ");
                build.append(appBk.getOwnerName());
                build.append("\n\n------------------------------\nAppointments:\n");

                for(AbstractAppointment app : appointments) {
                    try {
                        beginDate = format.parse(app.getBeginTimeString());
                    } catch (ParseException e) {
                        System.out.println("Date format incorrect");
                    }
                    try {
                        endDate = format.parse(app.getEndTimeString());
                    } catch (ParseException e) {
                        System.out.println("Date format incorrect");
                    }
                    int duration = (int) ((endDate.getTime() - beginDate.getTime()) / (1000*60));
                    build.append("\n Appointment: ");
                    build.append(app +"\nDuration: " + duration + " minutes.");
                }
                st = build.toString();
                i.putExtra("Value", st);
                startActivity(i);
                //finish();
                mOwnerButtonText.getText().clear();
            }
        });

        createAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkFile(filenames, mOwnerText.getText().toString() + ".txt")) {
                    save(mOwnerText.getText().toString() + ".txt");
                }
                saveFileNames(mOwnerText.getText().toString().trim() + ".txt");
                mOwnerText.getText().clear();
                mDescriptionText.getText().clear();
                mStartDateText.getText().clear();
                mEndDateText.getText().clear();
            }
        });
    }

    public boolean checkFile(String filename, String compare) {
        for(String file : FILE_NAME) {
            if(file.equals(compare)) {
                return true;
            }
        }
        return false;
    }

    public AppointmentBook load(String filename) {
        filename = filename;
        FileInputStream fis = null;
        AppointmentBook appbk = null;

        try {
            fis = openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;

            if(fileExists(this, filename)) {
                appbk = new AppointmentBook("no owner");
            }
            while ((line = br.readLine()) != null) {
                String[] app = line.split(";");
                if (!line.contains(";")) {
                    System.err.println("Malformed text file!");
                }
                if (line == null) {
                    System.err.println("Empty file!");
                    System.exit(1);
                }
                if (app.length < 4) {
                    System.err.println("Too few arguments!");
                    System.exit(1);
                }
                if (app[1] == null) {
                    System.err.println("Description is empty!");
                    System.exit(1);
                }
                if (app[2] == null) {
                    System.err.println("Start date/time in text empty!");
                    System.exit(1);
                }
                if (app[3] == null) {
                    System.err.println("End date/time in text empty!");
                    System.exit(1);
                }
                if (app.length > 4) {
                    System.err.println("Too many arguments. Malformed!");
                    System.exit(1);
                }
                appbk.setOwnerName(app[0]);
                if (checkFormat(app[2]) && checkFormat(app[3])) {
                    Appointment appointment = new Appointment(app[1], app[2], app[3]);
                    appbk.addAppointment(appointment);
                } else {
                    System.err.println("Date/Time is not in right format in text file! Ex: MM/DD/YYYY hh:mm am/pm");
                    break;
                }
                sb.append(line).append("\n");
            }

        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return appbk;
    }

    public void save(String filename) {
        filename = filename;
        String owner = mOwnerText.getText().toString();
        String description = mDescriptionText.getText().toString();
        String startDate = mStartDateText.getText().toString();
        String endDate = mEndDateText.getText().toString();

        if(description.equals("") || owner.equals("") || startDate.equals("") || endDate.equals("")) {
            Toast.makeText(this, "Error text field emtpy!", Toast.LENGTH_LONG).show();
            return;
        }
        String text = owner + ";" + description + ";" + startDate + ";" + endDate;
        FileOutputStream fos = null;

        try {
            fos = openFileOutput(filename, MODE_APPEND);
            fos.write(text.getBytes());
            fos.write(System.getProperty("line.separator").getBytes());
            Toast.makeText(this, "Saved to " + getFilesDir() + "/" + filename, Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void saveFileNames(String filename) {
        String text = filename;
        FileOutputStream fos = null;

        try {
            fos = openFileOutput("filenames.txt", MODE_APPEND);
            fos.write(text.getBytes());
            fos.write(System.getProperty("line.separator").getBytes());
            Toast.makeText(this, "Saved to " + getFilesDir() + "/" + filename, Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void loadFileNames(String filename) {
        FileInputStream fis = null;

        try {
            fis = openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while((text = br.readLine()) != null) {
                FILE_NAME.add(text);
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateAppointmentHomeScreen(String toThis) {
        TextView textView = (TextView) findViewById(R.id.AppoinmentBookHomescreen);
        textView.setText(toThis);
    }

    /**
     * This function is the same function used in the project 2 class except it is used to check whether the text files
     * data/time is in the right format.
     * @param dateTime
     * @return  returns isValid as true is its in the correct format or false if not.
     */
    private static boolean checkFormat(String dateTime) {
        String regEx = "^(([0]?[1-9]|1[0-2])/([0-2]?[0-9]|3[0-1])/[1-2]\\d{3})? ?((([0-1]?\\d)|(2[0-3])):[0-5]\\d)?(:[0-5]\\d)? ?(AM|am|PM|pm)?$";
        boolean isValid = false;
        Pattern p = Pattern.compile(regEx);
        if(p.matcher(dateTime).find()) {
            isValid = true;
        }
        return isValid;
    }

    public boolean fileExists(Context context, String filename) {
        File file = context.getFileStreamPath(filename);
        if(file == null || !file.exists()) {
            return false;
        }
        return true;
    }
}
