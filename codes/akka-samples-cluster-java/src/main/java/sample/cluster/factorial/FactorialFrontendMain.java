package sample.cluster.factorial;


import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;

import akka.cluster.Cluster;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorSystem;
import akka.actor.Props;

public class FactorialFrontendMain {

    public static void main(String[] args) {
        final int upToN = 200;

        final Config config = ConfigFactory.parseString(
                "akka.cluster.roles = [frontend]").withFallback(
                ConfigFactory.load("factorial"));

        final ActorSystem system = ActorSystem.create("ClusterSystem", config);

        system.log().info("Factorials will start when 2 backend members in the cluster.");

        // when current member state be set Up, this callback method will be called
        Cluster.get(system).registerOnMemberUp(new Runnable() {
            @Override
            public void run() {
                system.actorOf(Props.create(FactorialFrontend.class, upToN, true),
                        "factorialFrontend");
            }
        });

        // when current member state be set Removed, this callback method will be called
        Cluster.get(system).registerOnMemberRemoved(new Runnable() {
            @Override
            public void run() {
                // exit JVM when ActorSystem has been terminated
                final Runnable exit = new Runnable() {
                    @Override
                    public void run() {
                        System.exit(0);
                    }
                };
                system.registerOnTermination(exit);

                // shut down ActorSystem
                system.terminate();

                // In case ActorSystem shutdown takes longer than 10 seconds,
                // exit the JVM forcefully anyway.
                // We must spawn a separate thread to not block current thread,
                // since that would have blocked the shutdown of the ActorSystem.
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Await.ready(system.whenTerminated(), Duration.create(10, TimeUnit.SECONDS));
                        } catch (Exception e) {
                            System.exit(-1);
                        }
                    }
                }.start();
            }
        });
    }

}
