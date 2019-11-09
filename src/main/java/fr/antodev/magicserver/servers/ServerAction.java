package fr.antodev.magicserver.servers;

import com.google.gson.JsonObject;
import fr.antodev.magicserver.support.Logs;

public class ServerAction {

    private boolean correct = false;
    private String name;
    private Action action;

    public ServerAction(JsonObject jsonObject) {
        if(jsonObject.has("name")
                && jsonObject.has("action")){
            this.name = jsonObject.get("name").getAsString();
            this.action = Action.valueOf(jsonObject.get("action").getAsString());
            correct = true;
        }else
            Logs.warn("Parsing error");
    }

    public ServerAction(String name, Action action) {
        this.name = name;
        this.action = action;
    }

    public String getName() {
        return name;
    }

    public Action getAction() {
        return action;
    }

    public JsonObject toJson(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", getName());
        jsonObject.addProperty("action", getAction().name());
        return jsonObject;
    }

    public boolean isCorrect() {
        return correct;
    }

    public enum Action{
        FORCED_STOP, STOP, COMMAND;
    }

}
