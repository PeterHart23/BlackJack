package com.example.blackjack;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.hide();
    }

    public void start(View view){
        EditText name = findViewById(R.id.etName);
        Intent intent = new Intent(this, GamePlay.class);
        intent.putExtra("name", name.getText().toString());
        startActivity(intent);
    }
}