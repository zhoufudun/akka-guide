package com.lightbend.akka.sample.zfdtest;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.Objects;
import java.util.Scanner;

//#printer-messages
public class Client extends AbstractActor {
    //#printer-messages
    static public Props props() {
        return Props.create(Client.class, () -> new Client());
    }

    //#printer-messages

    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public Client() {
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Server.HandShakeOk.class, handShakeOk -> {
                    log.info("收到握手成功消息");
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("请输入一行内容：");
                    // 读取一行输入
                    String content = scanner.nextLine();
                    System.out.println("你输入的是：" + content);
                    if (Objects.equals(content, "q")) {
                        getContext().getSystem().terminate();
                    } else {
                        handShakeOk.getServerActorRef().tell(new Translation(content, getSelf()), getSelf());
                    }
                }).match(TranslationBack.class, translationBack -> {
                    log.info("收到握手成功消息");
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("请输入一行内容：");
                    // 读取一行输入
                    String content = scanner.nextLine();
                    System.out.println("你输入的是：" + content);
                    if (Objects.equals(content, "q")) {
                        getContext().getSystem().terminate();
                    } else {
                        translationBack.getFrom().tell(new Translation(content, getSelf()), getSelf());
                    }
                }).match(Server.HandShake.class, handShake -> {
                    log.info("Error");
                    getContext().getSystem().terminate();
                })
                .build();
    }
//#printer-messages
}
//#printer-messages
