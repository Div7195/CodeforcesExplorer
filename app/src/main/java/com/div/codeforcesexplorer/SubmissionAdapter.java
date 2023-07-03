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

public class SubmissionAdapter extends ArrayAdapter<Submission> {
    public SubmissionAdapter(Context context, ArrayList<Submission> submissionItems) {
        super(context, 0, submissionItems);
    }
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Submission submission = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.submission_item, parent, false);
        }
        TextView nameView = (TextView) convertView.findViewById(R.id.problemNameField);
        TextView verdictView = (TextView) convertView.findViewById(R.id.verdictField);
        TextView languageView =(TextView) convertView.findViewById(R.id.languageField);
        TextView startView = (TextView) convertView.findViewById(R.id.submissionTimeField);
        if(submission.getName() != null){
            nameView.setText(submission.getName());
        }
        if(submission.getVerdict() != null){
            verdictView.setText(submission.getVerdict());
        }

        if(submission.getTime() != null){
            java.util.Date time=new java.util.Date((long)submission.getTime()*1000);
            startView.setText(time.toString());
        }
        if(submission.getLanguage() != null){
            languageView.setText(submission.getLanguage());
        }

        return convertView;
    }
}
