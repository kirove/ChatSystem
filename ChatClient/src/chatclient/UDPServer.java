/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author Tobi
 */
public class UDPServer {

    private static final int BUFFER_SIZE = 123;
    private final static int SERVER_PORT = 50001;
    private DatagramSocket serverSocket;  // UDP-Socketklasse
    private final BlockingQueue<String> messageQueue;
    private boolean serviceRequested = true;

    public UDPServer(BlockingQueue<String> messageQueue) {
        this.messageQueue = messageQueue;
        startJob();
    }

    private void startJob() {
        try {
            /* UDP-Socket erzeugen (kein Verbindungsaufbau!)
             * Socket wird an irgendeinen freien (Quell-)Port gebunden, da kein Port angegeben */
            this.serverSocket = new DatagramSocket(SERVER_PORT);
            System.out.println("UDP Server: Waiting for connection - listening UDP port " +
                           SERVER_PORT);
            
            String message;

            while (serviceRequested) {
                message = readFromClient();
                this.messageQueue.add(message);
            }

            /* Socket schließen (freigeben)*/
            serverSocket.close();
        } catch (IOException e) {
            System.err.println(e.toString());
            System.exit(1);
        }
        System.out.println("UDP Client stopped!");
    }

    public void stopJob() {
        this.serviceRequested = false;
    }

    private String readFromClient() {
        /* Liefere den nächsten String vom Server */
        String receiveString = "";

        try {
            /* Paket für den Empfang erzeugen */
            byte[] receiveData = new byte[BUFFER_SIZE];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, BUFFER_SIZE);

            /* Warte auf Empfang des Antwort-Pakets auf dem eigenen Port */
            serverSocket.receive(receivePacket);

            /* Paket wurde empfangen --> auspacken und Inhalt anzeigen */
            receiveString = new String(receivePacket.getData(), 0, receivePacket.getLength());
        } catch (IOException e) {
            System.err.println("Connection aborted by client!");
            serviceRequested = false;
        }
        System.out.println("UDP Client got from Client: " + receiveString);
        return receiveString;
    }
}
