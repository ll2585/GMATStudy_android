package com.lukeli.gmatstudy;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.sql.PreparedStatement;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class GMATTesterModel {
    private HashMap<String, HashMap<String, Question>> possible_questions;
    private HashMap<String, ArrayList<Question>> question_ids;
    private HashMap<String, ArrayList<Question>> flagged_questions;
    private HashMap<String, ArrayList<Question>> unflag_questions;
    private HashMap<String,Object> settings;
    private ArrayList<HashMap<String, Object>> answered_questions;
    private String date;
    private String session_id;
    private Question cur_question;
    private int cur_number;
    private boolean inserted_answers;
    private ArrayList<ArrayList<Object>> already_answered_questions;
    private ArrayList<Question> possible_pool;

    public GMATTesterModel(){
        reset();
    }

    private boolean no_filters(){
        return (settings.get("min_difficulty") .equals("")
        && settings.get("max_difficulty") .equals("")
        && settings.get("min_percentage") .equals("")
        && settings.get("max_percentage") .equals("")
        && settings.get("min_sessions") .equals("")
        && settings.get("max_sessions") .equals("")
        && !(boolean) settings.get("only_unanswered")
        && !(boolean) settings.get("only_answered")
        && !(boolean) settings.get("only_wrong")
        && !(boolean) settings.get("only_right"));
    }

    private boolean limit_based_on_answered_questions(){
        return ((boolean) settings.get("only_unanswered") || (boolean) settings.get("only_answered") || (boolean) settings.get("only_wrong") || (boolean) settings.get("only_right"));
    }

    public HashMap<String, Object> get_question() {
        cur_number += 1;
        cur_question = possible_pool.get(new Random().nextInt(possible_pool.size()));
        return new HashMap<String, Object>(){{
            put("id", cur_question.getId());
            put("question_number", cur_number);
            put("max_questions", possible_pool.size());
            put("question", cur_question.getQuestion());
            put("a", cur_question.getAnswers()[0]);
            put("b", cur_question.getAnswers()[1]);
            put("c", cur_question.getAnswers()[2]);
            put("d", cur_question.getAnswers()[3]);
            put("e", cur_question.getAnswers()[4]);
            put("difficulty", cur_question.getDifficulty_bin_1());
            put("number_correct", cur_question.getDifficulty_bin_2());
            put("has_image", !cur_question.getImage().equals(""));
            put("image_path", cur_question.getImage());
            put("flagged", cur_question.isFlagged());
        }};
    }

    public String get_answer(){
        return cur_question.getAnswer();
    }

    private void load_questions_from_sql(){

        SQLiteDatabase conn = SQLiteUtils.connect("db.db");
        ArrayList<Question> ds_questions = new ArrayList<>();
        ArrayList<Question> ps_questions = new ArrayList<>();
        ArrayList<Question> sc_questions = new ArrayList<>();
        ArrayList<Question> cr_questions = new ArrayList<>();
        ArrayList<Question> rc_questions = new ArrayList<>();
        Cursor cursor = conn.rawQuery("SELECT * FROM DSQuestions", null);
        ArrayList<ArrayList<Object>> ds_qs = SQLiteUtils.fetch_all(cursor);
        cursor = conn.rawQuery("SELECT * FROM PSQuestions", null);
        ArrayList<ArrayList<Object>> ps_qs = SQLiteUtils.fetch_all(cursor);
        cursor = conn.rawQuery("SELECT * FROM SCQuestions", null);
        ArrayList<ArrayList<Object>> sc_qs = SQLiteUtils.fetch_all(cursor);
        cursor = conn.rawQuery("SELECT * FROM CRQuestions", null);
        ArrayList<ArrayList<Object>> cr_qs = SQLiteUtils.fetch_all(cursor);
        cursor.close();
        conn.close();
        for (ArrayList<Object> d : ds_qs) {
            Question this_question = new Question.QuestionBuilder()
                    .setId((int) d.get(0))
                    .setQuestion(MessageFormat.format("{0}\n\n(1) {1}\n(2) {2}", d.get(2), d.get(3), d.get(4)))
                    .setSource((String) d.get(9))
                    .setAnswers(new String[]{"Statement (1) ALONE is sufficient, but statement (2) alone is not sufficient",
                            "Statement (2) ALONE is sufficient, but statement (1) alone is not sufficient",
                            "BOTH statements TOGETHER are sufficient, but NEITHER statement ALONE is sufficient",
                            "EACH statement ALONE is sufficient",
                            "Statements (1) and (2) TOGETHER are NOT sufficient"})
                    .setAnswer((String) d.get(5))
                    .setExplanation((String) d.get(9))
                    .setDifficulty_bin_1((String) d.get(10))
                    .setDifficulty_bin_2((String) d.get(11))
                    .setImage((String) d.get(8))
                    .setType("DS")
                    .setSessions((int) (d.get(12).equals("N/A") ? 0 : d.get(12)))
                    .setFlagged(d.get(13) != null).build();
                ds_questions.add(this_question);
                possible_questions.get("DS").put(String.valueOf(d.get(0)), this_question);
        }
        Collections.sort(ds_questions);
        question_ids.put("DS", ds_questions);

        for (ArrayList<Object> d : ps_qs) {
            Question this_question = new Question.QuestionBuilder()
                    .setId((int) d.get(0))
                    .setQuestion(MessageFormat.format("{0}", d.get(2)))
                    .setSource((String) d.get(12))
                    .setAnswers(new String[]{(String) d.get(3),
                            (String) d.get(4),
                            (String) d.get(5),
                            (String) d.get(6),
                            (String) d.get(7)})
                    .setAnswer((String) d.get(8))
                    .setExplanation((String) d.get(12))
                    .setDifficulty_bin_1((String) d.get(13))
                    .setDifficulty_bin_2((String) d.get(14))
                    .setImage((String) d.get(11))
                    .setType("PS")
                    .setSessions((int) (d.get(15).equals("N/A") ? 0 : d.get(15)))
                    .setFlagged(d.get(16) != null).build();
            ps_questions.add(this_question);
            possible_questions.get("PS").put(String.valueOf(d.get(0)), this_question);
        }
        Collections.sort(ps_questions);
        question_ids.put("PS", ps_questions);

        for (ArrayList<Object> d : sc_qs) {
            if(((int) d.get(0)) == 555){
                Log.d("gmatstudy", (String) d.get(2));
            }
            Question this_question = new Question.QuestionBuilder()
                    .setId((int) d.get(0))
                    .setQuestion(MessageFormat.format("{0}", ((String) d.get(2)).replace("<span style=\"text-decoration: underline\">","<u>")
                            .replace("</span>","</u>")
                            .replace("<br/>"," ")
                            .replace("\n<u>"," <u>")
                            .replace("</u>\n,","</u>,")
                            .replace("</u>\n.","</u>.")
                            .replace("</u>\n","</u> ")
                            .replace("  ", " ")
                            .replace("\n",""))) //replace underlines blahblah
                    .setSource((String) d.get(12))
                    .setAnswers(new String[]{(String) d.get(3),
                            (String) d.get(4),
                            (String) d.get(5),
                            (String) d.get(6),
                            (String) d.get(7)})
                    .setAnswer((String) d.get(8))
                    .setExplanation((String) d.get(12))
                    .setDifficulty_bin_1((String) d.get(13))
                    .setDifficulty_bin_2((String) d.get(14))
                    .setImage((String) d.get(11))
                    .setType("SC")
                    .setSessions((int) (d.get(15).equals("N/A") ? 0 : d.get(15)))
                    .setFlagged(d.get(16) != null).build();
            sc_questions.add(this_question);
            possible_questions.get("SC").put(String.valueOf(d.get(0)), this_question);
        }
        Collections.sort(sc_questions);
        question_ids.put("SC", sc_questions);

        for (ArrayList<Object> d : cr_qs) {
            Question this_question = new Question.QuestionBuilder()
                    .setId((int) d.get(0))
                    .setQuestion(MessageFormat.format("{0}", d.get(2)))
                    .setSource((String) d.get(12))
                    .setAnswers(new String[]{(String) d.get(3),
                            (String) d.get(4),
                            (String) d.get(5),
                            (String) d.get(6),
                            (String) d.get(7)})
                    .setAnswer((String) d.get(8))
                    .setExplanation((String) d.get(12))
                    .setDifficulty_bin_1((String) d.get(13))
                    .setDifficulty_bin_2((String) d.get(14))
                    .setImage((String) d.get(11))
                    .setType("CR")
                    .setSessions((int) (d.get(15).equals("N/A") ? 0 : d.get(15)))
                    .setFlagged(d.get(16) != null).build();
            cr_questions.add(this_question);
            possible_questions.get("CR").put(String.valueOf(d.get(0)), this_question);
        }
        Collections.sort(cr_questions);
        question_ids.put("CR", cr_questions);

    }

    public void answer_question(final String answer, final int[] time_taken) {
        answered_questions.add(new HashMap<String, Object>(){{
            put("date", date);
            put("session_id", session_id);
            put("id", cur_question.getId());
            put("type", cur_question.getType());
            put("my_answer", answer);
            put("right_answer", cur_question.getAnswer());
            put("time_taken", time_taken);
        }}
        );
    }

    public void insert_into_sql_answered_questions() {
        SQLiteDatabase conn = SQLiteUtils.connect("db.db");
        for (HashMap<String, Object> dict : answered_questions){
            String sql = "INSERT INTO AnsweredQs(Date, SessionID, ID, Type, MyAnswer, RightAnswer, SecondsTaken) VALUES (?,?,?,?,?,?,?)";
            SQLiteStatement statement = conn.compileStatement(sql);
            statement.bindString(1, (String) dict.get("date"));
            statement.bindString(2, (String) dict.get("session_id"));
            statement.bindString(3, (String) dict.get("id"));
            statement.bindString(4, (String) dict.get("type"));
            statement.bindString(5, (String) dict.get("my_answer"));
            statement.bindString(6, (String) dict.get("right_answer"));
            statement.bindLong(7, ((int[]) dict.get("time_taken"))[1] + ((int[]) dict.get("time_taken"))[0] * 60);
            statement.executeInsert();
        }
        conn.close();
    }

    public void end_study(boolean see_results){
        update_flagged_questions();
        if((boolean) settings.get("store_answers")){
            if(!inserted_answers && answered_questions.size() > 0){
                insert_into_sql_answered_questions();
                inserted_answers = true;
            }
        }
    }

    public ArrayList<HashMap <String, Object>> get_results(){
        return this.answered_questions;
    }


    private void reset() {
        possible_questions = new HashMap<String, HashMap<String, Question>>(){{
            put("DS", new HashMap<String, Question>());
            put("PS", new HashMap<String, Question>());
            put("SC", new HashMap<String, Question>());
            put("RC", new HashMap<String, Question>());
            put("CR", new HashMap<String, Question>());
        }};
        question_ids = new HashMap<String, ArrayList<Question>>(){{
            put("DS", new ArrayList<Question>());
            put("PS", new ArrayList<Question>());
            put("SC", new ArrayList<Question>());
            put("RC", new ArrayList<Question>());
            put("CR", new ArrayList<Question>());
        }};
        load_questions_from_sql();
        cur_question = null;
        answered_questions = new ArrayList<>();
        flagged_questions = new HashMap<String, ArrayList<Question>>(){{
            put("DS", new ArrayList<Question>());
            put("PS", new ArrayList<Question>());
            put("SC", new ArrayList<Question>());
            put("RC", new ArrayList<Question>());
            put("CR", new ArrayList<Question>());
        }};
        unflag_questions = new HashMap<String, ArrayList<Question>>(){{
            put("DS", new ArrayList<Question>());
            put("PS", new ArrayList<Question>());
            put("SC", new ArrayList<Question>());
            put("RC", new ArrayList<Question>());
            put("CR", new ArrayList<Question>());
        }};
        date = new SimpleDateFormat("yyyy.MM.dd").format(new Date());
        session_id = generate_session_id();
        cur_number = 0;
        inserted_answers = false;
    }

    private String generate_session_id(){
        SQLiteDatabase conn = SQLiteUtils.connect("db.db");
        ArrayList<String> session_ids = new ArrayList<>();
        Cursor c = conn.rawQuery("select DISTINCT SessionID from AnsweredQs", null);
        for(ArrayList<Object> row : SQLiteUtils.fetch_all(c)){
            session_ids.add((String) row.get(0));
        }
        String test = String.valueOf(UUID.randomUUID());
        while(session_ids.contains(test)){
            test = String.valueOf(UUID.randomUUID());
        }
        c.close();
        conn.close();
        return test;
    }

    private ArrayList<ArrayList<Object>> get_answered_questions(){
        SQLiteDatabase conn = SQLiteUtils.connect("db.db");
        Cursor c = conn.rawQuery("SELECT * FROM AnsweredQs", null);
        ArrayList<ArrayList<Object>> result = SQLiteUtils.fetch_all(c);
        c.close();
        conn.close();
        return result;
    }

    public void start_study() {
        reset();
        boolean limit_based_on_answered_questions = limit_based_on_answered_questions();
        if (limit_based_on_answered_questions) {
            already_answered_questions = get_answered_questions();
        }
        possible_pool = new ArrayList<>();
        Set keySet   = ((HashMap) settings.get("questions_to_get")).keySet();
        Iterator<String> keySetIterator = keySet.iterator();
        while (keySetIterator.hasNext()) {
            String type = keySetIterator.next();
            if ((boolean) ((HashMap) settings.get("questions_to_get")).get(type)) {
                if (no_filters()) {
                    possible_pool.addAll(question_ids.get(type));
                }
                else {
                    for (Question possible_q : question_ids.get(type)) {
                        boolean add = true;
                        add = add && (settings.get("min_difficulty").equals("") || (int) settings.get("min_difficulty") <= possible_q.get_difficulty());
                        if (!add){
                            continue;
                        }
                        add = add && (settings.get("max_difficulty").equals("") || (int) settings.get("max_difficulty") >= possible_q.get_difficulty());
                        if (!add){
                            continue;
                        }
                        add = add && (settings.get("min_percentage").equals("") || (int) settings.get("min_percentage") <= possible_q.get_percentage_correct());
                        if (!add){
                            continue;
                        }
                        add = add && (settings.get("max_percentage").equals("") || (int) settings.get("max_percentage") >= possible_q.get_percentage_correct());
                        if (!add){
                            continue;
                        }
                        add = add && (settings.get("min_sessions").equals("") || (int) settings.get("min_sessions") <= possible_q.getSessions());
                        if (!add){
                            continue;
                        }
                        add = add && (settings.get("max_sessions").equals("") || (int) settings.get("max_sessions") >= possible_q.getSessions());
                        if (!add){
                            continue;
                        }
                        if (limit_based_on_answered_questions()){
                            possible_q.set_attribs_based_on_answered(already_answered_questions);
                            add = add && ((! ((boolean) settings.get("only_unanswered"))) ||(((boolean) settings.get("only_unanswered")) && !possible_q.isAnswered()));
                            if (!add){
                                continue;
                            }
                            add = add &&((!((boolean) settings.get("only_answered"))) ||(((boolean) settings.get("only_answered")) && possible_q.isAnswered()));
                            if (!add){
                                continue;
                            }
                            add = add && ((!((boolean) settings.get("only_wrong"))) ||(((boolean) settings.get("only_wrong")) && possible_q.isAnswered()  && !possible_q.isAnswered_right()));
                            if (!add){
                                continue;
                            }
                            add = add && ((!((boolean) settings.get("only_right"))) ||(((boolean) settings.get("only_right")) && possible_q.isAnswered()  && possible_q.isAnswered_right()));
                            if (!add){
                                continue;
                            }

                            add = add &&(settings.get("min_right").equals("") || (!settings.get("min_right").equals("") && possible_q.isAnswered() && possible_q.getTimes_right() >=  (int) settings.get("min_right")));
                            if (!add){
                                continue;
                            }
                            add = add &&(settings.get("min_wrong").equals("") || (!settings.get("min_wrong").equals("") && possible_q.isAnswered() && possible_q.getTimes_wrong() >=  (int) settings.get("min_wrong")));
                            if (!add){
                                continue;
                            }
                        }
                        possible_pool.add(possible_q);
                    }
                }
            }
        }
    }

    private void update_flagged_questions(){

    }

    public void set_settings(HashMap<String, Object> settings){
        this.settings = settings;
    }


    public HashMap<String, Object> get_my_answer_for_row(final int position) {
        String question_type = (String) ((HashMap) answered_questions.get(position)).get("type");
        int question_id = (int) ((HashMap) answered_questions.get(position)).get("id");
        final Question question = possible_questions.get(question_type).get(String.valueOf(question_id));
        HashMap<String, Object> q = new HashMap(){{
            put("id", question.getId());
            put("question_number", position+1);
            put("max_questions", answered_questions.size());
            put("question" , question.getQuestion());
            put("a", question.getAnswers()[0]);
            put("b", question.getAnswers()[1]);
            put("c", question.getAnswers()[2]);
            put("d", question.getAnswers()[3]);
            put("e", question.getAnswers()[4]);
            put("difficulty", question.getDifficulty_bin_1());
            put("number_correct", question.getDifficulty_bin_2());
            put("has_image", !question.getImage().equals(""));
            put("image_path", question.getImage());
            put("flagged", question.isFlagged());
            put("answer_result", new HashMap() {{
                put("my_answer", answered_questions.get(position).get("my_answer"));
                put("time_taken", answered_questions.get(position).get("time_taken"));
                put("right_answer", answered_questions.get(position).get("right_answer"));}});
        }};
        return q;
    }
}
