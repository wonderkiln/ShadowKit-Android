package com.flurgle.shadowkit.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.flurgle.shadowkit.ShadowKit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShadowKit.init(this);
        setContentView(R.layout.activity_main);
    }
}
