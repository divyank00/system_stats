package com.example.system_stats;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class Admin_Lab extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__lab);

        Intent intentThatStartedThisActivity=getIntent();
        String LabNo = intentThatStartedThisActivity.getStringExtra("Labno");
        Toast.makeText(this, LabNo, Toast.LENGTH_SHORT).show();
    }
}
