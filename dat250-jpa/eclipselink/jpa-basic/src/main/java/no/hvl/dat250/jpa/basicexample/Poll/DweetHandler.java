package no.hvl.dat250.jpa.basicexample.Poll;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;

class DweetHandler {
    String url = "https://dweet.io:443/dweet/for/Poll.no";

    // Inform Dweet about a vote being cast (Post the info to dweet.)
    void pollVoteCast(Long pollId, Answer voteStatus) throws IOException {
        String payload = "Vote Cast={" +
                "\"pollID\":" + pollId.toString() +
                "\"voteStatus\":" + voteStatus.toString() +
                "}";
        StringEntity entity = new StringEntity(payload,
                ContentType.APPLICATION_FORM_URLENCODED);

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(url);
        request.setEntity(entity);
        httpClient.execute(request);
    }
    //Inform dweet about poll creation
    void pollCreateCast(Long pollId, String pollQuestion, String endTime) throws IOException {
        String payload = "Poll Created={" +
                "\"pollID\":" + pollId.toString() +
                "\"Poll Question\":" + pollQuestion +
                "\"Poll End Time\":" + endTime +
                "}";

        StringEntity entity = new StringEntity(payload,
                ContentType.APPLICATION_FORM_URLENCODED);

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(url);
        request.setEntity(entity);
        httpClient.execute(request);

    }

    void pollCloseCast(Long pollId, int votesOpt1, int votesOpt2) throws IOException {
        int votes = votesOpt1 + votesOpt2;
        String payload = "Poll Ended={" +
                "\"pollID\":" + pollId.toString() +
                "\"Total votes\":" + votes +
                "\"Yes votes\":" +  votesOpt1 +
                "\"No votes\":" + votesOpt2 +
                "\"Percentage yes votes\":" + ((votesOpt1/votes) * 100) +
                "}";

        StringEntity entity = new StringEntity(payload,
                ContentType.APPLICATION_FORM_URLENCODED);

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(url);
        request.setEntity(entity);
        httpClient.execute(request);
    }
/*
    public static void main(String[] args) throws IOException {
        String payload = "data={" +
                "\"username\": \"admin\", " +
                "\"first_name\": \"System\", " +
                "\"last_name\": \"Administrator\"" +
                "}";

        StringEntity entity = new StringEntity(payload,
                ContentType.APPLICATION_FORM_URLENCODED);

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost("https://dweet.io:443/dweet/for/Poll.no");
        request.setEntity(entity);
        httpClient.execute(request);
    }

 /*
    public static void main(String[] args) throws Exception {
        String payload = "data={" +
                "\"pollID\":" + "1" +
                "\"voteStatus\":" + "Yes" +
                "}";
        StringEntity entity = new StringEntity(payload,
                ContentType.APPLICATION_FORM_URLENCODED);

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost("https://dweet.io:443/dweet/for/Poll.no");
        request.setEntity(entity);

        HttpResponse response = httpClient.execute(request);
        System.out.println(response.getStatusLine().getStatusCode());
    }

    /*
    void pollVoteCast(int pollId, int voteStatus) {
        http.post(
                URI.create('https://dweet.io:443/dweet/for/Poll.no'),
                headers: <String, String>{
                'Content-Type': 'application/json; charset=UTF-8',
      },
        body: jsonEncode(<String, int>{
            'Vote': voteStatus, //Indicates if a user voted yes or no, 1 for yes, 0
                    'pollId': pollId
        }),
    );
    }

    void pollCreateCast(String pollId, String question, String polCreatorId) {
        http.post(
                Uri.parse('https://dweet.io:443/dweet/for/Poll.no'),
                headers: <String, String>{
                'Content-Type': 'application/json; charset=UTF-8',
      },
        body: jsonEncode(<String, String>{
                'Question': question, // Poll question
                'Poll ID': pollId, // ID of the poll
                'Poll Creator userID': polCreatorId //User ID of the poll creator
      }),
    );
    }

    void pollDeleteCast(String pollId, String question, String polCreatorId) {
        http.post(
                Uri.parse('https://dweet.io:443/dweet/for/Poll.no'),
                headers: <String, String>{
                'Content-Type': 'application/json; charset=UTF-8',
      },
        body: jsonEncode(<String, String>{
                'Question': question,
                'Poll ID': pollId,
                'Poll Creator userID': polCreatorId,
                'Result Yes': "pending implementation",
                'Result No': "pending implementation",
                'Percentage Agreed': "pending implementation"
      }),
    );
    }

    void pollCreationUpdate() {
        http.post(
                Uri.parse('https://dweet.io:443/dweet/for/Poll.no'),
                headers: <String, String>{
                'Content-Type': 'application/json; charset=UTF-8',
      },
        body: jsonEncode(<String, int>{
            'Total Polls': number_Polls,
                    'Open Polls': open,
                    'Closed Polls': closed,
                    'Total Users': number_Users
        }),
    );
    }

    void initDweeter() {
        http.post(
                Uri.parse('https://dweet.io:443/dweet/for/Poll.no'),
                headers: <String, String>{
                'Content-Type': 'application/json; charset=UTF-8',
      },
        body: jsonEncode(<String, int>{
            'Total Polls': number_Polls,
                    'Open Polls': open,
                    'Closed Polls': closed,
                    'Total Users': number_Users
        }),
    );
    }

    void pollCreated() {
        number_Polls = number_Polls + 1;
        open = open + 1;
        http.post(
                Uri.parse('https://dweet.io:443/dweet/for/Poll.no'),
                headers: <String, String>{
                'Content-Type': 'application/json; charset=UTF-8',
      },
        body: jsonEncode(<String, int>{
            'Total Polls': number_Polls,
                    'Open Polls': open,
                    'Closed Polls': closed,
                    'Total Users': number_Users
        }),
    );
    }

    void pollClosed() {
        open = open - 1;
        closed = closed + 1;
        http.post(
                Uri.parse('https://dweet.io:443/dweet/for/Poll.no'),
                headers: <String, String>{
                'Content-Type': 'application/json; charset=UTF-8',
      },
        body: jsonEncode(<String, int>{
            'Total Polls': number_Polls,
                    'Open Polls': open,
                    'Closed Polls': closed,
                    'Total Users': number_Users
        }),
    );
    }

    void userCreated() {
        number_Users = number_Users + 1;
        http.post(
                Uri.parse('https://dweet.io:443/dweet/for/Poll.no'),
                headers: <String, String>{
                'Content-Type': 'application/json; charset=UTF-8',
      },
        body: jsonEncode(<String, int>{
            'Total Polls': number_Polls,
                    'Open Polls': open,
                    'Closed Polls': closed,
                    'Total Users': number_Users
        }),
    );
    }

    void userDeleted() {
        number_Users = number_Users - 1;
        http.post(
                Uri.parse('https://dweet.io:443/dweet/for/Poll.no'),
                headers: <String, String>{
                'Content-Type': 'application/json; charset=UTF-8',
      },
        body: jsonEncode(<String, int>{
            'Total Polls': number_Polls,
                    'Open Polls': open,
                    'Closed Polls': closed,
                    'Total Users': number_Users
        }),
    );
    }
  /*
  TO-DO: Update to match requirements:
  "I think informing dweet.io about:
- Poll creation (including question details)
- Votes on polls
- Poll closing
should be sufficient. If there is information on dweet.io to have an IoT device that shows live poll data (as one use case for dweet.io) that is more than enough."
  */
}

