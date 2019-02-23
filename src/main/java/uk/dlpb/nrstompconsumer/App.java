package uk.dlpb.nrstompconsumer;

import uk.dlpb.nrstompconsumer.connection.ConnectionFactory;
import uk.dlpb.nrstompconsumer.connection.NRConnectionImpl;
import uk.dlpb.nrstompconsumer.topic.NRTopic;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.Flow;

public class App {

    public static void main(String[] params) throws JMSException {

        File resourceFile = new File(App.class.getClassLoader().getResource("connection.properties").getFile());
        Properties properties = readProperties(resourceFile);
        String host = properties.getProperty("host");
        String password = properties.getProperty("password");
        String username = properties.getProperty("username");
        String clientId = properties.getProperty("clientId");
        Connection connection = new ConnectionFactory(host).createConnection(clientId, username, password);
        final NRConnectionImpl nrConnection = new NRConnectionImpl(connection);
        Feed feed = nrConnection.getFeed(NRTopic.TAIN_MOVEMENT_ALL_TOCS);

        feed.subscribe(new Flow.Subscriber<Message>() {
            @Override
            public void onSubscribe(Flow.Subscription subscription) {

            }

            @Override
            public void onNext(Message item) {
                if(item instanceof TextMessage){
                    try {
                        System.out.println(((TextMessage) item).getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    public static Properties readProperties(File resourceFile){
        Properties prop = new Properties();
        InputStream input = null;

        try {

            input = new FileInputStream(resourceFile);

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            System.out.println(prop.getProperty("user"));
            System.out.println(prop.getProperty("password"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return prop;
    }
}
