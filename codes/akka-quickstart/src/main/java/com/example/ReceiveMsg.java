package com.example;


import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;


public class ReceiveMsg extends AbstractBehavior<ReceiveMsg.Msg> {

    public static class Msg {
        public final String whom;
        public final ActorRef<ReturnMsg> replyTo;

        public Msg(String whom, ActorRef<ReturnMsg> replyTo) {
            this.whom = whom;
            this.replyTo = replyTo;
        }

        public String whom() {
            return whom;
        }

        public ActorRef<ReturnMsg> replyTo() {
            return replyTo;
        }
    }

//    public static record Greeted(String whom, ActorRef<Greet2> from) {
//    }

    public static class ReturnMsg {
        public final String whom;
        public final ActorRef<Msg> from;

        public ReturnMsg(String whom, ActorRef<Msg> from) {
            this.whom = whom;
            this.from = from;
        }

        public ActorRef<Msg> from() {
            return from;
        }

        public String whom() {
            return whom;
        }

    }

    public static Behavior<Msg> create() {
        return Behaviors.setup(ReceiveMsg::new);
    }

    private ReceiveMsg(ActorContext<Msg> context) {
        super(context);
    }

    @Override
    public Receive<Msg> createReceive() {
        return newReceiveBuilder().onMessage(Msg.class, this::onReceiveMsg).build();
    }

    private Behavior<Msg> onReceiveMsg(Msg command) {
        getContext().getLog().info("I am ReceiveMsg, Hello {}!", command.whom);
        command.replyTo.tell(new ReturnMsg(command.whom, getContext().getSelf()));
        return this;
    }
}

