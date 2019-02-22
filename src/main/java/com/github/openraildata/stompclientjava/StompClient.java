package com.github.openraildata.stompclientjava;

import org.fusesource.stomp.jms.StompJmsConnectionFactory;
import javax.jms.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class StompClient implements Runnable {

    public static void main(String[] args) throws Exception {
        new StompClient().run();
    }

    public void run() {
        String brokerUri = "tcp://datafeeds.nationalrail.co.uk:61618";
        Map<String, String> secrets = readSecuritySecrets();
        String QUEUE_NAME = secrets.get("key");

        StompJmsConnectionFactory connectionFactory = new StompJmsConnectionFactory();
        connectionFactory.setBrokerURI(brokerUri);

        Connection connection = null;
        Session session = null;
        MessageConsumer consumer = null;

        try {
            connection = connectionFactory.createConnection(secrets.get("username"), secrets.get("password"));
            connection.start();

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue(QUEUE_NAME);
            consumer = session.createConsumer(queue);

            System.out.println("Connected to STOMP " + brokerUri);

            consumer.setMessageListener(new MessageHandler());

            while (!Thread.interrupted()) {}

            try {
                if (consumer != null) {
                    consumer.close();
                }

                if (session != null) {
                    session.close();
                }

                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (JMSException ex) {
                System.out.println("Got exception on shutdown");
                ex.printStackTrace();
            }

            System.out.println("Thread was interrupted!");


        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private Map<String, String> readSecuritySecrets() {
        Map<String, String> secrets = new HashMap<String, String>();
        try {
            String data = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("security.key").toURI())));
            for(String line: data.split("\n")) {
                String[] parts = line.split(":");
                secrets.put(parts[0], parts[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return secrets;
    }
}