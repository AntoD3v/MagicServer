package fr.antodev.magicserver;

import fr.antodev.magicserver.servers.ServerType;
import fr.antodev.magicserver.services.ProducerService;
import fr.antodev.magicserver.services.AddingServersService;
import fr.antodev.magicserver.support.FileUtil;
import fr.antodev.magicserver.support.Logs;
import fr.antodev.magicserver.support.Options;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MagicServerStarter {

    private final MagicServerStarter magic;
    private File template = new File(Options.TEMPLATE);
    private File hosted = new File(Options.HOSTED);

    public MagicServerStarter() {
        this.magic = this;
        starter();
    }

    private void starter() {
        if(!template.exists()){
            template.mkdirs();

            for(ServerType type : ServerType.values())
                new File(Options.TEMPLATE+type.name()).mkdir();
            Logs.info("Creating the templates folder");
            Logs.info("Please to complete template folder before restart this program !");
            System.exit(1);
        }
        if(hosted.exists())
            FileUtil.deleteDir(hosted);
        hosted.mkdirs();

        Logs.info("Quantity of thread pool fixed on "+Options.MAX_FIXED_THREAD);
        ExecutorService service = Executors.newFixedThreadPool(Options.MAX_FIXED_THREAD);
        for (int i = 0; i < Options.MAX_FIXED_THREAD; i++)
            service.execute(new ProducerService());

        new Thread(new fr.antodev.magicserver.executors.Executors()).start();
        Logs.info("Listening on adding_server channel ...");
        new Thread(new AddingServersService()).start();

    }

    public static void main(String[] args){
        Logs.info(" * MagicServer © 2019");
        new MagicServerStarter();
    }
}
