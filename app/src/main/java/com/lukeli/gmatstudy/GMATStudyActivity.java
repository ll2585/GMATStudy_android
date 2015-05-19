package com.lukeli.gmatstudy;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class GMATStudyActivity extends ActionBarActivity { //TODO: Make Navigation Drawer new Activity

    private GMATTesterPresenter presenter;
    private HashMap<String, Object> settings;
    private TextView question;
    private TextView id_label;
    private TextView difficulty_label;
    private TextView number_correct_label;
    private RadioButton a_button;
    private RadioButton b_button;
    private RadioButton c_button;
    private RadioButton d_button;
    private RadioButton e_button;
    private RadioButton[] answer_widgets;
    private Button submit_answer_button;
    private Button next_question_button;
    private RadioGroup answer_button_radiogroup;
    private int m;
    private int s;
    private Chronometer timer;
    private TextView question_number_label;
    private Button new_question_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_screen);
        question = (TextView) findViewById(R.id.question_id);
        id_label = (TextView) findViewById(R.id.id_id);
        difficulty_label = (TextView) findViewById(R.id.difficulty_label);
        number_correct_label = (TextView) findViewById(R.id.number_correct_label);
        a_button = (RadioButton) findViewById(R.id.a_button);
        b_button = (RadioButton) findViewById(R.id.b_button);
        c_button = (RadioButton) findViewById(R.id.c_button);
        d_button = (RadioButton) findViewById(R.id.d_button);
        e_button = (RadioButton) findViewById(R.id.e_button);
        answer_button_radiogroup = (RadioGroup) findViewById(R.id.answer_button_radiogroup);
        answer_widgets = new RadioButton[] {a_button, b_button, c_button, d_button, e_button};
        next_question_button = (Button) findViewById(R.id.next_question_button);
        submit_answer_button = (Button) findViewById(R.id.submit_answer_button);
        new_question_button = (Button) findViewById(R.id.new_question_button);
        question_number_label = (TextView) findViewById(R.id.question_number_label);
        timer = (Chronometer) findViewById(R.id.timer);
        GlobalVars gv = (GlobalVars)getApplicationContext();
        set_presenter(gv.getPresenter());
        gv.getPresenter().set_study_view(this);
        settings = new HashMap<>();
        start_study();
    }

    public void set_presenter(GMATTesterPresenter presenter){
        this.presenter = presenter;
    }

    public void update_main_question(HashMap<String, Object> question, HashMap<String, Object> answer_result){
        Log.d("gmatstudy", "HERE I SHOW A QUESTION : " + question.toString());
        this.question.setText(Html.fromHtml((String) question.get("question")));
        this.id_label.setText(MessageFormat.format("ID: {0}", question.get("id")));
        this.difficulty_label.setText(MessageFormat.format("Diff: {0}", question.get("difficulty")));
        this.number_correct_label.setText(MessageFormat.format("Percent Correct: {0}", question.get("number_correct")));
        String[] answer_labels = {"(A)", "(B)", "(C)", "(D)", "(E)"};
        String[] letters = {"a", "b", "c", "d", "e"};
        for (int i = 0; i < this.answer_widgets.length; i++){
            this.answer_widgets[i].setVisibility(View.VISIBLE);
            this.answer_widgets[i].setText(MessageFormat.format("{0} {1}",answer_labels[i], question.get(letters[i])));
        }
        settings.put("num_questions", settings.get("num_questions").equals("") || (int) settings.get("num_questions") > (int) question.get("max_questions") ? question.get("max_questions") : settings.get("num_questions"));
        question_number_label.setText(MessageFormat.format("{0}/{1}",question.get("question_number"), settings.get("num_questions")));
        if (String.valueOf(question_number_label.getText()).equals(MessageFormat.format("{0}/{0}",settings.get("num_questions")))){
            next_question_button.setEnabled(false);
            new_question_button.setEnabled(false);
        }
        /**
         if question['has_image']:
             import os
             question_image = QtGui.QPixmap(os.path.join(os.path.dirname(os.path.realpath(__file__)), question['image_path']))
             self.question_image.setVisible(True)
             self.question_image.setPixmap(question_image)
             self.question_image.show()

         */
    }


    public void start_study(){

        Bundle bundle = this.getIntent().getExtras();
        if(bundle!=null) {

            settings.put("num_questions" , bundle.getString("num_questions" ).equals("") ? "" : Integer.parseInt(bundle.getString("num_questions" )));
            settings.put("min_difficulty", bundle.getString("min_difficulty").equals("") ? "" : Integer.parseInt(bundle.getString("min_difficulty")));
            settings.put("max_difficulty", bundle.getString("max_difficulty").equals("") ? "" : Integer.parseInt(bundle.getString("max_difficulty")));
            settings.put("min_percentage", bundle.getString("min_percentage").equals("") ? "" : Integer.parseInt(bundle.getString("min_percentage")));
            settings.put("max_percentage", bundle.getString("max_percentage").equals("") ? "" : Integer.parseInt(bundle.getString("max_percentage")));
            settings.put("min_sessions"  , bundle.getString("min_sessions"  ).equals("") ? "" : Integer.parseInt(bundle.getString("min_sessions"  )));
            settings.put("max_sessions"  , bundle.getString("max_sessions"  ).equals("") ? "" : Integer.parseInt(bundle.getString("max_sessions"  )));
            settings.put("min_wrong"     , bundle.getString("min_wrong"     ).equals("") ? "" : Integer.parseInt(bundle.getString("min_wrong"     )));
            settings.put("min_right"     , bundle.getString("min_right"     ).equals("") ? "" : Integer.parseInt(bundle.getString("min_right"     )));

            settings.put("show_answer_immediately", bundle.getBoolean("show_answer_immediately")); //show_answer_immediately.isChecked());
            settings.put("store_answers",           bundle.getBoolean("store_answers" ));//= store_answers.isChecked());
            settings.put("only_unanswered",         bundle.getBoolean("only_unanswered"));//= only_unanswered_questions.isChecked());
            settings.put("only_answered",           bundle.getBoolean("only_answered"));//= only_answered_questions.isChecked());
            settings.put("only_wrong",              bundle.getBoolean("only_wrong"));//= only_wrong_questions.isChecked());
            settings.put("only_right",              bundle.getBoolean("only_right"));// = only_right_questions.isChecked());
            settings.put("show_stats",              bundle.getBoolean("show_stats"));// = show_answered_stats.isChecked());
            settings.put("questions_to_get", bundle.getSerializable("questions_to_get"));
        }
        for (RadioButton widget : answer_widgets) {
            widget.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    submit_answer_button.setVisibility(View.GONE);
                    for (RadioButton widget : answer_widgets) {
                        if(widget.isChecked()){
                            submit_answer_button.setVisibility(View.VISIBLE);
                            break;
                        }
                    }
                }
            });
            widget.setVisibility(View.INVISIBLE);
            widget.setBackgroundColor(Color.TRANSPARENT);
        }

        new SQLLoaderView().execute();

        m = 0;
        s = 0;
    }

    private void show_question(){
        reset_question();
        if(String.valueOf(question_number_label.getText()).equals(MessageFormat.format("{0}/{0}", settings.get("num_questions")))){
            end_study_and_see_results(null);
        }else{
            presenter.show_question();
            timer.start();
        }

    }

    private void reset_question() {
        next_question_button.setVisibility(View.GONE);
        //question_image.setVisible(False)
        for (RadioButton widget : answer_widgets) {
            widget.setEnabled(true);
            widget.setChecked(false);
            widget.setBackgroundColor(Color.TRANSPARENT);
        }
        answer_button_radiogroup.clearCheck();
        timer.stop();
        timer.setBase(SystemClock.elapsedRealtime());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void show_question(View view) {
        show_question();
    }

    public void submit_answer(View view) {
        timer.stop();
        long elapsedMillis = SystemClock.elapsedRealtime() - timer.getBase();
        m = (int) TimeUnit.MILLISECONDS.toMinutes(elapsedMillis);
        s = (int) TimeUnit.MILLISECONDS.toSeconds(elapsedMillis) % 60;
        String[] answer_labels = {"A", "B", "C", "D", "E"};
        for (int i =0; i < answer_widgets.length; i++) {
            if(answer_widgets[i].isChecked()) {
                presenter.submitted_answer(answer_labels[i],new int[] {m, s});
                break;
            }
        }
    }

    public void show_right_answer(String right_answer){
        if (!(boolean) settings.get("show_answer_immediately")){
            show_question();
            return;
        }
        String[] answer_labels = {"A", "B", "C", "D", "E"};
        for(int i = 0; i < answer_widgets.length; i++) {
            if (answer_labels[i].equals(right_answer) ) {
                answer_widgets[i].setBackgroundColor(Color.GREEN);
            }
            else if (answer_widgets[i].isChecked()) {
                answer_widgets[i].setBackgroundColor(Color.RED);
            }
            answer_widgets[i].setEnabled(false);
        }
        next_question_button.setVisibility(View.VISIBLE);
        submit_answer_button.setVisibility(View.GONE);
    }

    public void end_study_and_see_results(View view) {
        this.timer.stop();
        this.presenter.end_study(true);

    }

    public void show_results(){
        Intent go_to_results_intent = new Intent(this, GMATResultsActivity.class); //this auto gets the results i guess...
        startActivity(go_to_results_intent);
    }

    public void end_study(View view) {
        presenter.end_study(false);
        finish();
    }


    private class SQLLoaderView extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            presenter.start_study(settings);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            show_question();
        }
    }
}
