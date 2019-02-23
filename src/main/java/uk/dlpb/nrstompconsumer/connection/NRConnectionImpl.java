package uk.dlpb.nrstompconsumer.connection;

import org.apache.activemq.command.ActiveMQTopic;
import uk.dlpb.nrstompconsumer.Feed;

import javax.jms.*;

public class NRConnectionImpl implements NRConnection {

    private final Session session;
    private Connection connection;

    public NRConnectionImpl(Connection connection) throws JMSException {
        this.connection = connection;

        init();
        this.session = createSession();
    }

    private Session createSession() throws JMSException {
        return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    private void init() throws JMSException {
        this.connection.start();
    }


    public Feed getFeed(Topic topic) throws JMSException {
        String topicName = topic.getTopicName();
        Topic nrTopic = new ActiveMQTopic(topicName);
        MessageConsumer consumer = session.createDurableSubscriber(nrTopic, makeDestination(topicName));

        return new Feed(consumer);
    }

    public String makeDestination(String name){
        return "DEV:" + name;
    }
}
