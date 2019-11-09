package fr.antodev.magicserver.services;

import com.google.gson.JsonParser;
import fr.antodev.magicserver.executors.Executors;
import fr.antodev.magicserver.executors.ServerExecutor;
import fr.antodev.magicserver.servers.Server;
import fr.antodev.magicserver.servers.ServerAction;
import fr.antodev.magicserver.support.Logs;
import fr.antodev.magicserver.support.exchange.JedisConnection;
import redis.clients.jedis.JedisPubSub;

public class AddingServersService extends JedisConnection implements Runnable{
    @Override
    public void run() {
        getJedis().subscribe(new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                if(channel.equalsIgnoreCase("adding_server")){

                try {
                    Server server = new Server(new JsonParser().parse(message).getAsJsonObject());
                    if (server.isCorrect()) {
                        Logs.info("Adding " + server.getName() + " in queues for create !");
                        ProducerService.getQueueAdding().put(server);
                    }
                }catch (Exception e){
                    Logs.warn("Invalid json for message: "+message);
                }
                }else if(channel.equalsIgnoreCase("action_server")){
                    ServerExecutor serverExecutor;
                    try {
                        ServerAction action = new ServerAction(new JsonParser().parse(message).getAsJsonObject());
                        if (action.isCorrect()) {
                            Logs.info("Action "+action.getAction()+" for "+action.getName()+" !");
                            if(Executors.getServersExecutors().containsKey(action.getName())) {
                                switch (action.getAction()){
                                    case STOP:
                                        Executors.getServersExecutors().get(action.getName()).stopServer();
                                        break;
                                    case COMMAND:
                                        Executors.getServersExecutors().get(action.getName()).sendCommand("");
                                        break;
                                    case FORCED_STOP:
                                        Executors.getServersExecutors().get(action.getName()).forcedStopServer();
                                        break;

                                }
                            }

                        }
                    }catch (Exception e){
                        Logs.warn("Invalid json for message: "+message);
                    }
                }
            }
        }, "adding_server", "action_server");
    }
}
