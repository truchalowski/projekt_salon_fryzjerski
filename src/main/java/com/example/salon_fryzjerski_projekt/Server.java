package com.example.salon_fryzjerski_projekt;

import java.net.ServerSocket;
import java.net.Socket;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.*;

public class Server {

    private static  ExecutorService threadPool = Executors.newFixedThreadPool(250);
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12346);

            System.out.println("Serwer nasłuchuje na porcie 12346...");


            while (true) {
                Socket clientSocket = serverSocket.accept();
                threadPool.submit(new ClientHandler(clientSocket));

            }
        }catch(Exception e){
                e.printStackTrace();
        }


    }


}