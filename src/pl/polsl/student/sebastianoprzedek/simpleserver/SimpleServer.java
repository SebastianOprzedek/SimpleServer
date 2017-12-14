package pl.polsl.student.sebastianoprzedek.simpleserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Sebastian Oprzędek on 14.12.2017.
 */
public class SimpleServer {
    public static void main(String [] args) throws Exception{
        new SimpleServer().start();
    }

    public void start() throws Exception{

        System.out.println("Aaaa");
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(4444);
        } catch (IOException e) {
            System.out.println("Could not listen on port: 4444.");
            System.exit(1);
        }

        Socket clientSocket = null;
        try {
            clientSocket = serverSocket.accept();
            System.out.println("bbb");
        } catch (IOException e) {
            System.out.println("Accept failed.");
            System.exit(1);
        }

        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        clientSocket.getInputStream()));

        out.print("ok");
        out.close();
        in.close();
        clientSocket.close();
        serverSocket.close();
        System.out.println("cc");

    }
}
