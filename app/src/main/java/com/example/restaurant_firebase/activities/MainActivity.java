package com.example.restaurant_firebase.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.restaurant_firebase.R;
import com.example.restaurant_firebase.fragment.ConsultaFragment;
import com.example.restaurant_firebase.fragment.LoginFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoginFragment loginFragment = new LoginFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout_login, loginFragment);
        transaction.commit();
    }
    public void setToolbarTitle(String title){
        getSupportActionBar().setTitle(title);
    }
}