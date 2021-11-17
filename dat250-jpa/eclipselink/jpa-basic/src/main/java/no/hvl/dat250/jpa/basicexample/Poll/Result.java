package no.hvl.dat250.jpa.basicexample.Poll;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DecimalFormat;

public class Result {
    private String question;
    private Answer opt1;
    private Answer opt2;
    private int votesFor1;
    private int votesFor2;
    private double votesFor1Percent;
    private double votesFor2Percent;
    private Long pollId;
    private Long userId;

    public Result(String question,Answer opt1, Answer opt2, Long pollId, Long userId, int votesFor1, int votesFor2){
        this.question = question;
        this.opt1 = opt1;
        this.opt2 = opt2;
        this.pollId = pollId;
        this.userId = userId;
        this.votesFor1 = votesFor1;
        this.votesFor2 = votesFor2;
        this.votesFor1Percent = getPercentage(votesFor1,votesFor2);
        this.votesFor2Percent = getPercentage(votesFor2,votesFor1);

    }

    public double getPercentage(int first, int second){
        return ((double)first / ((double)first + (double)second))*100;
    }


    public String toJson(){
        Gson gson = new GsonBuilder()
                .serializeSpecialFloatingPointValues()
                .serializeNulls().create();

        String jsonInString = gson.toJson(this);

        return jsonInString;
    }

    public Long getPollId() {
        return this.pollId;
    }

    public Answer getOpt1() {
        return opt1;
    }

    public Answer getOpt2() {
        return opt2;
    }

    public String getQuestion() {
        return question;
    }

    public int getVotesFor1() {
        return votesFor1;
    }

    public int getVotesFor2() {
        return votesFor2;
    }

    public double getVotesFor1Percent() {
        return Math.round(votesFor1Percent * 100.0) / 100.0;
    }

    public double getVotesFor2Percent() {
        return Math.round(votesFor2Percent * 100.0) / 100.0;
    }

    public Long getUserId() {
        return userId;
    }
}
