package com.div.codeforcesexplorer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class CompareActivity extends AppCompatActivity {
    BarChart barChart, barChartProblem;


    BarData barData, barDataProblem;


    BarDataSet barDataSet, barDataSetProblem;
    LineChart lineChart;
    ImageView firstView, secondView;
    TextView fProfile, sProfile, fRating, sRating, fRank, sRank, fMaxRating, sMaxRating, fMaxRank, sMaxRank;
    String firstProfileName, secondProfileName;
    Set<String> setFirst = new HashSet<String>();
    Set<String> setSecond = new HashSet<String>();
    Integer firstTotalContests = 0;
    Integer secondTotalContests = 0;
    Integer firstTotalProblemSolved = 0;
    Integer secondTotalProblemSolved = 0;
    Boolean flag = false;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    ArrayList<Entry> firstRatingValues = new ArrayList<>();
    ArrayList<Entry> secondRatingValues = new ArrayList<>();



    ArrayList barEntriesArrayList;
    ArrayList barEntriesTotalProblems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);
        Bundle bundle = getIntent().getExtras();
        firstProfileName = bundle.getString("firstProfileName", "");
        secondProfileName = bundle.getString("secondProfileName", "");
        Log.d("debugging", firstProfileName);
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
        sMaxRank = findViewById(R.id.secondMaxRankField);
        lineChart = findViewById(R.id.combinedRatingChart);
        barChart = findViewById(R.id.contestBarChart);
        barChartProblem = findViewById(R.id.problemBarChart);
        String ratingInfoUrlFirst = "https://codeforces.com/api/user.rating?handle="+firstProfileName;
        String ratingInfoUrlSecond = "https://codeforces.com/api/user.rating?handle="+secondProfileName;
        String submissionFirstUrl = "https://codeforces.com/api/user.status?handle="+firstProfileName+"&from=1";
        String submissionSecondUrl = "https://codeforces.com/api/user.status?handle="+secondProfileName+"&from=1";
        drawerLayout = findViewById(R.id.my_drawer_layout_compare);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_menu_compare);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
        actionBarDrawerToggle.syncState();
        getProfileInfoFirst("https://codeforces.com/api/user.info?handles="+firstProfileName);
        getProfileInfoSecond("https://codeforces.com/api/user.info?handles="+secondProfileName);
        dataValues(ratingInfoUrlFirst, ratingInfoUrlSecond);
        getProblemSolved(submissionFirstUrl, submissionSecondUrl);
    }
    private void dataValues(String ratingInfoUrlFirst, String ratingInfoUrlSecond ) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, ratingInfoUrlFirst, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonResponse = new JSONObject(response);
                    String status = jsonResponse.getString("status");
                    String comment = "";
                    if(!status.equals("OK")){
                        comment = jsonResponse.getString("comment");
                    }
                    if(status.equals("OK")){

                        JSONArray jsonArray = jsonResponse.getJSONArray("result");
                        firstRatingValues.add(new Entry(0,0));
                        for(int i = 0;i<jsonArray.length();i++){
                            JSONObject ratingChange = (JSONObject) jsonArray.get(i);
                            firstRatingValues.add(new Entry(i+1,ratingChange.getInt("newRating")));
                        }
                        firstTotalContests = jsonArray.length();
                        LineDataSet lineDataSet1 = new LineDataSet(firstRatingValues,"First profile" );
                        LineDataSet lineDataSet2 = new LineDataSet(secondRatingValues,"Second Profile profile" );
                        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                        dataSets.add(lineDataSet1);
                        dataSets.add(lineDataSet2);
                        LineData data = new LineData(dataSets);
                        lineChart.setData(data);
                        lineChart.invalidate();
                        StringRequest stringRequests = new StringRequest(Request.Method.GET, ratingInfoUrlSecond, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try{
                                    JSONObject jsonResponse = new JSONObject(response);
                                    String status = jsonResponse.getString("status");
                                    String comment = "";
                                    if(!status.equals("OK")){
                                        comment = jsonResponse.getString("comment");
                                    }
                                    if(status.equals("OK")){

                                        JSONArray jsonArray = jsonResponse.getJSONArray("result");
                                        secondRatingValues.add(new Entry(0,0));
                                        for(int i = 0;i<jsonArray.length();i++){
                                            JSONObject ratingChange = (JSONObject) jsonArray.get(i);
                                            secondRatingValues.add(new Entry(i+1,ratingChange.getInt("newRating")));
                                        }
                                        secondTotalContests = jsonArray.length();

                                        barEntriesArrayList = new ArrayList<>();
                                        barEntriesArrayList.add(new BarEntry(0, firstTotalContests));
                                        barEntriesArrayList.add(new BarEntry(1, secondTotalContests));
                                        barDataSet = new BarDataSet(barEntriesArrayList, "Total contests appeared");
                                        barData = new BarData(barDataSet);


                                        barChart.setData(barData);


                                        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);


                                        barDataSet.setValueTextColor(Color.BLACK);


                                        barDataSet.setValueTextSize(16f);
                                        barChart.getDescription().setEnabled(false);
                                        LineDataSet lineDataSet1 = new LineDataSet(firstRatingValues,"First profile" );
                                        LineDataSet lineDataSet2 = new LineDataSet(secondRatingValues,"Second profile" );
                                        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                                        dataSets.add(lineDataSet1);
                                        dataSets.add(lineDataSet2);
                                        LineData data = new LineData(dataSets);
                                        lineChart.setData(data);
                                        lineChart.invalidate();
                                    }else{
//                        infoll.setVisibility(View.GONE);
//                        failureMessageView.setText(comment);
                                    }
                                }
                                catch (JSONException e) {

                                    throw new RuntimeException(e);
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
//                infoll.setVisibility(View.GONE);
//                failureMessageView.setText("Something wrong occured");
                            }
                        });
                        RequestQueue requestQueues = Volley.newRequestQueue(getApplicationContext());
                        requestQueues.add(stringRequests);
                    }else{
//                        infoll.setVisibility(View.GONE);
//                        failureMessageView.setText(comment);
                    }
                }
                catch (JSONException e) {

                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                infoll.setVisibility(View.GONE);
//                failureMessageView.setText("Something wrong occured");
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }
    private void getProblemSolved(String submissionFirstUrl,String submissionSecondUrl){
            StringRequest stringRequest = new StringRequest(Request.Method.GET, submissionFirstUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String status = jsonResponse.getString("status");
                        String comment = "";
                        if (!status.equals("OK")) {
                            comment = jsonResponse.getString("comment");
                        }
                        if (status.equals("OK")) {

                            JSONArray jsonArray = jsonResponse.getJSONArray("result");
                            for(int i = 0;i<jsonArray.length();i++){
                                JSONObject ratingChange = (JSONObject) jsonArray.get(i);
                                JSONObject problemObj = ratingChange.getJSONObject("problem");
                                if(ratingChange.getString("verdict").equals("OK")){
                                    if(!setFirst.contains(problemObj.getString("name"))){
                                        setFirst.add(problemObj.getString("name"));
                                    }
                                }
                            }
                            Log.d("debug", "set size is"+String.valueOf(setFirst.size()));
                            StringRequest stringRequest = new StringRequest(Request.Method.GET, submissionSecondUrl, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonResponse = new JSONObject(response);
                                        String status = jsonResponse.getString("status");
                                        String comment = "";
                                        if (!status.equals("OK")) {
                                            comment = jsonResponse.getString("comment");
                                        }
                                        if (status.equals("OK")) {

                                            JSONArray jsonArray = jsonResponse.getJSONArray("result");
                                            for(int i = 0;i<jsonArray.length();i++){
                                                JSONObject ratingChange = (JSONObject) jsonArray.get(i);
                                                JSONObject problemObj = ratingChange.getJSONObject("problem");
                                                if(ratingChange.getString("verdict").equals("OK")){
                                                    if(!setSecond.contains(problemObj.getString("name"))){
                                                        setSecond.add(problemObj.getString("name"));
                                                    }
                                                }
                                            }
                                            Log.d("debug part two","set two size is"+String.valueOf(setSecond.size()));
                                            barEntriesTotalProblems = new ArrayList<>();
                                            barEntriesTotalProblems.add(new BarEntry(0, setFirst.size()));
                                            barEntriesTotalProblems.add(new BarEntry(1, setSecond.size()));
                                            barDataSetProblem = new BarDataSet(barEntriesTotalProblems, "Total problems");
                                            barDataProblem = new BarData(barDataSetProblem);

                                            // below line is to set data
                                            // to our bar chart.
                                            barChartProblem.setData(barDataProblem);

                                            // adding color to our bar data set.
                                            barDataSetProblem.setColors(ColorTemplate.MATERIAL_COLORS);

                                            // setting text color.
                                            barDataSetProblem.setValueTextColor(Color.BLACK);

                                            // setting text size
                                            barDataSetProblem.setValueTextSize(16f);
                                            barChartProblem.getDescription().setEnabled(false);
                                        }
                                    }catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });
                            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                            requestQueue.add(stringRequest);

                        }
                    }catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void getProfileInfoSecond(String url){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonResponse = new JSONObject(response);
                    String status = jsonResponse.getString("status");
                    String comment = "";
                    if(!status.equals("OK")){
                        comment = jsonResponse.getString("comment");
                    }
                    if(status.equals("OK")){

                        JSONArray jsonArray = jsonResponse.getJSONArray("result");
                        JSONObject jsonProfileObj = (JSONObject) jsonArray.get(0);
                        String image = (String) jsonProfileObj.get("avatar");
                        String rank = "";
                        String currentRating = "";
                        String maxRank = "";
                        Integer maxRating = 0;
                        if(jsonProfileObj.has("rank")) {
                            rank = "(" + (String) jsonProfileObj.get("rank") + ")";
                        }
                        String name = (String) jsonProfileObj.get("handle");
                        if(jsonProfileObj.has("rating")) {
                            currentRating = String.valueOf(jsonProfileObj.getInt("rating"));
                        }
                        if(jsonProfileObj.has("maxRank")) {
                            maxRank = (String) jsonProfileObj.get("maxRank");
                        }
                        if(jsonProfileObj.has("maxRating")){
                            maxRating = (Integer) jsonProfileObj.get("maxRating");
                        }
                        Picasso.get().load(image).into(firstView, new Callback() {
                            @Override
                            public void onSuccess() {
//                                avatarProgressView.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });
                        fRank.setText(rank);
                        fProfile.setText(name);
                        fMaxRank.setText(maxRank);
                        fRating.setText(currentRating);
                        fMaxRating.setText(String.valueOf(maxRating));



                    }else{
//                        infoll.setVisibility(View.GONE);
//                        failureMessageView.setText(comment);
                    }
                }
                catch (JSONException e) {

                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    private void getProfileInfoFirst(String url){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonResponse = new JSONObject(response);
                    String status = jsonResponse.getString("status");
                    String comment = "";
                    if(!status.equals("OK")){
                        comment = jsonResponse.getString("comment");
                    }
                    if(status.equals("OK")){

                        JSONArray jsonArray = jsonResponse.getJSONArray("result");
                        JSONObject jsonProfileObj = (JSONObject) jsonArray.get(0);
                        String image = (String) jsonProfileObj.get("avatar");
                        String rank = "";
                        String currentRating = "";
                        String maxRank = "";
                        Integer maxRating = 0;
                        if(jsonProfileObj.has("rank")) {
                             rank = "(" + (String) jsonProfileObj.get("rank") + ")";
                        }
                        String name = (String) jsonProfileObj.get("handle");
                        if(jsonProfileObj.has("rating")) {
                             currentRating = String.valueOf(jsonProfileObj.getInt("rating"));
                        }
                        if(jsonProfileObj.has("maxRank")) {
                             maxRank = (String) jsonProfileObj.get("maxRank");
                        }
                        if(jsonProfileObj.has("maxRating")){
                            maxRating = (Integer) jsonProfileObj.get("maxRating");
                        }

                            Picasso.get().load(image).into(secondView, new Callback() {
                                @Override
                                public void onSuccess() {
//                                avatarProgressView.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError(Exception e) {

                                }
                            });
                        sRank.setText(rank);
                        sProfile.setText(name);
                        sMaxRank.setText(maxRank);
                        sRating.setText(currentRating);
                        sMaxRating.setText(String.valueOf(maxRating));



                    }else{
//                        infoll.setVisibility(View.GONE);
//                        failureMessageView.setText(comment);
                    }
                }
                catch (JSONException e) {

                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.my_drawer_layout_compare);
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
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.my_drawer_layout_compare);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}