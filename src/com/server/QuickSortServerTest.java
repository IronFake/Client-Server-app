package com.server;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class QuickSortServerTest {

    ArrayList<String> NameExpected = new ArrayList<String>();
    ArrayList<String> ExtensionExpected = new ArrayList<String>();

    @Test
    public void separateFullName() {
        Server.FilesOnServer.add("LICENCE.TMP");
        Server.FilesOnServer.add("WIN32.LOG");
        Server.FilesOnServer.add("GSVIEW32.EXE");
        Server.FilesOnServer.add("GSVIEW32.ICO");
        Server.FilesOnServer.add("GSV16SPL.EXE");
        NameExpected.add("LICENCE");
        NameExpected.add("WIN32");
        NameExpected.add("GSVIEW32");
        NameExpected.add("GSVIEW32");
        NameExpected.add("GSV16SPL");
        ExtensionExpected.add("TMP");
        ExtensionExpected.add("LOG");
        ExtensionExpected.add("EXE");
        ExtensionExpected.add("ICO");
        ExtensionExpected.add("EXE");
        QuickSortServer.separateFullName(Server.FilesOnServer, Server.FilesOnServer.size());
        Assert.assertEquals(NameExpected, QuickSortServer.Name);
        Assert.assertEquals(ExtensionExpected, QuickSortServer.Extension);
    }

    @Test
    public void sortByName() {
        ArrayList <String> SortFile = new ArrayList<String>();
        SortFile.add("GSVIEW32.EXE");
        SortFile.add("GSVIEW32.ICO");
        Server.FilesOnServer.add("LICENCE.TMP");
        Server.FilesOnServer.add("WIN32.LOG");
        Server.FilesOnServer.add("GSVIEW32.EXE");
        Server.FilesOnServer.add("GSVIEW32.ICO");
        Server.FilesOnServer.add("GSV16SPL.EXE");
        QuickSortServer.Name.add("LICENCE");
        QuickSortServer.Name.add("WIN32");
        QuickSortServer.Name.add("GSVIEW32");
        QuickSortServer.Name.add("GSVIEW32");
        QuickSortServer.Name.add("GSV16SPL");
        QuickSortServer.SortByName(Server.FilesOnServer);
        Assert.assertEquals(SortFile, Server.FilesOnServer);
    }

    @Test
    public void sortByExtension() {
        ArrayList <String> SortFile = new ArrayList<String>();
        SortFile.add("GSVIEW32.EXE");
        SortFile.add("GSV16SPL.EXE");
        Server.FilesOnServer.add("LICENCE.TMP");
        Server.FilesOnServer.add("WIN32.LOG");
        Server.FilesOnServer.add("GSVIEW32.EXE");
        Server.FilesOnServer.add("GSVIEW32.ICO");
        Server.FilesOnServer.add("GSV16SPL.EXE");
        QuickSortServer.Extension.add("TMP");
        QuickSortServer.Extension.add("LOG");
        QuickSortServer.Extension.add("EXE");
        QuickSortServer.Extension.add("ICO");
        QuickSortServer.Extension.add("EXE");
        QuickSortServer.SortByExtension(Server.FilesOnServer);
        Assert.assertEquals(SortFile, Server.FilesOnServer);
    }
}