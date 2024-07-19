package main.java.CurrencyExchange;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class SocketRunner {
    public static void main(String[] args) throws IOException {

        var inetAddresses = Inet4Address.getByName("localhost");
        try (var socket = new Socket(inetAddresses, 7777);
             DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
             DataInputStream inputStream = new DataInputStream(socket.getInputStream());
             var scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                var request = scanner.nextLine();
                outputStream.writeUTF(request);
                System.out.println("Response from server: " + request);
            }
//
//            outputStream.writeUTF("123");
//            byte[] bytes = inputStream.readAllBytes();
//            System.out.println(Arrays.toString(bytes));
        }
    }
}
