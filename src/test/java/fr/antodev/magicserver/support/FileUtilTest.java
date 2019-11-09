package fr.antodev.magicserver.support;

import java.io.File;

class FileUtilTest {
    public static void main(String[] args){
        FileUtil.copyDir(new File("/servers/"), new File("/ser"));
    }
}