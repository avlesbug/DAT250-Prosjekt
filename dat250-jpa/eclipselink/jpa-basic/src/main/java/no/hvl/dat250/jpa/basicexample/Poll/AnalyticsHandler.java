package no.hvl.dat250.jpa.basicexample.Poll;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class AnalyticsHandler {
    private String analyticsLog = "";
    private static final String EXCHANGE_NAME = "analytics";

    //Method to open the channel for receiving analytics. Will store the analytics in variable analytics log.
    public void analyticsReceiver() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        System.out.println("[*] Waiting for analytics from poll... To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received Analytics! '" + message + "'");
            analyticsLog = analyticsLog + message + "\n";
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
    }

    //Method to publish analytics to the receiver.
    public void sendAnalytics(long pollId, int yesVotes, int noVotes) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        int totalVotes = yesVotes + noVotes;
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

            String message = "--ANALYTICS--" + "\n" + "pId:" + pollId + ", votes:" + totalVotes + ", yesVotes:" + yesVotes + ", noVotes:" + noVotes;

            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
            System.out.println("Analytics have been sent!");
        }
    }

    //Method to save the whole analytics log
    public void saveAnalyticsLog() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("logs.txt"));
        writer.write(analyticsLog);

        writer.close();
    }
}
