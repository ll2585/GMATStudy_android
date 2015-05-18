package com.lukeli.gmatstudy;

import android.app.Activity;

import java.util.ArrayList;
import java.util.HashMap;

public class GMATTesterPresenter {
    private GMATStudyActivity study_view;
    private GMATResultsActivity results_view;
    private GMATTesterModel model;

    public GMATTesterPresenter(GMATTesterModel model){
        this.model = model;
    }

    public void set_study_view(GMATStudyActivity view){
        this.study_view = view;
    }

    public void set_results_view(GMATResultsActivity view){
        this.results_view = view;
        ArrayList<HashMap<String, Object>> study_results = model.get_results();
        results_view.set_answered_questions(study_results);
    }

    public void show_question(){
        HashMap<String, Object> new_question = model.get_question();
        study_view.update_main_question(new_question, null);
    }

    public void start_study(HashMap<String, Object> settings){
        model.set_settings(settings);
        model.start_study();
    }

    public void submitted_answer(String answer, int[] time_taken){
        model.answer_question(answer, time_taken);
        study_view.show_right_answer(model.get_answer());
    }

    public GMATTesterModel get_model() {
        return this.model;
    }

    public void end_study(boolean see_results) {
        model.end_study(see_results);
        if (see_results) {
            study_view.show_results();
        }
    }


    public void show_question_results(int position) {

        HashMap<String, Object> new_question = model.get_my_answer_for_row(position);
        HashMap<String, Object> answer_result = (HashMap<String, Object>) new_question.get("answer_result");
        results_view.update_main_question(new_question, answer_result);
    }
}

