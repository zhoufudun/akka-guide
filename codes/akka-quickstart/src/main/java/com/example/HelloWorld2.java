package com.example;


import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;


public class HelloWorld2 extends AbstractBehavior<HelloWorld2.Greet> {

    public static class Greet {
        public final String whom;
        public final ActorRef<Greeted> replyTo;

        public Greet(String whom, ActorRef<Greeted> replyTo) {
            this.whom = whom;
            this.replyTo = replyTo;
        }

        public String whom() {
            return whom;
        }

        public ActorRef<Greeted> replyTo() {
            return replyTo;
        }
    }

//    public static record Greeted(String whom, ActorRef<Greet2> from) {
//    }

    public static class Greeted {
        public final String whom;
        public final ActorRef<Greet> from;

        public Greeted(String whom, ActorRef<Greet> from) {
            this.whom = whom;
            this.from = from;
        }

        public ActorRef<Greet> from() {
            return from;
        }

        public String whom() {
            return whom;
        }

    }

    public static Behavior<Greet> create() {
        return Behaviors.setup(HelloWorld2::new);
    }

    private HelloWorld2(ActorContext<Greet> context) {
        super(context);
    }

    @Override
    public Receive<Greet> createReceive() {
        return newReceiveBuilder().onMessage(Greet.class, this::onGreet).build();
    }

    private Behavior<Greet> onGreet(Greet command) {
        getContext().getLog().info("Hello {}!", command.whom);
        command.replyTo.tell(new Greeted(command.whom, getContext().getSelf()));
        return this;
    }
}

