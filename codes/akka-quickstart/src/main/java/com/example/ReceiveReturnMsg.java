package com.example;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;


public class ReceiveReturnMsg extends AbstractBehavior<ReceiveMsg.ReturnMsg> {

    public static Behavior<ReceiveMsg.ReturnMsg> create(int max) {
        return Behaviors.setup(context -> new ReceiveReturnMsg(context, max));
    }

    private final int max;
    private int returnCounter;

    private ReceiveReturnMsg(ActorContext<ReceiveMsg.ReturnMsg> context, int max) {
        super(context);
        this.max = max;
    }

    @Override
    public Receive<ReceiveMsg.ReturnMsg> createReceive() {
        return newReceiveBuilder().onMessage(ReceiveMsg.ReturnMsg.class, this::onReceiveReturnMsg).build();
    }

    private Behavior<ReceiveMsg.ReturnMsg> onReceiveReturnMsg(ReceiveMsg.ReturnMsg message) {
        returnCounter++;
        getContext().getLog().info("I am original sender, I receive ReturnMsg {} for {}", returnCounter, message.from());
        if (returnCounter == max) {
            return Behaviors.stopped();
        } else {
            message.from().tell(new ReceiveMsg.Msg(message.whom(), getContext().getSelf()));
            return this;
        }
    }
}

