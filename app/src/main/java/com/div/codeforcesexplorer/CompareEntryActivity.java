package com.div.codeforcesexplorer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.navigation.NavigationView;

public class CompareEntryActivity extends AppCompatActivity {
    public DrawerLayout drawerLayout;
    Button compareButtonView;
    EditText firstInputView, secondInputView;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_entry);

        compareButtonView = findViewById(R.id.compareButtonView);
        firstInputView = findViewById(R.id.firstInputField);
        secondInputView = findViewById(R.id.secnondInputField);
        drawerLayout = findViewById(R.id.my_drawer_layout_compare_entry);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_menu_compare_entry);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        compareButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CompareEntryActivity.this, CompareActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("firstProfileName", firstInputView.getText().toString());
                bundle.putString("secondProfileName", secondInputView.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.my_drawer_layout_compare_entry);
        drawer.closeDrawer(GravityCompat.START);
        if(!item.getTitle().toString().equals("Search profile") && !item.getTitle().toString().equals("Compare profiles")) {
            Intent intent = new Intent(CompareEntryActivity.this, ContestListActivty.class);
            Bundle bundle = new Bundle();
            bundle.putString("currentActivity", item.toString());
            intent.putExtras(bundle);
            startActivity(intent);
        }else if (item.getTitle().toString().equals("Search profile")) {
            Intent intent = new Intent(CompareEntryActivity.this, MainActivity.class);
            startActivity(intent);
        }

        return true;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.my_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}