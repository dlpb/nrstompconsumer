package uk.dlpb.nrstompconsumer;

import javax.jms.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow;

public class Feed implements MessageListener, Flow.Publisher<Message> {
    private MessageConsumer consumer;
    private List<Flow.Subscriber<? super Message>> subscribers = new ArrayList<>();

    public Feed(MessageConsumer consumer) throws JMSException {
        this.consumer = consumer;
        consumer.setMessageListener(this);
    }

    public Topic getTopic() throws JMSException {
        return ((TopicSubscriber) consumer).getTopic();
    }


    public void onMessage(Message message) {
        for (Flow.Subscriber<? super Message> subscriber: subscribers) {
            subscriber.onNext(message);
        }
    }

    public void subscribe(Flow.Subscriber<? super Message> subscriber) {
        this.subscribers.add(subscriber);
    }
}
