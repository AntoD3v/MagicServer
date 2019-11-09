package fr.antodev.magicserver.support;


import fr.antodev.magicserver.servers.Server;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    public static void copyFile(File sources, File destination) {

        try {
            FileInputStream fis = new FileInputStream(sources);
            FileOutputStream fos = new FileOutputStream(destination);
            int len;
            byte[] buffer = new byte[4096];
            while ((len = fis.read(buffer)) > 0)
                fos.write(buffer, 0, len);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void copyDir(File sources, File destination) {
        if(!destination.exists()) destination.mkdir();
        for (File file : sources.listFiles()){
            if(file.isDirectory()){
                copyDir(file, new File(destination.getPath()+"/"+file.getName()));
                continue;
            }
            copyFile(file, new File(destination.getPath()+"/"+file.getName()));
        }
    }

    public static void deleteFile(File sources) {
        sources.delete();
        if(sources.exists())
            Logs.warn("Cannot delete "+sources.getPath());
    }

    public static void deleteDir(File sources) {
        for (File file : sources.listFiles()){
            if(file.isDirectory())
                deleteDir(file);
            deleteFile(file);
        }
    }

    public static void createRapidFile(File file, List<String> contenu){
        try {
            file.createNewFile();
            BufferedOutputStream ouput = new BufferedOutputStream(new FileOutputStream(file));
            contenu.forEach(s -> {
                try {
                    ouput.write(s.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            ouput.flush();
            ouput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void editServerProperties(File file, Server server){
        try {
            List<String> list = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null){
                if(line.startsWith("server-ip"))
                    line = "server-ip="+server.getIP();
                else if(line.startsWith("server-port"))
                    line = "server-port="+server.getPort();
                else if(line.startsWith("max-players"))
                    line = "max-players="+server.getMaxPlayers();
                list.add(line);
            }
            reader.close();
            BufferedOutputStream ouput = new BufferedOutputStream(new FileOutputStream(file));
            list.forEach(s -> {
                try {
                    ouput.write((s+"\n").getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            ouput.flush();
            ouput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}