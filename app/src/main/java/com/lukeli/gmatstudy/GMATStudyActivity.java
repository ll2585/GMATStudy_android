package com.lukeli.gmatstudy;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import java.text.MessageFormat;
import java.util.HashMap;

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
        answer_widgets = new RadioButton[] {a_button, b_button, c_button, d_button, e_button};
        set_presenter(new GMATTesterPresenter(this, new GMATTesterModel()));
        settings = new HashMap<>();
        start_study();
    }

    public void set_presenter(GMATTesterPresenter presenter){
        this.presenter = presenter;
    }

    public void update_main_question(HashMap<String, Object> question, HashMap<String, Object> answer_result){
        Log.d("gmatstudy", "HERE I SHOW A QUESTION : " + question.toString());
        this.question.setText((String) question.get("question"));
        this.id_label.setText(MessageFormat.format("ID: {0}", question.get("id")));
        this.difficulty_label.setText(MessageFormat.format("Difficulty: {0}", question.get("difficulty")));
        this.number_correct_label.setText(MessageFormat.format("Percentage Correct: {0}", question.get("number_correct")));
        String[] answer_labels = {"(A)", "(B)", "(C)", "(D)", "(E)"};
        String[] letters = {"a", "b", "c", "d", "e"};
        for (int i = 0; i < this.answer_widgets.length; i++){
            this.answer_widgets[i].setVisibility(View.VISIBLE);
            this.answer_widgets[i].setText(MessageFormat.format("{0} {1}",answer_labels[i], question.get(letters[i])));
        }
        /**
         if question['has_image']:
             import os
             question_image = QtGui.QPixmap(os.path.join(os.path.dirname(os.path.realpath(__file__)), question['image_path']))
             self.question_image.setVisible(True)
             self.question_image.setPixmap(question_image)
             self.question_image.show()
         self.settings["num_questions"] = question["max_questions"] if self.settings["num_questions"] == ""  or self.settings["num_questions"] > question["max_questions"] else self.settings["num_questions"]
         self.question_number_label.setText("{0}/{1}".format(question["question_number"], self.settings["num_questions"]))
         if self.question_number_label.text() == "{0}/{0}".format(self.settings["num_questions"]) and not answer_result:
             self.next_question_button.setDisabled(True)
             self.new_question_button.setDisabled(True)
         if answer_result is not None:
             self.reset_question()
             my_answer = answer_result["my_answer"]
             right_answer = answer_result["right_answer"]
             for i in range(0, len(self.answer_widgets)):
                 if right_answer in answer_labels[i]:
                    self.answer_widgets[i].setStyleSheet("* {background-color: rgb(0, 255, 0);}")
                 if my_answer in answer_labels[i]:
                    if my_answer != right_answer:
                        self.answer_widgets[i].setStyleSheet("* {background-color: rgb(255, 0, 0);}")
                    self.answer_widgets[i].setChecked(True)
                     self.submit_answer_button.setVisible(False)
                 self.answer_widgets[i].setDisabled(True)
             m = answer_result["time_taken"][0]
             s = answer_result["time_taken"][1]
             time = "{0:02d}:{1:02d}".format(m,s)
             self.time_taken.setText(time)
         */
    }


    public void start_study(){

        settings.put("show_answer_immediately", true); //show_answer_immediately.isChecked());
        settings.put("num_questions", "");//= '' if number_of_questions.text() == '' else int(number_of_questions.text()));
        settings.put("min_difficulty", "");//= '' if minimum_difficulty.text() == '' else int(minimum_difficulty.text()));
        settings.put("max_difficulty", "");//= '' if maximum_difficulty.text() == '' else int(maximum_difficulty.text()));
        settings.put("min_percentage", "");//= '' if minimum_percentage.text() == '' else int(minimum_percentage.text()));
        settings.put("max_percentage", "");//= '' if maximum_percentage.text() == '' else int(maximum_percentage.text()));
        settings.put("min_sessions", "");//= '' if minimum_sessions.text() == '' else int(minimum_sessions.text()));
        settings.put("max_sessions", "");//= '' if maximum_sessions.text() == '' else int(maximum_sessions.text()));
        settings.put("store_answers", false);//= store_answers.isChecked());
        settings.put("only_unanswered", false);//= only_unanswered_questions.isChecked());
        settings.put("only_answered", false);//= only_answered_questions.isChecked());
        settings.put("only_wrong", false);//= only_wrong_questions.isChecked());
        settings.put("only_right",false);// = only_right_questions.isChecked());
        settings.put("show_stats",false);// = show_answered_stats.isChecked());
        settings.put("min_wrong", "");//= '' if min_wrong.text() == '' else int(min_wrong.text()));
        settings.put("min_right", "");//= '' if min_right.text() == '' else int(min_right.text()));
        settings.put("questions_to_get", new HashMap<String, Object>(){{
            put("DS", true);//DS_checkbox.isChecked());
            put("PS", false);//PS_checkbox.isChecked());
            put("SC", false);//SC_checkbox.isChecked());
            put("CR", false);//CR_checkbox.isChecked());
        }});
        presenter.start_study(settings);
        show_question();
    }

    private void show_question(){
        /**
        self.reset_question()
        if self.question_number_label.text() == "{0}/{0}".format(self.settings["num_questions"]):
        self.end_study_and_see_results()
        else:
        self.presenter.show_question()
        self.timer.start(1000)
         **/
        presenter.show_question();
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
}
