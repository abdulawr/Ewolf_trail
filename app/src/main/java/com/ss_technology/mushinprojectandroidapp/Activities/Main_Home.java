package com.ss_technology.mushinprojectandroidapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.ss_technology.mushinprojectandroidapp.R;

public class Main_Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return true;
    }

    public void Amination(View view) {
        startActivity(new Intent(getApplicationContext(),Home.class));
    }
    public void Add_YOur_Pets(View view) {
        startActivity(new Intent(getApplicationContext(),Add_Your_Pet.class));
    }
    public void YOur_Pets_List(View view) {
        startActivity(new Intent(getApplicationContext(),Your_Pets_List.class));
    }

    public void Buy_Pets(View view) {
        startActivity(new Intent(getApplicationContext(),Buy_Pets.class));
    }

    public void Sell_Pets(View view) {
        startActivity(new Intent(getApplicationContext(),Sell_Pets.class));
    }

    public void Sell_Pets_List(View view) {
        startActivity(new Intent(getApplicationContext(),Your_Sell_Pets_Post.class));
    }

    public void Chat_Now(View view) {
        startActivity(new Intent(getApplicationContext(),Chats.class));
    }

    public void Diet_Plan(View view) {
        startActivity(new Intent(getApplicationContext(),Diet_chart.class));
    }

    public void Profile(View view) {
        startActivity(new Intent(getApplicationContext(),Profile.class));
    }

    public void Contact_Us(View view) {
        startActivity(new Intent(getApplicationContext(),Contact_Us.class));
    }

    public void Question(View view) {
        startActivity(new Intent(getApplicationContext(),Website.class));
    }

    public void Vaccination(View view) {
        startActivity(new Intent(getApplicationContext(),Vaccination.class));
    }
}