package com.div.codeforcesexplorer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.model.GradientColor;
import com.github.mikephil.charting.utils.Utils;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    LineChart lineChart;
    ImageView profileImg;
    TextView rankView, profileView, contestRatingView, contributionView, friendsView, lastOnlineView, registrationView, failureMessageView, organisationView;
    LinearLayout infoll;
    ProgressBar avatarProgressView;
    ListView submissionListView;
    ArrayList<Entry> dataValue = new ArrayList<>();
    private final String apiUrlInformation = "https://codeforces.com/api/user.info?";
    private final String apiKey = "18849620bd25c43c6a542b0a328715a2c64534d1";
    private final String secret = "40fa813568a1071aec7616c87340eb0515c3c904";
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Bundle bundle = getIntent().getExtras();
        String profileName = bundle.getString("profileName", "");
        Log.d("debugging", profileName);
        String urlInformation = apiUrlInformation+"handles="+profileName;
        String ratingInfoUrl = "https://codeforces.com/api/user.rating?handle="+profileName;
        String submissionsUrl = "https://codeforces.com/api/user.status?handle="+profileName+"&from=1&count=10";
        lineChart = findViewById(R.id.ratingChart);


        drawerLayout = findViewById(R.id.my_drawer_layout_profile);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_menu_profile);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
        actionBarDrawerToggle.syncState();


        profileImg = findViewById(R.id.avatarField);
        rankView = findViewById(R.id.rankField);
        profileView = findViewById(R.id.profileField);
        contestRatingView = findViewById(R.id.ratingField);
        contributionView = findViewById(R.id.contributionField);
        friendsView = findViewById(R.id.friendsField);
        lastOnlineView = findViewById(R.id.lastOnlineField);
        registrationView = findViewById(R.id.registeredField);
        failureMessageView = findViewById(R.id.failureMessage);
        avatarProgressView = findViewById(R.id.avatarProgress);
        organisationView = findViewById(R.id.orgField);
        submissionListView = findViewById(R.id.submissionList);
        avatarProgressView.setVisibility(View.VISIBLE);
        infoll = findViewById(R.id.infoSection);
        getProfileInfo(urlInformation);
        dataValues(ratingInfoUrl);
        getSubmissions(submissionsUrl);



    }


    private void dataValues(String ratingInfoUrl) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, ratingInfoUrl, new Response.Listener<String>() {
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
                        dataValue.add(new Entry(0,0));
                        for(int i = 0;i<jsonArray.length();i++){
                            JSONObject ratingChange = (JSONObject) jsonArray.get(i);
                            dataValue.add(new Entry(i+1,ratingChange.getInt("newRating")));
                        }
                        LineDataSet lineDataSet = new LineDataSet(dataValue,"Data set" );
                        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                        dataSets.add(lineDataSet);
                        LineData data = new LineData(dataSets);
                        lineChart.setData(data);
                        lineChart.invalidate();
                    }else{
                        infoll.setVisibility(View.GONE);
                        failureMessageView.setText(comment);
                    }
                }
                catch (JSONException e) {

                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                infoll.setVisibility(View.GONE);
                failureMessageView.setText("Something wrong occured");
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }
    private void getProfileInfo(String url){

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
                        String image = (String)jsonProfileObj.get("avatar");
                        String rank = "";
                        if(jsonProfileObj.has("rank")){
                             rank = "("+(String) jsonProfileObj.get("rank")+")";
                        }

                        String name = (String) jsonProfileObj.get("handle");
                        String org = "";
                        if(jsonProfileObj.has("organization")){
                            org = (String) jsonProfileObj.get("organization");
                        }
                        String currentRating = "";
                        if(jsonProfileObj.has("rating") && jsonProfileObj.has("maxRank") && jsonProfileObj.has("maxRating")){
                            currentRating = (String.valueOf(jsonProfileObj.getInt("rating"))+" (max. "+(String) jsonProfileObj.get("maxRank")+", "+String.valueOf( jsonProfileObj.getInt("maxRating"))+")");
                        }
                        String contribution = String.valueOf(jsonProfileObj.getInt("contribution")) + " contributions";
                        String friends = String.valueOf(jsonProfileObj.get("friendOfCount"))+" users";
                        Integer lastOnline = (Integer) jsonProfileObj.get("lastOnlineTimeSeconds");
                        Integer registered = (Integer) jsonProfileObj.get("registrationTimeSeconds");
                        Picasso.get().load(image).into(profileImg, new Callback() {
                            @Override
                            public void onSuccess() {
                                avatarProgressView.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });
                        java.util.Date timeLast=new java.util.Date((long)lastOnline*1000);
                        java.util.Date timeRegistered=new java.util.Date((long)registered*1000);
                        rankView.setText(rank);
                        profileView.setText(name);
                        organisationView.setText(org);
                        contestRatingView.setText(currentRating);
                        contributionView.setText(contribution);
                        friendsView.setText(friends);
                        lastOnlineView.setText(timeLast.toString());
                        registrationView.setText(timeRegistered.toString());


                    }else{
                        infoll.setVisibility(View.GONE);
                        failureMessageView.setText(comment);
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
    private void getSubmissions(String url){
        ArrayList<Submission> submissionArrayList = new ArrayList<Submission>();
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

                        for(int i = 0;i<jsonArray.length();i++){
                            JSONObject ratingChange = (JSONObject) jsonArray.get(i);
                            JSONObject problemObj = ratingChange.getJSONObject("problem");
                            submissionArrayList.add(new Submission((String)problemObj.get("name")
                            ,(String)ratingChange.get("programmingLanguage")
                            ,(String)ratingChange.get("verdict")
                            ,(Integer)ratingChange.get("creationTimeSeconds")));
                        }
                    }else{
                        infoll.setVisibility(View.GONE);
                        failureMessageView.setText(comment);
                    }
                }
                catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                SubmissionAdapter adapter = new SubmissionAdapter(ProfileActivity.this, submissionArrayList);
                // Attach the adapter to a ListView
                ListView listView = (ListView) findViewById(R.id.submissionList);
                listView.setVisibility(View.VISIBLE);
                listView.setAdapter(adapter);
                getListViewSize(listView);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    private void getListViewSize(ListView myListView) {
        ListAdapter myListAdapter = myListView.getAdapter();
        if (myListAdapter == null) {
            //do nothing return null
            return;
        }
        //set listAdapter in loop for getting final size
        int totalHeight = 0;
        for (int size = 0; size < myListAdapter.getCount(); size++) {
            View listItem = myListAdapter.getView(size, null, myListView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        //setting listview item in adapter
        ViewGroup.LayoutParams params = myListView.getLayoutParams();
        params.height = totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
        myListView.setLayoutParams(params);
        // print height of adapter on log
        Log.i("height of listItem:", String.valueOf(totalHeight));

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
            Intent intent = new Intent(ProfileActivity.this, ContestListActivty.class);
            Bundle bundle = new Bundle();
            bundle.putString("currentActivity", item.toString());
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (item.getTitle().toString().equals("Search profile")) {
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(ProfileActivity.this, CompareEntryActivity.class);
            startActivity(intent);
        }

        return true;
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.my_drawer_layout_profile);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}

