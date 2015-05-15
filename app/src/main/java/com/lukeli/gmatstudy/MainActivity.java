package com.lukeli.gmatstudy;

import android.app.Activity;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks{ //TODO: Make Navigation Drawer new Activity

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private FirstFragment frag;
    private GMATTesterPresenter presenter;
    private HashMap<String, Object> settings;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_screen);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //add a fragment
        frag = new FirstFragment();
        fragmentTransaction.add(R.id.container, frag);
        fragmentTransaction.commit();
        settings = new HashMap<>();
        start_study();
    }

    public void set_presenter(GMATTesterPresenter presenter){
        this.presenter = presenter;
    }

    public void update_main_question(HashMap<String, Object> question, HashMap<String, Object> answer_result){
        Log.d("gmatstudy","HERE I SHOW A QUESTION : " + question.toString());
        if(getSupportFragmentManager().findFragmentById(R.id.container) != null) {
            ((TextView) getSupportFragmentManager().findFragmentById(R.id.container).getView().findViewById(R.id.question_id)).setText((String) question.get("question"));
        }
        /**
         if question['has_image']:
         import os
         question_image = QtGui.QPixmap(os.path.join(os.path.dirname(os.path.realpath(__file__)), question['image_path']))
         self.question_image.setVisible(True)
         self.question_image.setPixmap(question_image)
         self.question_image.show()
         self.question.setText(question["question"])
         self.settings["num_questions"] = question["max_questions"] if self.settings["num_questions"] == ""  or self.settings["num_questions"] > question["max_questions"] else self.settings["num_questions"]
         self.question_number_label.setText("{0}/{1}".format(question["question_number"], self.settings["num_questions"]))
         if self.question_number_label.text() == "{0}/{0}".format(self.settings["num_questions"]) and not answer_result:
         self.next_question_button.setDisabled(True)
         self.new_question_button.setDisabled(True)
         self.id_label.setText("ID: {0}".format(question["id"]))
         self.difficulty_label.setText("Difficulty: {0}".format(question["difficulty"]))
         self.number_correct_label.setText("Percentage Correct: {0}".format(question["number_correct"]))
         answer_labels = ["(A)", "(B)", "(C)", "(D)", "(E)"]
         letters = ["a", "b", "c", "d", "e"]
         for i in range(0, len(self.answer_widgets)):
         self.answer_widgets[i].setVisible(True)
         self.answer_widgets[i].setText("{0} {1}".format(answer_labels[i], question[letters[i]]))
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

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        if(getSupportFragmentManager().findFragmentById(R.id.container) != null){
            Log.d("THIS", ((TextView) getSupportFragmentManager().findFragmentById(R.id.container).getView().findViewById(R.id.question_id)).getText().toString());
        }


    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
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

    @SuppressWarnings("deprecation")
    // Changes the title of the page to the current fragments title
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
            getMenuInflater().inflate(R.menu.main, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
