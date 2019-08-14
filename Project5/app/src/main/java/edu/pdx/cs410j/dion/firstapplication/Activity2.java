package edu.pdx.cs410j.dion.firstapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Activity2 extends AppCompatActivity {
    TextView tv;
    String st;
    public static Activity fa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fa = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        tv = findViewById(R.id.result_display);

        st = getIntent().getExtras().getString("Value");
        tv.setText(st);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
