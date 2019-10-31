package com.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Server {
    // порт, который будет прослушивать наш сервер
    static final int PORT = 8888;
    // список клиентов, которые будут подключаться к серверу
    public static ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();
    // Вводные значения вносятся в ArrayList
    static ArrayList<String> FilesOnServer = new ArrayList<String>();
    static String ChooseFile; // Файл выбранный сервером
    static String FileName; // Имя выбранного файла
    static String FileExtension; // Расширение выбранного файла

    public ArrayList getFilesOnServer() {
        return FilesOnServer;
    }

    public Server() {
        // сокет клиента, это некий поток, который будет подключаться к серверу
        // по адресу и порту
        Socket clientSocket = null;
        // серверный сокет
        ServerSocket serverSocket = null;
        selectInputMethod();
        chooseTheFile();
        System.out.println("Файлы на сервера: " + FilesOnServer);
        System.out.println("Выбранный файл: " + ChooseFile);
        System.out.println("Имя выбранного файла: " + FileName);
        System.out.println("Расширение выбранного файла: " + FileExtension);
        try {
            // создаём серверный сокет на определенном порту
            serverSocket = new ServerSocket(PORT);
            System.out.println("Сервер запущен!");
            // запускаем бесконечный цикл
            while (true) {
                // таким образом ждём подключений от сервера
                clientSocket = serverSocket.accept();
                // создаём обработчик клиента, который подключился к серверу
                // this - это наш сервер
                ClientHandler client = new ClientHandler(clientSocket, this);
                clients.add(client);
                // каждое подключение клиента обрабатываем в новом потоке
                new Thread(client).start();
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        finally {
            try {
                // закрываем подключение
                clientSocket.close();
                System.out.println("Сервер остановлен");
                serverSocket.close();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    // Выбирает способ ввода входных данных
    private void selectInputMethod() {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("Выберите способ ввода входных значений: 1 - Файл; 2 - Консоль");
            int SelectMethod = scanner.nextInt(); // считываем ввод
            if (SelectMethod == 1) {fileManager(1); break;} // 1 - из файла
            else if (SelectMethod == 2) {fileManager(2); break;} // 2 - консольное заполнение
            else System.out.println("Вы ввели неправильное число");
        }
        scanner.close();
    }
    // Запись файлов в список
    public static void fileManager(int SelectMethod) {
        switch (SelectMethod){
            case (1): // наименования файлов берется из текстового файла
                try {
                    FileReader fr = new FileReader("src/com/server/StartFiles.txt");
                    Scanner scannerFile = new Scanner(fr);
                    while (scannerFile.hasNextLine()){
                        String FullNameFile = scannerFile.nextLine();
                        FilesOnServer.add(FullNameFile);
                    }
                    scannerFile.close();
                    fr.close();
                } catch (FileNotFoundException e) {e.printStackTrace();}
                  catch (IOException e) {e.printStackTrace();}
                break;
            case (2): // наименование файлов вводится в консоль
                Scanner scannerFile = new Scanner(System.in);
                while (scannerFile.hasNextLine()){
                    String FullNameFile = scannerFile.nextLine();
                    if (FullNameFile.equals("")) break;
                    else FilesOnServer.add(FullNameFile);
                }
                scannerFile.close();
                break;
        }
    }
    // Выбирает файл который нужно найти + сразу же делит его на ИМЯ файла и РАСШИРЕНИЕ файла
    public static void chooseTheFile() {
        Random random = new Random();
        int num = random.nextInt(FilesOnServer.size());
        ChooseFile = FilesOnServer.get(num); // рандомано выбираем файл
        String[] substr = ChooseFile.split("[.\\n\\r]");
        if (substr.length == 1) { // это если расширение равно null и не возникало ошибки
            String[] substr2 = new String[2];
            substr2[0] = substr[0];
            substr2[1] = null;
            FileName = (substr2[0]);
            FileExtension = (substr2[1]);
        } else {
            FileName = (substr[0]);
            FileExtension = (substr[1]);
        }
    }

    public static void Infile(){
        try {
            FileWriter fw = new FileWriter("src/com/server/EndFiles.txt");
            for (int i = 0; i < FilesOnServer.size(); i++){
                fw.write(FilesOnServer.get(i) + "\n");
            }
            fw.close(); // Закрывает поток
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // отправляем сообщение всем клиентам
    public static void sendMessageToAllClients(String msg, int i) {
        for (ClientHandler o : clients) {
            if (clients.indexOf(o) != i){
                o.sendMsg(msg);
            }
        }

    }

    // удаляем клиента из коллекции при выходе из чата
    public static void removeClient(ClientHandler client) {
        clients.remove(client);
    }
}