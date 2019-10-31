package com.server;

import java.util.ArrayList;

public class QuickSortServer {

    static ArrayList<String> Name = new ArrayList<String>();
    static ArrayList<String> Extension = new ArrayList<String>();

    public synchronized static void separateFullName(ArrayList<String> FullNames, int NumberOfFiles) {
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
    }

    // Сортировка по Имени файла
    public synchronized static void SortByName(ArrayList<String> FullNames) {
        ArrayList<Integer> coincidence = new ArrayList<Integer>();
        for (int i = 0; i < FullNames.size(); i++) {
            int numberOfCoincidences = 0; // число совпадений
            for (int k = 0; k < FullNames.size(); k++) {
                String name = Name.get(i);
                String name2 = Name.get(k);
                if (i != k && name.equals(name2)) {
                    numberOfCoincidences++;
                }
            }
            // Если совпадений ноль, значит файл уникальный
            if (numberOfCoincidences == 0) {
                coincidence.add(i);
            }
        }
        // Удаляем все уникальные файлы
        for (int i = coincidence.size(); i > 0; i--) {
            int number = coincidence.get(i - 1);
            Server.FilesOnServer.remove(number);
        }
    }

    // Сортировка по расширению файла
    public synchronized static void SortByExtension(ArrayList<String> FullNames) {
        ArrayList<Integer> coincidence = new ArrayList<Integer>();
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
            // Если совпадений ноль, значит файл уникальный
            if (numberOfCoincidences == 0) {
                coincidence.add(i);
            }
        }
        // Удаляем все уникальные файлы
        for (int i = coincidence.size(); i > 0; i--) {
            int number = coincidence.get(i - 1);
            Server.FilesOnServer.remove(number);
        }
    }
}
