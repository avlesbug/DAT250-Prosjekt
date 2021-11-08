package no.hvl.dat250.jpa.basicexample.Poll;

import com.google.gson.Gson;

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
        Gson gson = new Gson();

        String jsonInString = gson.toJson(this);

        return jsonInString;
    }

}
