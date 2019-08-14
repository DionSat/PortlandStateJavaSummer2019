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
    EditText mEndDate;
    EditText mStartDate;
    TextView mAppBkList;
    Button getButton;
    Button getOwnerButton;
    Button createAppButton;
    Button searchButton;
    Button helpButton;
    String st = "";
    String filenames = "filenames.txt";
    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
    Date beginDate = null;
    Date endDate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);

        FILE_NAME.clear();
        loadFileNames(filenames);

        getButton = findViewById(R.id.getAllButton);
        getOwnerButton = findViewById(R.id.getAllByOwnerButton);
        createAppButton = findViewById(R.id.createButton);
        mOwnerText = findViewById(R.id.ownerText);
        mDescriptionText = findViewById(R.id.descriptionText);
        mStartDateText = findViewById(R.id.startDateText);
        mEndDateText = findViewById(R.id.endDateText);
        mOwnerButtonText = findViewById(R.id.getByOwnerEntered);
        mEndDate = findViewById(R.id.endDateRangeText);
        mStartDate = findViewById(R.id.beginDateRangeText);
        searchButton = findViewById(R.id.search_button);
        mAppBkList = findViewById(R.id.AppoinmentBookHomescreen);
        helpButton = findViewById(R.id.help_button);

        String homescreen = "There are " + FILE_NAME.size() + " AppointmentBook owners in this application currently";
        mAppBkList.setText(homescreen);

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printReadme();
            }
        });

        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FILE_NAME.clear();
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
                            return;
                        }
                        try {
                            endDate = format.parse(app.getEndTimeString());
                        } catch (ParseException e) {
                            System.out.println("Date format incorrect");
                            return;
                        }
                        int duration = (int) ((endDate.getTime() - beginDate.getTime()) / (1000 * 60));
                        build.append("\n Appointment: ");
                        build.append(app + "\nDuration: " + duration + " minutes.\n\n\n");
                    }
                    st += build.toString();
                }
                intent.putExtra("Value", st);
                startActivity(intent);
                st = "";
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FILE_NAME.clear();
                loadFileNames(filenames);
                Intent intent = new Intent(MainActivity.this, Activity2.class);

                if(mStartDate.getText().toString().isEmpty() || mEndDate.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Field Empty!", Toast.LENGTH_LONG).show();
                    return;
                }

                for (String file : FILE_NAME) {
                    appBk = load(file);
                    StringBuilder build = new StringBuilder(appBk.toString());
                    try {
                        beginDate = format.parse(mStartDate.getText().toString());
                    } catch (ParseException e) {
                        Toast.makeText(MainActivity.this, "Begin Date format incorrect", Toast.LENGTH_LONG).show();
                        return;
                    }
                    try {
                        endDate = format.parse(mEndDate.getText().toString());
                    } catch (ParseException e) {
                        Toast.makeText(MainActivity.this, "End Date format incorrect", Toast.LENGTH_LONG).show();
                        return;
                    }
                    rangePrint(appBk, beginDate, endDate, build);
                }
                intent.putExtra("Value", st);
                startActivity(intent);
                st = "";
                mStartDate.getText().clear();
                mEndDate.getText().clear();
            }
        });

        getOwnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOwnerButtonText.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Field Empty!", Toast.LENGTH_LONG).show();
                    return;
                }
                appBk = load(mOwnerButtonText.getText().toString().trim() + ".txt");
                Intent i = new Intent(MainActivity.this, Activity2.class);
                StringBuilder build = new StringBuilder(appBk.toString());
                ArrayList<Appointment> appointments = appBk.getAppointments();
                build.append("\n====== Appointment Book ======\n\nOwner Name: ");
                build.append(appBk.getOwnerName());
                build.append("\n\n------------------------------\nAppointments:\n");

                for (AbstractAppointment app : appointments) {
                    try {
                        beginDate = format.parse(app.getBeginTimeString());
                    } catch (ParseException e) {
                        Toast.makeText(MainActivity.this, "Begin Date format incorrect", Toast.LENGTH_LONG).show();
                        return;
                    }
                    try {
                        endDate = format.parse(app.getEndTimeString());
                    } catch (ParseException e) {
                        Toast.makeText(MainActivity.this, "End Date format incorrect", Toast.LENGTH_LONG).show();
                        return;
                    }
                    int duration = (int) ((endDate.getTime() - beginDate.getTime()) / (1000 * 60));
                    build.append("\n Appointment: ");
                    build.append(app + "\nDuration: " + duration + " minutes.");
                }
                st = build.toString();
                i.putExtra("Value", st);
                startActivity(i);
                st = "";
                mOwnerButtonText.getText().clear();
            }
        });

        createAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FILE_NAME.clear();
                loadFileNames(filenames);

                if(mOwnerText.getText().toString().isEmpty()|| mDescriptionText.getText().toString().isEmpty() || mStartDateText.getText().toString().isEmpty() || mEndDateText.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Field is empty!", Toast.LENGTH_LONG).show();
                    return;
                }

                if(!checkFormat(mEndDateText.getText().toString()) || !checkFormat(mStartDateText.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Incorrect Date/Time format!", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!checkFile(mOwnerText.getText().toString() + ".txt")) {
                    saveFileNames(mOwnerText.getText().toString().trim() + ".txt");
                }
                save(mOwnerText.getText().toString() + ".txt");
                mOwnerText.getText().clear();
                mDescriptionText.getText().clear();
                mStartDateText.getText().clear();
                mEndDateText.getText().clear();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        FILE_NAME.clear();
        loadFileNames(filenames);
        String homescreen = "There are " + FILE_NAME.size() + " AppointmentBook owners in this application currently";
        mAppBkList.setText(homescreen);
    }

    /**
     * A void function that prints the read me when called
     * @return there is no return type
     */
    void printReadme() {
        String temp = ("\n\nProject 5 README Dion Satcher, CS410J " +
                "\nThis program will allow user to" +
                "\nenter appointment information" +
                "\non the command line, then saves" +
                "\nthe information. This program" +
                "\n will allow you to write that" +
                "\ninformation to a text file." +
                "\n---------------" +
                "\nusage press a button." +
                "\nargs are (in this order):\n" +
                "\nowner - The person whose owns" +
                "\nthe appt book\n\n" +
                "description - A non-blank description" +
                "\nof the appointment\n\n" +
                "beginTime - (date AND time) When" +
                "\nthe appt begins (12-hour time)\n\n" +
                "endTime - (date AND time) When" +
                "\nthe appt ends (12-hour time)\n\n" +
                "  buttons are (options may appear" +
                "\nin any order):\n\n" +
                "       -GET ALL :: get all" +
                "\nappointment books and print all appointments\n\n" +
                "       -SEARCH :: search for" +
                "\nappointments within date range\n\n" +
                "       -GET BY OWNER :: get a" +
                "\nspecific owners appointment book\n\n" +
                "       -CREATE AN APPOINTMENT ::" +
                "\ncreate a new appointment\n\n" +
                "  Date and time should be in the" +
                "\nformat: mm/dd/yyyy hh:mm am/pm" +
                "\n(or mm/dd/yy hh:mm am/pm)" +
                "\n***END OF README***");

        Intent intent = new Intent(this, Activity2.class);
        intent.putExtra("Value", temp);
        startActivity(intent);
    }

    public boolean checkFile(String compare) {
        for (String file : FILE_NAME) {
            if (file.equals(compare)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Load appointmentBook owner appointments from textfile
     * @param filename
     *        the filename of the textfile(which would be the owners name)
     * @return
     */
    public AppointmentBook load(String filename) {
        FileInputStream fis = null;
        AppointmentBook appbk = null;

        try {
            fis = openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;

            if (fileExists(this, filename)) {
                appbk = new AppointmentBook("no owner");
            }
            while ((line = br.readLine()) != null) {
                String[] app = line.split(";");
                if (!line.contains(";")) {
                    Toast.makeText(this, "Malformed text file!", Toast.LENGTH_LONG).show();
                }
                if (line == null) {
                    Toast.makeText(this, "Empty file!", Toast.LENGTH_LONG).show();
                    break;
                }
                if (app.length < 4) {
                    Toast.makeText(this, "Too few arguments!", Toast.LENGTH_LONG).show();
                    break;
                }
                if (app[1] == null) {
                    Toast.makeText(this, "Description is empty!", Toast.LENGTH_LONG).show();
                    break;
                }
                if (app[2] == null) {
                    Toast.makeText(this, "Start date/time in text empty!", Toast.LENGTH_LONG).show();
                    break;
                }
                if (app[3] == null) {
                    Toast.makeText(this, "End date/time in text empty!", Toast.LENGTH_LONG).show();
                    break;
                }
                if (app.length > 4) {
                    Toast.makeText(this, "Too many arguments. Malformed!", Toast.LENGTH_LONG).show();
                    break;
                }
                appbk.setOwnerName(app[0]);
                if (checkFormat(app[2]) && checkFormat(app[3])) {
                    Appointment appointment = new Appointment(app[1], app[2], app[3]);
                    appbk.addAppointment(appointment);
                } else {
                    Toast.makeText(this, "Date/Time is not in right format in text file! Ex: MM/DD/YYYY hh:mm am/pm", Toast.LENGTH_LONG).show();
                    break;
                }
                sb.append(line).append("\n");
            }

        } catch (FileNotFoundException e) {
            Toast.makeText(this, "File not found.", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return appbk;
    }

    /**
     * Save appointmentBook owner appointments to textfile
     * @param filename
     *        the filename of the textfile(which would be the owners name)
     */
    public void save(String filename) {
        String owner = mOwnerText.getText().toString();
        String description = mDescriptionText.getText().toString();
        String startDate = mStartDateText.getText().toString();
        String endDate = mEndDateText.getText().toString();

        if (description.equals("") || owner.equals("") || startDate.equals("") || endDate.equals("")) {
            Toast.makeText(this, "Error field emtpy!", Toast.LENGTH_LONG).show();
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
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * save appointment book owners file names
     * @param filename
     *        The file holding all the owners textfile names
     */
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
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * load appointment book owners file names
     * @param filename
     *        The file holding all the owners textfile names
     */
    public void loadFileNames(String filename) {
        FileInputStream fis;

        try {
            fis = openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {
                FILE_NAME.add(text);
            }


        } catch (FileNotFoundException e) {
            Toast.makeText(this, "List of owner text files not found.", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "IOException when filenames.txt", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * This function is the same function used in the project 2 class except it is used to check whether the text files
     * data/time is in the right format.
     *
     * @param dateTime
     * @return returns isValid as true is its in the correct format or false if not.
     */
    private static boolean checkFormat(String dateTime) {
        String regEx = "^(([0]?[1-9]|1[0-2])/([0-2]?[0-9]|3[0-1])/[1-2]\\d{3})? ?((([0-1]?\\d)|(2[0-3])):[0-5]\\d)?(:[0-5]\\d)? ?(AM|am|PM|pm)?$";
        boolean isValid = false;
        Pattern p = Pattern.compile(regEx);
        if (p.matcher(dateTime).find()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * Check if file already exists
     * @param context
     * @param filename
     * @return
     */
    public boolean fileExists(Context context, String filename) {
        File file = context.getFileStreamPath(filename);
        if (file == null || !file.exists()) {
            return false;
        }
        return true;
    }

    /**
     * Print the appointments within the specified ranges
     * @param aBook, startDate, endDate
     * @throws IOException exception thrown
     */
    public void rangePrint(AbstractAppointmentBook aBook, Date startDate, Date endDate, StringBuilder build) {
        ArrayList<Appointment> apptList;
        ArrayList<Appointment> sortedList;
        AppointmentBook appointmentBook;
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

        if (aBook == null || startDate == null || endDate == null || build == null)
            return;

        sortedList = new ArrayList<>();
        appointmentBook = (AppointmentBook) aBook;
        apptList = appointmentBook.getAppointments();

        for (Appointment appt : apptList) {
            if (appt.getBeginTime().after(startDate) || appt.getBeginTime().compareTo(startDate) == 0) {
                if (appt.getEndTime().before(endDate) || appt.getEndTime().compareTo(endDate) == 0)
                    sortedList.add(appt);
            }
        }

        if (sortedList.size() != 0) {
            build.append("\n====== Appointment Book ======\n\nOwner Name: ");
            build.append(appBk.getOwnerName());
            build.append("\n\n------------------------------\nAppointments:\n");

            for (Appointment app : sortedList) {
                try {
                    beginDate = format.parse(app.getBeginTimeString());
                } catch (ParseException e) {
                    Toast.makeText(this, "Begin Date format incorrect", Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    endDate = format.parse(app.getEndTimeString());
                } catch (ParseException e) {
                    Toast.makeText(this, "End Date format incorrect", Toast.LENGTH_LONG).show();
                    return;
                }
                int duration = (int) ((endDate.getTime() - beginDate.getTime()) / (1000 * 60));
                build.append("\n Appointment: ");
                build.append(app + "\nDuration: " + duration + " minutes.\n\n\n");
            }
            build.append("\n--- End of Appointments ---\n");
            st += build.toString();
        } else
            Toast.makeText(this, "There are NO APPOINTMENTS in the date/time range provided", Toast.LENGTH_LONG).show();
    }
}
