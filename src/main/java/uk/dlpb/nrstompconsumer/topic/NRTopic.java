package uk.dlpb.nrstompconsumer.topic;

import javax.jms.JMSException;
import javax.jms.Topic;

public enum NRTopic implements Topic {
    TAIN_MOVEMENT_ALL_TOCS("TRAIN_MVT_ALL_TOC");

    private String topicName;

    NRTopic(String topicName){
        this.topicName = topicName;
    }

    public String getTopicName() throws JMSException {
        return topicName;
    }
}
