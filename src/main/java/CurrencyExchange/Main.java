package main.java.CurrencyExchange;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws IOException {

        try (var socket = new Socket("google.com", 80);
             DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
             DataInputStream inputStream = new DataInputStream(socket.getInputStream())
        ) {
            outputStream.writeUTF("123");
            byte[] bytes = inputStream.readAllBytes();
            System.out.println(Arrays.toString(bytes));
        }

        try (var serverSocket = new ServerSocket(7777);
             var socket = serverSocket.accept();
             var outputStream = new DataOutputStream(socket.getOutputStream()); //response
             var inputStream = new DataInputStream(socket.getInputStream())) {

        }
    }
}