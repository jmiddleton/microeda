microEDA
========

MicroEDA provides a simple event-driven messaging layer on top of Hazelcast. MicroEDA is based on Spring framework and can be easily embedded in a single JVM or clustered across multiples nodes. The design is heavily influenced by [Vert.x](http://vertx.io) and the [Reactor pattern](http://en.wikipedia.org/wiki/Reactor_pattern).

After evaluating different event-driven/actor/reactor/proactor frameworks, I found complex the way those frameworks work and difficult to integrate with my preferred tecnology stack so I decided to build this small library following some simple good ideas from Vert.x. The final product is a Distributed Event Bus on top of Hazelcast that dispatches messages across different JVM.

![MicroEDA Components](/docs/h_microEDA.png?raw=true "MicroEDA Components")

The Event Bus supports 3 integration patterns:

* [Publish-Subscribe](http://www.enterpriseintegrationpatterns.com/PublishSubscribeChannel.html): Event will be delivered to an endpoint. MicroEDA will deliver the event to all Handlers registered to the endpoint.
* [Point-to-point](http://www.enterpriseintegrationpatterns.com/PointToPointChannel.html): The Event will be delivered to only one Handler. If many handlers are registered to the same endpoint, MicroEDA will choose one based on a simple Round Robin algorithm.
* [Request-Reply](http://www.enterpriseintegrationpatterns.com/RequestReply.html): When the client send a Event, it will also specify an EventCallback which will process the response from the EventHandler. This pattern is implemented using Future API. 

MicroEDA can be used to implement a Microservices Architecture where the services are distributed across the cluster.

MicroEDA is aligned and shares the same ideas of the [Reactive Manifesto](http://www.reactivemanifesto.org/).

### Prerequisites
* Java 1.8
* Maven 3.1 or later

### Getting Started

Open a terminal and run the following commands:
```
git clone https://github.com/jmiddleton/microeda.git
mvn install -DskipTests=true

cd microeda-cluster
./start.sh
```

If everything compiled correctly, you should see something like this:

```
  __  __ _                  ______ _____          
 |  \/  (_)                |  ____|  __ \   /\    
 | \  / |_  ___ _ __ ___   | |__  | |  | | /  \   
 | |\/| | |/ __| '__/ _ \  |  __| | |  | |/ /\ \  
 | |  | | | (__| | | (_) | | |____| |__| / ____ \ 
 |_|  |_|_|\___|_|  \___/  |______|_____/_/    \_\
 _________________________________________________
      Event-driven asynchronous/non-blocking
 _________________________________________________
2014-10-27 18:10:46.950  INFO 15313 --- [main] ar.tunuyan.eda.MicroServiceBus: Starting MicroServiceBus on Jorge-MacBook-Pro.local with PID ...
 .

 .
 2014-10-27 18:10:53.047  INFO 15313 --- [main] ar.tunuyan.eda.MicroServiceBus: Started MicroServiceBus in 6.346 seconds (JVM running for 6.67)
```

It is also possible to start the application as exploded jar as shown below. This is useful when we need to add our own jar files. Just drop the jar under target/lib folder.
```
$ cd target
$ unzip -q microeda-cluster-1.0.0-SNAPSHOT.jar
$ java org.springframework.boot.loader.JarLauncher
```

Now, open a new terminal and execute the following commands:
```
cd microeda/microeda-test
mvn test
```
If all tests run successfully, on the cluster console you should see entry logs prefixed with "Quote".


TODO: continue....
