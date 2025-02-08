package com.lightbend.akka.sample.zfdtest;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;


public class AkkaServerClientTest {
    public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create("HelloAkka");
        try {
            //#create-actors
            final ActorRef client = system.actorOf(Client.props(), "Client");
            final ActorRef server = system.actorOf(Server.props(), "Server");

            //#main-send-messages
            server.tell(new Server.HandShake("zfd-start-communicate",client), client);

            //#main-send-messages
        } catch (Exception ioe) {

        } finally {
//          system.terminate();
        }
    }
}
