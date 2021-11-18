package no.hvl.dat250.jpa.basicexample.Poll;

class DweetHandler {
    String url = "https://dweet.io:443/dweet/for/Poll.no";
    int number_Users = 0;
    int number_Polls = 0;
    int open = 0;
    int closed = 0;

    void pollVoteCast(int pollId, int voteStatus) {
        http.post(
                Uri.parse('https://dweet.io:443/dweet/for/Poll.no'),
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
