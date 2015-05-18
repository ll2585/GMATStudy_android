package com.lukeli.gmatstudy;

import android.app.Activity;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;


public class GMATResultsActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    private ArrayList<HashMap<String, Object>> answered_questions;
    private GMATTesterPresenter presenter;
    private int fragments_created = 0;
    private QuestionFragment question_fragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GlobalVars gv = (GlobalVars)getApplicationContext();
        this.presenter = gv.getPresenter();
        presenter.set_results_view(this);

        setContentView(R.layout.activity_gmatresults);
        Log.d("gmatstudy", "STRARTED OTEHR ACTIVTYY" + gv.getPresenter().get_model().get_question());
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
         .replace(R.id.container, new DefaultFragment())
         .commit();

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Log.d("gmatstudy", " NEW SECTION SELECTED: " + String.valueOf(position) + " AND " + fragments_created);

        // update the main content by replacing fragments
        if(fragments_created == 1){
            question_fragment = new QuestionFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
             .replace(R.id.container, question_fragment)
             .commit();
        }else if(fragments_created > 1){
            presenter.show_question_results(position);

        }
        if(fragments_created <= 1){
            fragments_created += 1;
        }
        Log.d("gmatstudy", " NEW SECTION SELECTED: " + String.valueOf(position) + " ANDNOW " + fragments_created);


    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.gmatresults, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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

    public void update_main_question(HashMap<String, Object> new_question, HashMap<String, Object> answer_result) {
        question_fragment.set_question(new_question, answer_result);
    }


    public static class DefaultFragment extends Fragment {


        public DefaultFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.default_results_screen, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
        }
    }

    public static class QuestionFragment extends Fragment {

        private View view;
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

        public QuestionFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.question_screen, container, false);
            view.findViewById(R.id.new_question_button).setVisibility(View.GONE);
            view.findViewById(R.id.back_to_main_button).setVisibility(View.GONE);
            view.findViewById(R.id.end_button).setVisibility(View.GONE);
            question = (TextView) view.findViewById(R.id.question_id);
            id_label = (TextView) view.findViewById(R.id.id_id);
            difficulty_label = (TextView) view.findViewById(R.id.difficulty_label);
            number_correct_label = (TextView) view.findViewById(R.id.number_correct_label);
            a_button = (RadioButton) view.findViewById(R.id.a_button);
            b_button = (RadioButton) view.findViewById(R.id.b_button);
            c_button = (RadioButton) view.findViewById(R.id.c_button);
            d_button = (RadioButton) view.findViewById(R.id.d_button);
            e_button = (RadioButton) view.findViewById(R.id.e_button);
            answer_button_radiogroup = (RadioGroup) view.findViewById(R.id.answer_button_radiogroup);
            answer_widgets = new RadioButton[] {a_button, b_button, c_button, d_button, e_button};
            next_question_button = (Button) view.findViewById(R.id.next_question_button);
            submit_answer_button = (Button) view.findViewById(R.id.submit_answer_button);
            timer = (Chronometer) view.findViewById(R.id.timer);
            return view;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
        }

        public void set_question(HashMap<String, Object> question, HashMap<String, Object> answer_result) {
            /***
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
             */

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
            if(answer_result != null) {
                reset_question();
                String my_answer = (String) answer_result.get("my_answer");
                String right_answer = (String)  answer_result.get("right_answer");
                for (int i = 0; i < answer_widgets.length; i++){
                    if(answer_labels[i].contains(right_answer)) {
                        answer_widgets[i].setBackgroundColor(Color.GREEN);
                    }
                    if(answer_labels[i].contains(my_answer)) {
                        if(!my_answer.equals(right_answer)) {
                            answer_widgets[i].setBackgroundColor(Color.RED);
                        }
                        answer_widgets[i].setChecked(true);
                        submit_answer_button.setVisibility(View.GONE);
                    }
                    answer_widgets[i].setEnabled(false);
                }
                m = ((int[]) answer_result.get("time_taken"))[0];
                s = ((int[]) answer_result.get("time_taken"))[1];
                timer.setBase(timer.getBase() - s*1000+m*60*1000);
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
    }

    public void set_answered_questions(ArrayList<HashMap<String, Object>> list){
        this.answered_questions = list;
    }

    public ArrayList<HashMap<String, Object>> get_answered_questions(){
        return this.answered_questions;
    }

}
