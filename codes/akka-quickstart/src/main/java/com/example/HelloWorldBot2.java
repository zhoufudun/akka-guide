package com.example;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;


public class HelloWorldBot2 extends AbstractBehavior<HelloWorld2.Greeted> {

    public static Behavior<HelloWorld2.Greeted> create(int max) {
        return Behaviors.setup(context -> new HelloWorldBot2(context, max));
    }

    private final int max;
    private int greetingCounter;

    private HelloWorldBot2(ActorContext<HelloWorld2.Greeted> context, int max) {
        super(context);
        this.max = max;
    }

    @Override
    public Receive<HelloWorld2.Greeted> createReceive() {
        return newReceiveBuilder().onMessage(HelloWorld2.Greeted.class, this::onGreeted).build();
    }

    private Behavior<HelloWorld2.Greeted> onGreeted(HelloWorld2.Greeted message) {
        greetingCounter++;
        getContext().getLog().info("Greeting {} for {}", greetingCounter, message.from());
        if (greetingCounter == max) {
            return Behaviors.stopped();
        } else {
            message.from().tell(new HelloWorld2.Greet(message.whom(), getContext().getSelf()));
            return this;
        }
    }
}

