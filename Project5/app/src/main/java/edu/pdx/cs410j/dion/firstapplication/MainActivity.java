package edu.pdx.cs410j.dion.firstapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private static final String FILE_NAME = "appointment.txt";
    Appointment app;
    AppointmentBook appBk;
    ArrayList<AppointmentBook> appBkList;
    EditText mOwnerText;
    EditText mDescriptionText;
    EditText mStartDateText;
    EditText mEndDateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);

        mOwnerText = findViewById(R.id.ownerText);
        mDescriptionText = findViewById(R.id.descriptionText);
        mStartDateText = findViewById(R.id.startDateText);
        mEndDateText = findViewById(R.id.endDateText);

        ArrayList<AppointmentBook> appbkList = load();
        if(appbkList.size() == 0) {
            Toast.makeText(this, "Appointment Book Data Empty", Toast.LENGTH_LONG).show();
        }
    }

    public ArrayList<AppointmentBook> load() {
        FileInputStream fis = null;
        ArrayList<AppointmentBook> appbkList = new ArrayList<>();

        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String owner;
            String description;
            String startDate;
            String endDate;
            String line = null;

            if(fileExists(this, FILE_NAME)) {
                appBk = new AppointmentBook("no owner");
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
                appBk.setOwnerName(app[0]);
                if (checkFormat(app[2]) && checkFormat(app[3])) {
                    Appointment appointment = new Appointment(app[1], app[2], app[3]);
                    appBk.addAppointment(appointment);
                    appbkList.add(appBk);
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
        return appbkList;
    }

    public void save() {
        String owner = mOwnerText.getText().toString();
        String description = mDescriptionText.getText().toString();
        String startDate = mStartDateText.getText().toString();
        String endDate = mEndDateText.getText().toString();
        String text = owner + ";" + description + ";" + startDate + ";" + endDate;
        FileOutputStream fos = null;

        try {
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(text.getBytes());

            mOwnerText.getText().clear();
            mDescriptionText.getText().clear();
            mStartDateText.getText().clear();
            mEndDateText.getText().clear();
            Toast.makeText(this, "Saved to " + getFilesDir() + "/" + FILE_NAME, Toast.LENGTH_LONG).show();
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
        String regEx = "^(((0[13578]|1[02])[\\/\\.-](0[1-9]|[12]\\d|3[01])[\\/\\.-]((19|[2-9]\\d)\\d{2})\\s(0[0-9]|1[0-2]):(0[0-9]|[1-59]\\d)\\s(AM|am|PM|pm))|((0[13456789]|1[012])[\\/\\.-](0[1-9]|[12]\\d|30)[\\/\\.-]((19|[2-9]\\d)\\d{2})\\s(0[0-9]|1[0-2]):(0[0-9]|[1-59]\\d)\\s(AM|am|PM|pm))|((02)[\\/\\.-](0[1-9]|1\\d|2[0-8])[\\/\\.-]((19|[2-9]\\d)\\d{2})\\s(0[0-9]|1[0-2]):(0[0-9]|[1-59]\\d)\\s(AM|am|PM|pm))|((02)[\\/\\.-](29)[\\/\\.-]((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))\\s(0[0-9]|1[0-2]):(0[0-9]|[1-59]\\d))\\s(AM|am|PM|pm))$";
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
