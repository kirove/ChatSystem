/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import java.io.*;
import java.net.*;


/**
 *
 * @author aaz815
 */
public class ServerThread extends Thread {
    /* Arbeitsthread, der eine existierende Socket-Verbindung zur Bearbeitung erh�lt */


    private ChatServer chatServer;
    private Socket connectionSocket;
    public static boolean serviceRequested = true; // Arbeitsthread beenden?

    public ServerThread(Socket sock,ClientsList clientsList) throws IOException {
        /* Konstruktor */
      this.connectionSocket = sock;
        chatServer = new ChatServer(sock,clientsList);
    }

    public void run() {

        String clientCommand;

        System.out.println("TCP Server Thread is running until BYE is received!");
        this.serviceRequested = true;
        try {

            while (serviceRequested) {
                /* Commands vom Client empfangen */
                clientCommand = chatServer.getCommand();
                System.out.println("TCP Server Thread detected job: "
                        + clientCommand);

                //Command interpretieren und Aktion führen
                chatServer.doJob(clientCommand);

            }
            this.connectionSocket.close();
        } catch (IOException e) {
            System.err.println(e.toString());
            System.exit(1);
        }

        System.out.println("TCP Server Thread with " + chatServer.client_ChatName + " stopped!");
    }
}
