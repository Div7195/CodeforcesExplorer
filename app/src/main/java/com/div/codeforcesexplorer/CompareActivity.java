package com.div.codeforcesexplorer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class CompareActivity extends AppCompatActivity {
    ImageView firstView, secondView;
    TextView fProfile, sProfile, fRating, sRating, fRank, sRank, fMaxRating, sMaxRating, fMaxRank, sMaxRank;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);
        Bundle bundle = getIntent().getExtras();
        String profileName = bundle.getString("profileName", "");
        Log.d("debugging", profileName);
        firstView = findViewById(R.id.firstImage);
        secondView = findViewById(R.id.secondImage);
        fProfile = findViewById(R.id.firstProfileField);
        sProfile = findViewById(R.id.secondProfileField);
        fRating = findViewById(R.id.firstRatingField);
        sRating = findViewById(R.id.secondRatingField);
        fRank = findViewById(R.id.firstRankField);
        sRank = findViewById(R.id.secondRankField);
        fMaxRating = findViewById(R.id.firstMaxRatingField);
        sMaxRating = findViewById(R.id.secondMaxRatingField);
        fMaxRank = findViewById(R.id.firstMaxRankField);
        sMaxRating = findViewById(R.id.secondMaxRankField);

        drawerLayout = findViewById(R.id.my_drawer_layout_compare);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_menu_compare);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
        actionBarDrawerToggle.syncState();



    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.my_drawer_layout_profile);
        drawer.closeDrawer(GravityCompat.START);
        if(!item.getTitle().toString().equals("Search profile") && !item.getTitle().toString().equals("Compare profiles")) {
            Intent intent = new Intent(CompareActivity.this, ContestListActivty.class);
            Bundle bundle = new Bundle();
            bundle.putString("currentActivity", item.toString());
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (item.getTitle().toString().equals("Search profile")) {
            Intent intent = new Intent(CompareActivity.this, MainActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(CompareActivity.this, CompareEntryActivity.class);
            startActivity(intent);
        }

        return true;
    }
}