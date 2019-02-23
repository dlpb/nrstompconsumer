package uk.dlpb.nrstompconsumer.connection;

import uk.dlpb.nrstompconsumer.Feed;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.Topic;

public interface NRConnection {

    Feed getFeed(Topic topic) throws JMSException;
}
