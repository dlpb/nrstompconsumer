# National Rail Stomp Consumer

This application is intended to consume events from National Rail's Stomp Data feeds.
The data feeds available can be found [at the NR datafeeds portal](https://datafeeds.networkrail.co.uk/ntrod/login)

## Running this application
Requirements:
- Java 11
- Maven
- A NR datafeeds account (this may take some time to provision)

You will need to change the `connection.properties` file with your username and password. The client id should be your username.
Don't change the host - I've found `host = tcp://datafeeds.networkrail.co.uk:61619?wireFormat.maxInactivityDuration=29000` works best.

## Topics
Topics names are NOT the same as the feed names.
A list of feeds names can be found here: https://wiki.openraildata.com/index.php?title=About_the_Network_Rail_feeds
A list of topic names can be found under each feed.