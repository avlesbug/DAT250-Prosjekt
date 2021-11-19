package no.hvl.dat250.jpa.basicexample.Poll;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

public class subscriptionUpdater {

    private static final String EXCHANGE_NAME = "pollsubscription";

    public void sendUpdate(String question, long pollId, int yesVotes, int noVotes) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        int totalVotes = yesVotes + noVotes;
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

            String message = "Alert: A poll you subscribed to has finished!" +
                    "Question: " + question +
                    "poll ID: " + pollId +
                    "Total votes: " + totalVotes +
                    "Yes votes: " + yesVotes +
                    "No votes: " + noVotes;

            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
/*
    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

            String message = argv.length < 1 ? "Alert: A poll you subscribed to has finished!" +
                    "Question:" +
                    "poll ID:" +
                    "Total votes:" +
                    "Yes votes:" +
                    "No votes:" :
                    String.join(" ", argv);

            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
*/
}

