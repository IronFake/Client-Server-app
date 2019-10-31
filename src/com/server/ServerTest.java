package com.server;

import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class ServerTest {

    static ArrayList<String> FilesExpected = new ArrayList<String>();

    @Test
    public void fileManager() {
        FilesExpected.add("LICENCE.TMP");
        FilesExpected.add("WIN32.LOG");
        FilesExpected.add("FILEID.");
        Assert.assertEquals(FilesExpected, Server.FilesOnServer);
    }

    @Test
    public void chooseTheFile() {
        ArrayList<String> FileName = new ArrayList<String>();
        FileName.add("LICENCE");
        FileName.add("WIN32");
        FileName.add("FILEID");
        Server.fileManager(1);
        Server.chooseTheFile();
        if (FileName.get(0) == Server.FileName) {
            Assert.assertEquals(FileName.get(0), Server.FileName);
        } else if (FileName.get(1) == Server.FileName) {
            Assert.assertEquals(FileName.get(0), Server.FileName);
        } else if (FileName.get(2) == Server.FileName) {
            Assert.assertEquals(FileName.get(0), Server.FileName);
        }
    }

    @Test
    public void infile() {
        ArrayList<String> InFileFiles = new ArrayList<String>();
        Server.Infile();
        try {
            FileReader fr = new FileReader("D://OneDrive//Документы//IdeaProjects//Курсовая работа//src//com//server//EndFiles.txt");
            Scanner scannerFiles = new Scanner(fr);
            while (scannerFiles.hasNextLine()) {
                String FullNameFile = scannerFiles.nextLine();
                InFileFiles.add(FullNameFile);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(FilesExpected, InFileFiles);
    }
}