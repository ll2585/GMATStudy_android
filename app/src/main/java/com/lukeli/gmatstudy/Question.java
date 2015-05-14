package com.lukeli.gmatstudy;

import java.util.ArrayList;

public class Question {
    private String  question;
    private int     id;
    private String  source;
    private String  answers;
    private String  answer;
    private String  explanation;
    private String  difficulty_bin_1;
    private String  difficulty_bin_2;
    private String  image;
    private String  type;
    private boolean flagged;
    private String  sessions;
    private boolean answered;
    private int times_right;
    private int times_wrong;
    private boolean answered_right;

    public Question(String question, int id, String source, String answers, String answer, String explanation, String difficulty_bin_1, String difficulty_bin_2, String image, String type, boolean flagged, String sessions){
        this.question = question;
        this.id = id;
        this.source = source;
        this.answers = answers;
        this.answer = answer;
        this.explanation = explanation;
        this.difficulty_bin_1 = difficulty_bin_1;
        this.difficulty_bin_2 = difficulty_bin_2;
        this.image = image;
        this.type = type;
        this.flagged = flagged;
        this.sessions = sessions;
    }

    public int get_difficulty(){
        if(this.difficulty_bin_1.equals("N/A")){
            return 0;
        }
        return Integer.parseInt(this.difficulty_bin_1.replace("%",""));
    }

    public int get_percentage_correct(){
        if(this.difficulty_bin_2.equals("N/A")){
            return 100;
        }
        return Integer.parseInt(this.difficulty_bin_2.replace("%",""));
    }

    public void set_attribs_based_on_answered(ArrayList<Object> answered_questions){
        this.answered = false;
        this.times_right = 0;
        this.times_wrong = 0;
        for (int i = 0; i < answered_questions.size(); i++){
            /***
            Object[] q = answered_questions.get(i);
            if (answered_questions.get(i)[3] == this.id && q[4] == this.type){
                this.answered = true;
                this.answered_right = q[5] == q[6];
                if q[5] == q[6]:
                print("IS RIGHT")
                this.times_right += 1
                else:
                print("IS WRONG")
                this.times_wrong += 1
            }***/
        }
    }

}
