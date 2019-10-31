package com.client;

import java.util.ArrayList;

public class QuickSort {

    static ArrayList<String> Name = new ArrayList<String>();
    static ArrayList<String> Extension = new ArrayList<String>();

    static boolean flag;

    public static void NameOrExtension(String PartChooseFile) {
        if (PartChooseFile.length() <= 4) {
            separateFullName(ClientWindow.Files, ClientWindow.Files.size());
            flag = true;
            SortByExtension(ClientWindow.Files);
        } else {
            separateFullName(ClientWindow.Files, ClientWindow.Files.size());
            flag = true;
            SortByName(ClientWindow.Files);
        }

    }

    public static void separateFullName(ArrayList<String> FullNames, int NumberOfFiles) {
        Name.clear();
        Extension.clear();
        for (int i = 0; i < NumberOfFiles; i++) {
            String fullname = FullNames.get(i);
            String[] substr = fullname.split("[.\\n\\r]");
            if (substr.length == 1) {
                String[] substr2 = new String[2];
                substr2[0] = substr[0];
                substr2[1] = null;
                Name.add(substr2[0]);
                Extension.add(substr2[1]);
            } else {
                Name.add(substr[0]);
                Extension.add(substr[1]);
            }
        }
        System.out.println("Отделенные имена: " + Name);
        System.out.println("Отделенные расширения: " +Extension);
    }

    public static void SortByName(ArrayList<String> FullNames) {
        if (flag == true) {
            int numberOfCoincidence = 0;
            int num = 0;
            for (int i = 0; i < FullNames.size(); i++) {
                String name = Name.get(i);
                if (name == null && ClientWindow.PartChooseFile == null) {
                    numberOfCoincidence++;
                    num = i;
                }
                if (name != null && name.equals(ClientWindow.PartChooseFile)) {
                    numberOfCoincidence++;
                    num = i;
                }
            }
            if (numberOfCoincidence == 1) {
                ClientWindow.FindFile = ClientWindow.Files.get(num);
            } else flag = false;
        } else {
            ArrayList<Integer> coincidence = new ArrayList<Integer>();
            for (int i = 0; i < FullNames.size(); i++) {
                int numberOfCoincidences = 0;
                for (int k = 0; k < FullNames.size(); k++) {
                    String name = Name.get(i);
                    String name2 = Name.get(k);
                    if (i != k && name.equals(name2)) {
                        numberOfCoincidences++;
                    }
                }
                if (numberOfCoincidences == 0) {
                    coincidence.add(i);
                }
            }
            for (int i = coincidence.size(); i > 0; i--) {
                int number = coincidence.get(i - 1);
                ClientWindow.Files.remove(number);
            }
        }
    }

    public static void SortByExtension(ArrayList<String> FullNames) {
        ArrayList<Integer> coincidence = new ArrayList<Integer>();
        if (flag == true) {
            int numberOfCoincidence = 0;
            int num = 0;
            for (int i = 0; i < FullNames.size(); i++) {
                String name = Extension.get(i);
                if (name ==  null && ClientWindow.PartChooseFile == null) {
                    numberOfCoincidence++;
                    num = i;
                }
                if (name != null && name.equals(ClientWindow.PartChooseFile)) {
                    numberOfCoincidence++;
                    num = i;
                }
            }
            if (numberOfCoincidence == 1) {
                ClientWindow.FindFile = ClientWindow.Files.get(num);
            }
            else flag = false;
        } else {
            for (int i = 0; i < FullNames.size(); i++) {
                int numberOfCoincidences = 0;
                for (int k = 0; k < FullNames.size(); k++) {
                    String name = Extension.get(i);
                    String name2 = Extension.get(k);
                    if (i != k && (name == null && name2 == null)) {
                        numberOfCoincidences++;
                    } else if (i != k && name != null && name.equals(name2)) {
                        numberOfCoincidences++;
                    }
                }
                if (numberOfCoincidences == 0) {coincidence.add(i);}
            }
            for (int i = coincidence.size(); i > 0; i--) {
                int number = coincidence.get(i - 1);
                ClientWindow.Files.remove(number);
            }
        }
    }
}