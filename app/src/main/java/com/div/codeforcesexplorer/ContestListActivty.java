package com.div.codeforcesexplorer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ContestListActivty extends AppCompatActivity {
    TextView headingView, statusView;

    ProgressBar progressBar;
    String currentActivity = "";
    String goToActivity = "";
    
    private final String apiUrl = "https://codeforces.com/api/contest.list?gym=false";
    private final String apiKey = "18849620bd25c43c6a542b0a328715a2c64534d1";
    private final String secret = "40fa813568a1071aec7616c87340eb0515c3c904";
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest_list_activty);
        drawerLayout = findViewById(R.id.my_drawer_layout_contest);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        currentActivity = getIntent().getStringExtra("currentActivity");
//        headingView = findViewById(R.id.headingContestList);
        progressBar = findViewById(R.id.progressBarContestList);
        statusView = findViewById(R.id.statusField);
        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_menu_contestlist);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
        updateList(currentActivity);


    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.my_drawer_layout_contest);
        drawer.closeDrawer(GravityCompat.START);
        if(item.getTitle().toString().equals(currentActivity)){
            Toast.makeText(this, "Already here", Toast.LENGTH_SHORT).show();
        }else {
            if (item.getTitle().equals("Search profile")) {
                    Intent intent = new Intent(ContestListActivty.this, MainActivity.class);
                    startActivity(intent);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("entryRole", "user");
//                    intent.putExtras(bundle);
//                    startActivity(intent);
//                    return true;
                Toast.makeText(this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
            } else if (item.getTitle().equals("Compare profiles")) {


//                    Intent intent = new Intent(ContestListActivty.this, MainActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("entryRole", "user");
//                    intent.putExtras(bundle);
//                    startActivity(intent);
                Toast.makeText(this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();


            }else{
                if(!item.getTitle().toString().equals(currentActivity)){
                    updateList((String) item.getTitle());
//                    headingView.setText("Fetching..");
                }
            }
        }

        return true;
    }



    private void updateList(String s){
        ListView listView = (ListView) findViewById(R.id.contestList);
        listView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        statusView.setVisibility(View.GONE);

        ArrayList<Contest> contestArrayList = new ArrayList<Contest>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String status = jsonResponse.getString("status");
                    String comment = "";
                    if(!status.equals("OK")){
                        comment = jsonResponse.getString("comment");
                    }
        
                    if(status.equals("OK")){
                        currentActivity = s;
//                        headingView.setText(currentActivity);
                        JSONArray jsonArray = jsonResponse.getJSONArray("result");
                        if(s.equals("Upcoming contests")){
                            for(int i = 0; i < 15; i++){
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                if(!jsonObject.get("phase").equals("BEFORE")){break;}
                                contestArrayList.add(new Contest((Integer) jsonObject.get("id")
                                        , (String) jsonObject.get("name")
                                        , (String) jsonObject.get("type")
                                        , (String) jsonObject.get("phase")
                                        , (Boolean) jsonObject.get("frozen")
                                        , (Integer) jsonObject.get("durationSeconds")
                                        , (Integer) jsonObject.get("startTimeSeconds")
                                        , (Integer) jsonObject.get("relativeTimeSeconds")));
                            }
                            if(contestArrayList.size() == 0){

                                statusView.setVisibility(View.VISIBLE);
                                statusView.setText("No upcoming contests available");
                            }
                        } else if (s.equals("Past contests")) {
                            for(int i = 0; i < 15; i++) {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                if (jsonObject.get("phase").equals("FINISHED")) {
                                    contestArrayList.add(new Contest((Integer) jsonObject.get("id")
                                            , (String) jsonObject.get("name")
                                            , (String) jsonObject.get("type")
                                            , (String) jsonObject.get("phase")
                                            , (Boolean) jsonObject.get("frozen")
                                            , (Integer) jsonObject.get("durationSeconds")
                                            , (Integer) jsonObject.get("startTimeSeconds")
                                            , (Integer) jsonObject.get("relativeTimeSeconds")));
                                }
                                if(contestArrayList.size() == 0){
                                    statusView.setVisibility(View.VISIBLE);
                                    statusView.setText("No past contests available");
                                }
                            }
                        } else if (s.equals("Live contests")) {
                            for(int i = 0; i < 15; i++) {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                if (jsonObject.get("phase").equals("CODING") || jsonObject.get("phase").equals("PENDING_SYSTEM_TEST") || jsonObject.get("phase").equals("SYSTEM_TEST")) {
                                    contestArrayList.add(new Contest((Integer) jsonObject.get("id")
                                            , (String) jsonObject.get("name")
                                            , (String) jsonObject.get("type")
                                            , (String) jsonObject.get("phase")
                                            , (Boolean) jsonObject.get("frozen")
                                            , (Integer) jsonObject.get("durationSeconds")
                                            , (Integer) jsonObject.get("startTimeSeconds")
                                            , (Integer) jsonObject.get("relativeTimeSeconds")));
                                }
                                if(contestArrayList.size() == 0){
                                    statusView.setVisibility(View.VISIBLE);
                                    statusView.setText("No live contests ongoing right now");
                                }
                            }
                        }
                    } else if (status.equals("FAILED")) {
                        statusView.setVisibility(View.VISIBLE);
                        if(comment.equals("Call limit exceeded")){
                            statusView.setText("Call limit exceeded, only 1 request per 2 seconds is allowed");
                        }else{
                            statusView.setText(comment);
                        }
                    }
                } catch (JSONException e) {

                    throw new RuntimeException(e);
                }


                ContestAdapter adapter = new ContestAdapter(ContestListActivty.this, contestArrayList);
                // Attach the adapter to a ListView
                ListView listView = (ListView) findViewById(R.id.contestList);
                listView.setVisibility(View.VISIBLE);
                listView.setAdapter(adapter);
//                getListViewSize(listView);
                progressBar.setVisibility(View.GONE);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://codeforces.com/contest/"+contestArrayList.get(i).getId()));
                        startActivity(intent);
                    }
                });

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ContestListActivty.this, "Response not proper", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.my_drawer_layout_contest);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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










}