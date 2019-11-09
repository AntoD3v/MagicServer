package fr.antodev.magicserver.services;

import fr.antodev.magicserver.servers.Server;
import fr.antodev.magicserver.servers.ServerBuilder;
import fr.antodev.magicserver.support.Logs;

import java.util.Iterator;
import java.util.concurrent.*;

public class ProducerService implements Runnable{

    private static BlockingQueue<Server> queueAdding = new LinkedBlockingQueue<Server>();

    @Override
    public void run() {
        for (;;){
            try {
                ServerBuilder.build(queueAdding.take());
                if(queueAdding.isEmpty())
                    Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static BlockingQueue<Server> getQueueAdding() {
        return queueAdding;
    }

}
