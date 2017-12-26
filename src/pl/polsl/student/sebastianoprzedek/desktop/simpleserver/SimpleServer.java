package pl.polsl.student.sebastianoprzedek.desktop.simpleserver;

import java.io.IOException;
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
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(4444);
            System.out.println("Server started on port 4444");
        } catch (IOException e) {
            System.out.println("Could not listen on port: 4444.");
            System.exit(1);
        }

        Socket clientSocket = null;
        while(true) {
            try {
                clientSocket = serverSocket.accept();
                System.out.println("Client connected at port " + clientSocket.getPort());
                new ClientThread(clientSocket).start();
            } catch (IOException e) {
                System.out.println("Connection failed.");
                e.printStackTrace();
            }
        }
    }
}
