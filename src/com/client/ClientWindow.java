package com.client;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientWindow extends JFrame {
    // Соединение с сервером
    private static final String SERVER_HOST = "localhost"; // адрес сервера
    private static final int SERVER_PORT = 8888; // порт
    private Socket clientSocket; // клиентский сокет
    private String clientName = ""; // имя клиента
    // получаем имя клиента
    public String getClientName() {
        return this.clientName;
    }

    private Scanner inMessage; // входящее сообщение
    private PrintWriter outMessage; // исходящее сообщение
    private String clientMessage = ""; //написанное сообщение

    static ArrayList<String> Files = new ArrayList<String>(); // Полученные файлы
    static String PartChooseFile; // Часть выбранного файла
    static String FindFile; // Найденный файл
    static int i;

    // конструктор
    public ClientWindow() {
        try {
            // подключаемся к серверу
            clientSocket = new Socket(SERVER_HOST, SERVER_PORT);
            inMessage = new Scanner(clientSocket.getInputStream());
            outMessage = new PrintWriter(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int i = 0;
                    while (true) {
                        // если есть входящее сообщение
                        // считываем его
                        String inMes = inMessage.nextLine();
                        if (i == 0) {
                            ClientWindow.i = Integer.parseInt(inMes); // Число файлов на сервере
                            i++;
                        } else if (i <= ClientWindow.i) {
                            Files.add(inMes); // Запоминаем пришедшие названия файлов
                            i++;
                        } else if (i == (ClientWindow.i + 1)) {
                            System.out.println("Клиент получил все файлы");
                            clientName = inMes; // Получаем имя файла для наглядности
                            System.out.println("Клиент получил имя");
                            i++;
                        } else if (i == (ClientWindow.i + 2)) {
                            PartChooseFile = inMes; // Получаем часть выбранного файла
                            System.out.println("Клиент получил часть выбранного файла: " + PartChooseFile);
                            i++;
                            if (PartChooseFile.length() <= 4) { // Если длина меньше 4, то это расширение
                                QuickSort.NameOrExtension(PartChooseFile);
                                sendMsg();
                            } else {
                                QuickSort.separateFullName(Files, Files.size());
                            }
                        // Если от клиента пришло сообщение что он не знает имя или расширение фалйа, значит файл не уникальный
                        } else if (i == (ClientWindow.i + 3)) {
                            if (inMes.contains("не знаю")) {
                                if (PartChooseFile.length() <= 4) {
                                    QuickSort.SortByName(Files);
                                    QuickSort.NameOrExtension(PartChooseFile);
                                    sendMsg();
                                } else {
                                    QuickSort.SortByExtension(Files);
                                    QuickSort.NameOrExtension(PartChooseFile);
                                    sendMsg();
                                }
                            }
                        } else {System.out.println(inMes);}
                    }
                } catch (Exception e) {
                }
            }
        }).start();

    }

    // отправка сообщения
    public void sendMsg() {
        // формируем сообщение для отправки на сервер
        String messageStr;
        if (QuickSort.flag == true){
            messageStr = clientName + ": " + FindFile;
            QuickSort.flag = false;
        }
        else {
            messageStr = clientName + ": " + "Я не знаю полного имени файла";
        }
        // отправляем сообщение
        outMessage.println(messageStr);
        outMessage.flush();
        clientMessage = "";
    }
}