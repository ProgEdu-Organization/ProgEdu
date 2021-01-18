package fcu.selab.progedu.conn;

import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class TomcatServiceTest {

    @Test
    public void deleteDirectory() { // also test createNewDirectory()
        //  No sort out below

//        TomcatService tomcatService = TomcatService.getInstance();
//
//        Path target = Paths.get(System.getProperty("java.io.tmpdir"), "deleteDirectory-for-unit-test");
//
//        File willDeleteDir = target.toFile();
//        tomcatService.createNewDirectory(willDeleteDir);
//        tomcatService.createNewFile(Paths.get(willDeleteDir.getPath(), "file_1").toFile());
//        System.out.println("The new directory is at " + target.toString() + " and it will be deleted.");
//
//        assertTrue(tomcatService.deleteDirectory(willDeleteDir));
    }

    @Test
    public void deleteFileInDirectory() {

        //  No sort out below
//
//        TomcatService tomcatService = TomcatService.getInstance();
//
//        Path target = Paths.get(System.getProperty("java.io.tmpdir"), "deleteFileInDirectory-for-unit-test");
////      If in Windows: System.getProperty("java.io.tmpdir") is C:\\Users\\users\\AppData\\Local\\Temp
//
//        File willDeleteDir = target.toFile();
//        tomcatService.createNewDirectory(willDeleteDir);
//        tomcatService.createNewFile(Paths.get(willDeleteDir.getPath(), "file_1").toFile());
//        System.out.println("The new directory is at " + target.toString() + " and which file in it will be deleted.");
//
//        assertTrue(tomcatService.deleteFileInDirectory(willDeleteDir));

    }

}