package com.example;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;


public class HelloWorldMain extends AbstractBehavior<HelloWorldMain.SayHello> {


    public static void main(String[] args) throws Exception {

        final ActorSystem<SayHello> system =
                ActorSystem.create(HelloWorldMain.create(), "hello");

        system.tell(new HelloWorldMain.SayHello("World"));
        system.tell(new HelloWorldMain.SayHello("Akka"));


        Thread.sleep(3000);
        system.terminate();
    }

    // java14提供的类定义：不可变类型

    /**
     * 记录类型自动提供了以下功能：
     * 一个带有所有字段的构造函数。
     * 每个字段的访问器方法（getter）。
     * equals、hashCode 和 toString 方法的实现。
     * 因此，SayHello 记录类型可以用来创建不可变的对象，这些对象包含一个 name 字段，并且可以方便地进行比较和打印
     */
    public static record SayHello(String name) {
    }

    public static Behavior<SayHello> create() {
        return Behaviors.setup(HelloWorldMain::new);
    }

    private final ActorRef<HelloWorld.Greet> greeter;

    private HelloWorldMain(ActorContext<SayHello> context) {
        super(context);
        greeter = context.spawn(HelloWorld.create(), "greeter");
    }

    @Override
    public Receive<SayHello> createReceive() {
        return newReceiveBuilder().onMessage(SayHello.class, this::onSayHello).build();
    }

    private Behavior<SayHello> onSayHello(SayHello command) {
        ActorRef<HelloWorld.Greeted> replyTo =
                getContext().spawn(HelloWorldBot.create(3), command.name);
        greeter.tell(new HelloWorld.Greet(command.name, replyTo));
        return this;
    }
}

