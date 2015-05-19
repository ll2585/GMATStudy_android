package com.lukeli.gmatstudy;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.HashMap;


public class GMATStartStudyActivity extends ActionBarActivity {

    private CheckBox PS_checkbox;
    private CheckBox DS_checkbox;
    private CheckBox SC_checkbox;
    private CheckBox CR_checkbox;
    private CheckBox RC_checkbox;
    private CheckBox store_answers;
    private CheckBox only_unanswered_questions;
    private CheckBox only_answered_questions;
    private CheckBox only_wrong_questions;
    private CheckBox only_right_questions;
    private CheckBox show_answered_stats;
    private CheckBox show_answer_immediately;
    private EditText minimum_difficulty;
    private EditText number_of_questions;
    private EditText maximum_difficulty;
    private EditText minimum_percentage;
    private EditText maximum_percentage;
    private EditText minimum_sessions;
    private EditText maximum_sessions;
    private EditText min_wrong;
    private EditText min_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_study);
        show_answer_immediately = (CheckBox) findViewById(R.id.show_answer_immediately);
        minimum_difficulty = (EditText) findViewById(R.id.minimum_difficulty);
        PS_checkbox = (CheckBox) findViewById(R.id.PS_checkbox);
        DS_checkbox = (CheckBox) findViewById(R.id.DS_checkbox);
        SC_checkbox = (CheckBox) findViewById(R.id.SC_checkbox);
        CR_checkbox = (CheckBox) findViewById(R.id.CR_checkbox);
        RC_checkbox = (CheckBox) findViewById(R.id.RC_checkbox);
        store_answers =  (CheckBox) findViewById(R.id.store_answers);
        only_unanswered_questions = (CheckBox)findViewById(R.id.only_unanswered_questions);
        only_answered_questions = (CheckBox)findViewById(R.id.only_answered_questions);
        only_wrong_questions = (CheckBox)findViewById(R.id.only_wrong_questions);
        only_right_questions = (CheckBox)findViewById(R.id.only_right_questions);
        show_answered_stats = (CheckBox)findViewById(R.id.show_answered_stats);
        minimum_difficulty = (EditText)findViewById(R.id.minimum_difficulty);
        number_of_questions = (EditText)findViewById(R.id.number_of_questions);
        maximum_difficulty = (EditText) findViewById(R.id.maximum_difficulty);
        minimum_percentage = (EditText) findViewById(R.id.minimum_percentage);
        maximum_percentage = (EditText) findViewById(R.id.maximum_percentage);
        minimum_sessions = (EditText)findViewById(R.id.minimum_sessions);
        maximum_sessions = (EditText) findViewById(R.id.maximum_sessions);
        min_wrong = (EditText)findViewById(R.id.min_wrong);
        min_right = (EditText)findViewById(R.id.min_right);
        GlobalVars gv = (GlobalVars)getApplicationContext();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gmatstart_study, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void start_study(View view) {
        Intent intent = new Intent(this, GMATStudyActivity.class);
        Bundle settings = new Bundle();

        settings.putString("min_difficulty", String.valueOf(minimum_difficulty.getText()));
        settings.putString("num_questions",  String.valueOf(number_of_questions.getText()));
        settings.putString("max_difficulty", String.valueOf(maximum_difficulty.getText()));
        settings.putString("min_percentage", String.valueOf(minimum_percentage.getText()));
        settings.putString("max_percentage", String.valueOf(maximum_percentage.getText()));
        settings.putString("min_sessions",   String.valueOf(minimum_sessions.getText()));
        settings.putString("max_sessions",   String.valueOf(maximum_sessions.getText()));
        settings.putString("min_wrong",      String.valueOf(min_wrong.getText()));
        settings.putString("min_right",      String.valueOf(min_right.getText()));

        settings.putBoolean("show_answer_immediately", show_answer_immediately.isChecked());
        settings.putBoolean("store_answers", store_answers.isChecked());
        settings.putBoolean("only_unanswered", only_unanswered_questions.isChecked());
        settings.putBoolean("only_answered", only_answered_questions.isChecked());
        settings.putBoolean("only_wrong", only_wrong_questions.isChecked());
        settings.putBoolean("only_right", only_right_questions.isChecked());
        settings.putBoolean("show_stats", show_answered_stats.isChecked());

        settings.putSerializable("questions_to_get", new HashMap<String, Object>() {{
            put("DS", DS_checkbox.isChecked());//DS_checkbox.isChecked());
            put("PS", PS_checkbox.isChecked());//PS_checkbox.isChecked());
            put("SC", SC_checkbox.isChecked());//SC_checkbox.isChecked());
            put("CR", CR_checkbox.isChecked());//CR_checkbox.isChecked());
            put("RC", RC_checkbox.isChecked());//CR_checkbox.isChecked());
        }});
        intent.putExtras(settings);
        startActivity(intent);

    }
}
