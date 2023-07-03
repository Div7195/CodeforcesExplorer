package com.div.codeforcesexplorer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;



import java.util.ArrayList;

public class ContestAdapter extends ArrayAdapter<Contest> {
    public ContestAdapter(Context context, ArrayList<Contest> contestItems) {
        super(context, 0, contestItems);
    }
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Contest contest = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contest_item, parent, false);
        }
        TextView nameView = (TextView) convertView.findViewById(R.id.contestNameField);
        TextView typeView = (TextView) convertView.findViewById(R.id.contestTypeField);
        TextView durationView =(TextView) convertView.findViewById(R.id.contestDurationField);
        TextView startView = (TextView) convertView.findViewById(R.id.contestStartField);
        if(contest.getName() != null){
            nameView.setText(contest.getName());
        }
        if(contest.getType() != null){
            typeView.setText(contest.getType());
        }
        if(contest.getDurationSeconds() != null){
            Integer hours = (contest.getDurationSeconds() / 3600);
            Double minutes = ((double) contest.getDurationSeconds() / 3600 - hours)*60;
            durationView.setText(hours+"hr "+minutes.intValue()+"min");

        }
        if(contest.getStartTimeSeconds() != null){
            java.util.Date time=new java.util.Date((long)contest.getStartTimeSeconds()*1000);
            startView.setText(time.toString());
        }

        return convertView;
    }
}
