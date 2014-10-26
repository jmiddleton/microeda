microEDA
========

MicroEDA provides a simple event-driven messaging layer on top of Hazelcast to be used on Spring applications.

The main driver to built this example is to provide an easy event-driven architecture for Spring applications. After some investigation about different event-driven/actor/reactor/proactor frameworks I found complex the way those frameworks work and difficult to integrate with my actual preferred tecnology stack.

The Event Bus supports 3 integration patterns:

* [Publish-Subscribe](http://www.enterpriseintegrationpatterns.com/PublishSubscribeChannel.html): Event will be delivered to an endpoint. MicroEDA will deliver the event to all Handlers registered to the endpoint.
* [Point-to-point](http://www.enterpriseintegrationpatterns.com/PointToPointChannel.html): The Event will be delivered to only one Handler. If many handlers are registered to the same endpoint, MicroEDA will choose one based on a simple Round Robin algorithm.
* [Request-Reply](http://www.enterpriseintegrationpatterns.com/RequestReply.html): When the client send a Event, it will also specify an EventCallback which will process the response from the EventHandler. This pattern is implemented using Future API. 

MicroEDA can be used to implement a MicroServices Architecture where the services can be distributed across the cluster.

MicroEDA is aligned and shares the same ideas of the [Reactive Manifesto](http://www.reactivemanifesto.org/).

### Getting Started

```
git clone https://github.com/jmiddleton/microeda.git
mvn install
```

TODO: continue....
