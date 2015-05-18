package com.lukeli.gmatstudy;

import android.app.Activity;

import java.util.HashMap;

public class GMATTesterPresenter {
    private GMATStudyActivity view;
    private GMATTesterModel model;

    public GMATTesterPresenter(GMATStudyActivity view, GMATTesterModel model){
        this.view = view;
        this.model = model;
    }

    public void show_question(){
        HashMap<String, Object> new_question = model.get_question();
        view.update_main_question(new_question, null);
    }

    public void start_study(HashMap<String, Object> settings){
        model.set_settings(settings);
        model.start_study();
    }

    public void submitted_answer(String answer, int[] time_taken){
        model.answer_question(answer, time_taken);
        view.show_right_answer(model.get_answer());
    }
}

