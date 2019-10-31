package com.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

// реализуем интерфейс Runnable, который позволяет работать с потоками
public class ClientHandler implements Runnable {
    // экземпляр нашего сервера
    private Server server;
    private static final String HOST = "localhost";
    private static final int PORT = 8888;
    // клиентский сокет
    private Socket clientSocket = null;
    // количество клиента в чате, статичное поле
    private static int clients_count = 0;

    private PrintWriter outMessage;  // исходящее сообщение
    private Scanner inMessage;       // входящее собщение

    static int MaxMessage = 3; // Максимальное количество сообщений отправленных клиентами
    static int CurrentMessage = 0; // Текущее количество сообщений отправленных клиентами

    // конструктор, который принимает клиентский сокет и сервер
    public ClientHandler(Socket socket, Server server) {
        try {
            clients_count++;
            this.server = server;
            this.clientSocket = socket;
            this.outMessage = new PrintWriter(socket.getOutputStream());
            this.inMessage = new Scanner(socket.getInputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    // Переопределяем метод run(), который вызывается когда
    // мы вызываем new Thread(client).start();
    @Override
    public void run() {
        try {
            System.out.println("Подлкючился");
            while (true) {
                // сервер отправляет сообщение
                sendMsg(Integer.toString(Server.FilesOnServer.size())); // Отправляем количество файлов на сервере
                for (int i=0; i < Server.FilesOnServer.size(); i++) { // Отправляем сами файлы
                    sendMsg(Server.FilesOnServer.get(i));
                }
                if (clients_count == 1) { // Одному клиенту отправляем имя выбранного файла
                    sendMsg("Клиент знающий имя файла");
                    sendMsg(Server.FileName);
                }
                if (clients_count == 2){ // Второму отправляем расширение выбранного файла
                    sendMsg("Клиент знающий расширение файла");
                    sendMsg(Server.FileExtension);
                }
                break;
            }
            while (true) {
                // Если от клиента пришло сообщение
                if (inMessage.hasNext()) {
                    String clientMessage = inMessage.nextLine();
                    if (CurrentMessage < MaxMessage){
                        // Если клиент не знает имя или расширение
                        if (clientMessage.contains("не знаю")){
                            System.out.println(clientMessage);
                            if (clientMessage.contains("расширение файла")){ // Сортирует и удаляет уникальные расширения
                               QuickSortServer.separateFullName(Server.FilesOnServer, Server.FilesOnServer.size());
                               QuickSortServer.SortByExtension(Server.FilesOnServer);
                            }
                            else if (clientMessage.contains("имя файла")){ // Сортирует и удаляет уникальные имена
                               QuickSortServer.separateFullName(Server.FilesOnServer, Server.FilesOnServer.size());
                               QuickSortServer.SortByName(Server.FilesOnServer);
                            }
                            // Отправляет сообщение другому клиенту
                            server.sendMessageToAllClients(clientMessage, server.clients.indexOf(this)); //
                            CurrentMessage++;
                        }else {
                            // Если один из клиентов знает имя или расширени, то просто выводим на экран
                            System.out.println(clientMessage);
                            break;
                        }
                    } else {
                        // Если клиенты не нашли файл за указанное число сообщений, то выводим на экран файлв-кандидаты
                        System.out.println(Server.FilesOnServer);
                        server.Infile(); // Заносим файлы кандидаты в отдельный текстовый файл
                    }
                }
                // останавливаем выполнение потока на 100 мс
                Thread.sleep(100);
            }
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        finally {
            this.close();
        }
    }

    // отправляем сообщение
    public void sendMsg(String msg) {
        try {
            outMessage.println(msg);
            outMessage.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    // клиент выходит из чата
    public void close() {
        // удаляем клиента из списка
        server.removeClient(this);
        clients_count--;
    }
}