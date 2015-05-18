package com.lukeli.gmatstudy;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by luke.li on 5/18/2015.
 */
public class ResultsAdapter extends ArrayAdapter<HashMap<String, Object>> {

    public ResultsAdapter(Context context, ArrayList<HashMap<String, Object>> questions) {
        super(context, R.layout.custom_question_row, questions);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater theInflater = LayoutInflater.from(getContext());

        View theView = theInflater.inflate(R.layout.custom_question_row, parent, false);

        HashMap<String, Object> question = getItem(position);
        String id = String.valueOf(question.get("id"));
        TextView question_id_view = (TextView) theView.findViewById(R.id.question_result_id);
        question_id_view.setText("ID: " + id);

        if((question.get("my_answer")).equals(question.get("right_answer"))){
            (theView.findViewById(R.id.custom_question_linear_layout)).setBackgroundColor(Color.GREEN);
        }else{
            (theView.findViewById(R.id.custom_question_linear_layout)).setBackgroundColor(Color.RED);
        }

        return theView;

    }
}