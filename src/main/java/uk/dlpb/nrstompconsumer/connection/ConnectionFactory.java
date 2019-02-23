package uk.dlpb.nrstompconsumer.connection;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.JMSException;

public class ConnectionFactory {
    private String host;

    public ConnectionFactory(String host) {

        this.host = host;
    }

    public Connection createConnection(String clientId, String username, String password) throws JMSException {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(host);
        factory.setClientID(clientId);
        return factory.createConnection(username, password);
    }
}
