package com.dlpb.nrstompconsumer.connection;

import org.apache.activemq.command.ActiveMQTopic;
import org.junit.Test;
import uk.dlpb.nrstompconsumer.Feed;
import uk.dlpb.nrstompconsumer.connection.NRConnectionImpl;
import uk.dlpb.nrstompconsumer.topic.NRTopic;

import javax.jms.*;

import java.util.concurrent.Flow;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class NRConnectionImplTest {

    @Test
    public void shouldInitConnectionOnConstruction() throws JMSException {
        Connection connection = mock(Connection.class);

        new NRConnectionImpl(connection);

        verify(connection, times(1)).start();
        verify(connection, times(1)).createSession(false, Session.AUTO_ACKNOWLEDGE);

    }

    @Test
    public void shouldGetFeedForTopic() throws JMSException {

        Topic topic = NRTopic.TAIN_MOVEMENT_ALL_TOCS;
        NRConnectionImpl nrConnection = getNrConnection(topic);

        Feed feed = nrConnection.getFeed(topic);
        assertThat(feed.getTopic().getTopicName(), is(equalTo(topic.getTopicName())));
    }

    @Test
    public void shouldSubscribeToFeedAndGetEvents() throws JMSException {
        Topic topic = NRTopic.TAIN_MOVEMENT_ALL_TOCS;
        NRConnectionImpl nrConnection = getNrConnection(topic);

        Message message = mock(Message.class);
        Feed feed = nrConnection.getFeed(topic);
        feed.subscribe(new Flow.Subscriber<Message>() {
            @Override
            public void onSubscribe(Flow.Subscription subscription) {

            }

            @Override
            public void onNext(Message item) {
                assertThat(item, is(equalTo(message)));
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });
        feed.onMessage(message);

    }

    private NRConnectionImpl getNrConnection(Topic topic) throws JMSException {
        String topicName = topic.getTopicName();
        ActiveMQTopic activeMQTopic = new ActiveMQTopic(topicName);

        Connection connection = mock(Connection.class);

        Session mockSession = mock(Session.class);
        when(connection.createSession(false, Session.AUTO_ACKNOWLEDGE)).thenReturn(mockSession);
        TopicSubscriber mockTopicSubscriber = mock(TopicSubscriber.class);
        when(mockTopicSubscriber.getTopic()).thenReturn(activeMQTopic);

        NRConnectionImpl nrConnection = new NRConnectionImpl(connection);
        when(mockSession.createDurableSubscriber(activeMQTopic, nrConnection.makeDestination(topicName))).thenReturn(mockTopicSubscriber);
        return nrConnection;
    }

    @Test
    public void shouldMakeDesinationForTopicName() throws JMSException {
        Connection connection = mock(Connection.class);

        String destination = new NRConnectionImpl(connection).makeDestination("TEST");

        assertThat(destination, is(equalTo("DEV:TEST")));
    }
}
