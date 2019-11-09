package fr.antodev.magicserver.executors;

import fr.antodev.magicserver.servers.Server;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Executors implements Runnable{

    public static ConcurrentLinkedQueue<Server> queues = new ConcurrentLinkedQueue<Server>();
    public static Map<String, ServerExecutor> serversExecutors = new HashMap<>();

    public static void addQueueStarting(Server server){
        queues.add(server);
    }

    @Override
    public void run() {
        for (;;) {
            while (!queues.isEmpty()) {
                Server server = queues.poll();
                ServerExecutor executor = new ServerExecutor(server);
                serversExecutors.put(server.getName(), executor);
                executor.start();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static Map<String, ServerExecutor> getServersExecutors() {
        return serversExecutors;
    }
}
