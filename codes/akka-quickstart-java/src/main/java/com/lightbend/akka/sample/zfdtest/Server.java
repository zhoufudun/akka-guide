package com.lightbend.akka.sample.zfdtest;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

//#greeter-messages
public class Server extends AbstractActor {
    //#greeter-messages
    static public Props props() {
        return Props.create(Server.class, () -> new Server());
    }

    //#greeter-messages
    static public class HandShake {
        private String name;
        private final ActorRef clientActorRef;

        public HandShake(String name, ActorRef clientActorRef) {
            this.name = name;
            this.clientActorRef = clientActorRef;
        }

        public ActorRef getClientActorRef() {
            return clientActorRef;
        }

        public String getName() {
            return name;
        }
    }

    //#greeter-messages
    static public class HandShakeOk {
        private String name;
        private final ActorRef serverActorRef;

        public HandShakeOk(String name, ActorRef serverActorRef) {
            this.name = name;
            this.serverActorRef = serverActorRef;
        }

        public ActorRef getServerActorRef() {
            return serverActorRef;
        }

        public String getName() {
            return name;
        }
    }

    private ActorRef clientActorRef;
    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(HandShake.class, handShake -> {
                    log.info("Server received handshake from client: {}", handShake.getName());
                    clientActorRef=handShake.getClientActorRef();
                    clientActorRef.tell(new HandShakeOk("server return ok",getSelf()), getSelf());
                })
                .match(Translation.class, msg -> {
                    log.info("Server received translation from client: {}", msg.getMsg());
                    clientActorRef.tell(new TranslationBack("server return ok",getSelf()), getSelf());
                })
                .build();
    }
//#greeter-messages
}
//#greeter-messages
