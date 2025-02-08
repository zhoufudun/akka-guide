package com.lightbend.akka.sample.zfdtest;

import akka.actor.ActorRef;

/**
 *  传输对象
 */
public class Translation {
    private String msg;
    private final ActorRef from;

    public Translation(String name, ActorRef from) {
        this.msg = name;
        this.from = from;
    }

    public ActorRef getFrom() {
        return from;
    }

    public String getMsg() {
        return msg;
    }
}