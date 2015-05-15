package com.lukeli.gmatstudy;

import java.util.ArrayList;

public class Question implements Comparable<Question>{
    private String  question;

    public int getId() {
        return id;
    }

    private int     id;
    private String  source;
    private String[]  answers;

    public String getAnswer() {
        return answer;
    }

    public int getTimes_right() {
        return times_right;
    }

    public boolean isAnswered_right() {
        return answered_right;
    }

    public String getType() {
        return type;

    }

    public int getTimes_wrong() {
        return times_wrong;
    }

    public String getImage() {
        return image;
    }

    private String  answer;
    private String  explanation;
    private String  difficulty_bin_1;

    public boolean isAnswered() {
        return answered;
    }

    public int getSessions() {

        return sessions;
    }

    private String  difficulty_bin_2;
    private String  image;
    private String  type;
    private boolean flagged;
    private int  sessions;
    private boolean answered;
    private int times_right;
    private int times_wrong;
    private boolean answered_right;

    public Question(String question, int id, String source, String[] answers, String answer, String explanation, String difficulty_bin_1, String difficulty_bin_2, String image, String type, boolean flagged, int sessions){
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

    public String getQuestion() {
        return question;
    }

    public String getSource() {
        return source;
    }

    public String[] getAnswers() {
        return answers;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getDifficulty_bin_1() {
        return difficulty_bin_1;
    }

    public String getDifficulty_bin_2() {
        return difficulty_bin_2;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void set_attribs_based_on_answered(ArrayList<ArrayList<Object>> answered_questions){
        this.answered = false;
        this.times_right = 0;
        this.times_wrong = 0;

        for (int i = 0; i < answered_questions.size(); i++){
            ArrayList<Object> q = answered_questions.get(i);
            if (q.get(3).equals(this.id) && q.get(4).equals(this.type)){
                this.answered = true;
                this.answered_right = q.get(5) == q.get(6);
                if (q.get(5) == q.get(6)) {
                    this.times_right += 1;
                } else{
                    this.times_wrong += 1;
                }
            }
        }
    }

    @Override
    public int compareTo(Question another) {
        int this_difficulty_1 = this.get_difficulty();
        int this_difficulty_2 = this.get_percentage_correct();
        int that_difficulty_1 = another.get_difficulty();
        int that_difficulty_2 = another.get_percentage_correct();
        //easy -> hard
        if(this_difficulty_1 < that_difficulty_1){
            return -1;
        }else if (this_difficulty_1 > that_difficulty_1){
            return 1;
        }else {
            if(this_difficulty_2 < that_difficulty_2){
                return -1;
            }else if (this_difficulty_2 > that_difficulty_2){
                return 1;
            }else{
                return 0;
            }
        }
    }


    public static class QuestionBuilder {
        public String getQuestion() {
            return question;
        }

        public QuestionBuilder setQuestion(String question) {
            this.question = question;
            return this;
        }

        public int getId() {
            return id;
        }

        public QuestionBuilder setId(int id) {
            this.id = id;
            return this;
        }

        public String getSource() {
            return source;
        }

        public QuestionBuilder setSource(String source) {
            this.source = source;
            return this;
        }

        public String[] getAnswers() {
            return answers;
        }

        public QuestionBuilder setAnswers(String[] answers) {
            this.answers = answers;
            return this;
        }

        public String getAnswer() {
            return answer;
        }

        public QuestionBuilder setAnswer(String answer) {
            this.answer = answer;
            return this;
        }

        public String getExplanation() {
            return explanation;
        }

        public QuestionBuilder setExplanation(String explanation) {
            this.explanation = explanation;
            return this;
        }

        public String getDifficulty_bin_1() {
            return difficulty_bin_1;
        }

        public QuestionBuilder setDifficulty_bin_1(String difficulty_bin_1) {
            this.difficulty_bin_1 = difficulty_bin_1;
            return this;
        }

        public String getDifficulty_bin_2() {
            return difficulty_bin_2;
        }

        public QuestionBuilder setDifficulty_bin_2(String difficulty_bin_2) {
            this.difficulty_bin_2 = difficulty_bin_2;
            return this;
        }

        public String getImage() {
            return image;
        }

        public QuestionBuilder setImage(String image) {
            this.image = image;
            return this;
        }

        public String getType() {
            return type;
        }

        public QuestionBuilder setType(String type) {
            this.type = type;
            return this;
        }

        public boolean isFlagged() {
            return flagged;
        }

        public QuestionBuilder setFlagged(boolean flagged) {
            this.flagged = flagged;
            return this;
        }

        public int getSessions() {
            return sessions;
        }

        public QuestionBuilder setSessions(int sessions) {
            this.sessions = sessions;
            return this;
        }

        private String  question;
        private int     id;
        private String  source;
        private String[]  answers;
        private String  answer;
        private String  explanation;
        private String  difficulty_bin_1;
        private String  difficulty_bin_2;
        private String  image;
        private String  type;
        private boolean flagged;
        private int  sessions;
        private boolean answered;
        private int times_right;
        private int times_wrong;
        private boolean answered_right;

        public QuestionBuilder(){

        }

        public Question build() {
            return new Question(question, id, source, answers, answer, explanation, difficulty_bin_1, difficulty_bin_2, image, type, flagged, sessions);
        }
    }


}
