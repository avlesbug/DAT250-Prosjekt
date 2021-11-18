package no.hvl.dat250.jpa.basicexample.Poll;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Sender {
    private final static String QUEUE_NAME = "pollqueue";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false,false,false, null);
            String message = "A poll you voted on has now been closed!";
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("[x] sent message '" + message + "'");
        }
    }
}
