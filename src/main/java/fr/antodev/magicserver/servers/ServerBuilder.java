package fr.antodev.magicserver.servers;

import fr.antodev.magicserver.executors.Executors;
import fr.antodev.magicserver.support.FileUtil;
import fr.antodev.magicserver.support.Logs;
import fr.antodev.magicserver.support.Options;

import java.io.File;
import java.net.Socket;
import java.util.Arrays;

public class ServerBuilder {

    private static File workspace;
    private static int iteration = Options.USE_PORT[0];

    public static void build(Server server){
        workspace = new File(Options.HOSTED+"/"+ server.getName()+"/");
        if(!workspace.exists())
            workspace.mkdir();

        server.setPort(availablePort());

        FileUtil.copyDir(new File(Options.TEMPLATE+server.getServerType()), workspace);
        FileUtil.createRapidFile(new File(workspace.getPath()+"/eula.txt"), Arrays.asList("eula=true"));
        FileUtil.editServerProperties(new File(workspace.getPath()+"/server.properties"), server);

        Executors.addQueueStarting(server);
    }

    private static int availablePort(){
        Socket socket = null;
        for(;;){
            iteration++;
            if(iteration > Options.USE_PORT[1])
                iteration = Options.USE_PORT[0];
            try{
                socket = new Socket("localhost", iteration);
            } catch (Exception e) {
                return iteration;
            }
        }
    }


}
