package com.dlpb.nrstompconsumer.connection;

import org.junit.Test;
import uk.dlpb.nrstompconsumer.connection.ConnectionFactory;

import javax.jms.Connection;
import javax.jms.JMSException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class ConnectionFactoryTest {

    @Test
    public void shouldCreateConnectionWithRequiredParameters() throws JMSException {
        String host = "";
        String username = "";
        String password = "";
        String clientId = "clientId";

        Connection connection = new ConnectionFactory(host).createConnection(clientId, username, password);
        assertThat(connection.getClientID(), is(equalTo(clientId)));
    }

}
