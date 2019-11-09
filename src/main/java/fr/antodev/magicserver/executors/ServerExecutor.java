package fr.antodev.magicserver.executors;

import fr.antodev.magicserver.servers.Server;
import fr.antodev.magicserver.support.Logs;
import fr.antodev.magicserver.support.Options;
import redis.clients.jedis.Jedis;

import java.io.*;
import java.util.regex.Pattern;

public class ServerExecutor extends Thread {

    private final Server server;
    private static Runtime runtime = Runtime.getRuntime();
    private Process processus = null;
    private boolean started;
    private static Jedis jedis = new Jedis(Options.JEDIS_HOST);

    public ServerExecutor(Server server) {
        super(server.getName());
        this.server = server;
    }

    @Override
    public void run() {
        bootingServer();
        if(processus == null){
            Logs.warn("Server "+server.getName()+" would not start");
            return;
        }
        new Thread(){
            @Override
            public void run() {
                Pattern pattern = Pattern.compile("Done");
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(processus.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if(pattern.matcher(line).find()){
                            Logs.debug("["+server.getName()+"] Ready server");
                            jedis.publish("ready_server", server.toJson().toString());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        while (processus != null){
            if(!processus.isAlive()){
                Logs.info("Close and destroy server " + server.getName());
                break;
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopServer() {
        sendCommand("stop");
    }


    public void forcedStopServer() {
        processus.destroy();
    }

    public void bootingServer(){
        Logs.info("Starting server " + server.getName()+" in "+server.getIP()+":"+server.getPort());
        String[] cmdLinux = { "/bin/sh", "-c", "cd " + Options.HOSTED+server.getName() + "; java -jar spigot.jar" };
        String[] cmdWindows = { "cmd.exe", "/c", "cd \"" + Options.HOSTED+server.getName() + "\" & java -jar spigot.jar" };

        try {
            this.processus = runtime.exec((isLinux()) ? cmdLinux : cmdWindows);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.started = true;
    }

    public void sendCommand(String command) {
        try {
            OutputStream ouput = processus.getOutputStream();
            ouput.write(command.getBytes());
            ouput.flush();
            ouput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean isStarted() {
        return started;
    }

    public boolean isLinux() {
        if(System.getProperty("os.name").toLowerCase().indexOf("win") >= 0)
            return false;
        else
            return true;
    }
}
