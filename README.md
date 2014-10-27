microEDA
========

MicroEDA provides a simple event-driven messaging layer on top of Hazelcast to be used on Spring applications.

After evaluating different event-driven/actor/reactor/proactor frameworks, I found complex the way those frameworks work and difficult to integrate with my preferred tecnology stack so I decided to build this small library following some good ideas from Vert.x. The final product is a Distributed Event Bus on top of Hazelcast that dispatches messages across different JVM. 

The Event Bus supports 3 integration patterns:

* [Publish-Subscribe](http://www.enterpriseintegrationpatterns.com/PublishSubscribeChannel.html): Event will be delivered to an endpoint. MicroEDA will deliver the event to all Handlers registered to the endpoint.
* [Point-to-point](http://www.enterpriseintegrationpatterns.com/PointToPointChannel.html): The Event will be delivered to only one Handler. If many handlers are registered to the same endpoint, MicroEDA will choose one based on a simple Round Robin algorithm.
* [Request-Reply](http://www.enterpriseintegrationpatterns.com/RequestReply.html): When the client send a Event, it will also specify an EventCallback which will process the response from the EventHandler. This pattern is implemented using Future API. 

MicroEDA can be used to implement a MicroServices Architecture where the services can be distributed across the cluster.

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
 .
 2014-10-27 18:10:53.047  INFO 15313 --- [main] ar.tunuyan.eda.MicroServiceBus: Started MicroServiceBus in 6.346 seconds (JVM running for 6.67)
```

Now, open a new terminal and execute the following commands:
```
cd microeda/microeda-test
mvn test
```

<<<<<<< HEAD
After that, on the first console, you should see entry logs prefixed with "Quote n:"
=======
If the run executed successfully, on the first console you should see entry logs prefixed with "Quote".
>>>>>>> FETCH_HEAD


TODO: continue....
