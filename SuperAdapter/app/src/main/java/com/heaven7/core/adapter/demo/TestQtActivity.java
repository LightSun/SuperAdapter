package com.heaven7.core.adapter.demo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.vida.android.util.Reflection;

public class TestQtActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println(new Reflection().hello());
    }
}
