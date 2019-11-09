package fr.antodev.magicserver.servers;

import com.google.gson.JsonObject;
import fr.antodev.magicserver.support.Logs;

public class Server {

    private ServerType serverType;
    private String ip = "localhost";
    private int port = 0;
    private int maxPlayer;
    private String name;
    private boolean correct = false;

    public Server(JsonObject jsonObject) {
        if(jsonObject.has("name")
                && jsonObject.has("type")
                && jsonObject.has("max-players")){
            this.name = jsonObject.get("name").getAsString();
            this.serverType = ServerType.valueOf(jsonObject.get("type").getAsString());
            this.maxPlayer = jsonObject.get("max-players").getAsInt();
            correct = true;
        }else
            Logs.warn("Parsing error");
    }

    public Server(String name, ServerType serverType, String ip, int port, int maxPlayer) {
        this.name = name;
        this.serverType = serverType;
        this.ip = ip;
        this.port = port;
        this.maxPlayer = maxPlayer;
    }

    public String getName() {
        return name;
    }

    public String getIP() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public ServerType getServerType() {
        return serverType;
    }

    public int getMaxPlayers() {
        return maxPlayer;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public JsonObject toJson(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", getName());
        jsonObject.addProperty("type", getServerType().name());
        jsonObject.addProperty("ip", getIP());
        jsonObject.addProperty("port", getPort());
        jsonObject.addProperty("max-players", getMaxPlayers());
        return jsonObject;
    }

    public boolean isCorrect() {
        return correct;
    }
}
