microEDA
========

MicroEDA provides a simple event-driven messaging layer on top of Hazelcast to be used on Spring applications.

The main driver to built this example is to provide an easy event-driven architecture for Spring applications. After some investigation about different event-driven/actor/reactor/proactor frameworks I found complex the way those frameworks work and difficult to integrate with my actual preferred tecnology stack.

The Event Bus supports 3 integration patterns:
1.- [Publish-Subscribe](http://www.enterpriseintegrationpatterns.com/PublishSubscribeChannel.html)
2.- [Point-to-point](http://www.enterpriseintegrationpatterns.com/PointToPointChannel.html)
3.- [Request-Reply](http://www.enterpriseintegrationpatterns.com/RequestReply.html). This pattern is implemented using async in memory response. When the client send a message, it also defines the EventCallback that will process the response from the EventHandler.

This library can be used to implement a MicroServices architecture using distributed Event Handler ....  

### Getting Started

```
git clone https://github.com/jmiddleton/microeda.git
mvn install
```

TODO: continue....
