package fr.antodev.magicserver.support;

import fr.antodev.magicserver.servers.Server;
import fr.antodev.magicserver.servers.ServerType;

import java.io.File;

public class ServerTest {

    //{"name":"SKW-001","type":"HUB","max-players":99}

    public static void main(String[] args){
        Logs.info(new Server("SKW-001", ServerType.HUB, "localhost", 99, 99).toJson().toString());
    }

}

