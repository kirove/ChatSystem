package chatserver;

import java.io.*;
import java.net.*;

/**
 *
 * @author Nidal
 */
public class TCPServer {

    public final int SERVER_PORT; 
    public ClientsList clientsList;
    
    public TCPServer(int serverPort) {
        this.SERVER_PORT = serverPort;
        this.clientsList = new ClientsList(); 
        
        ServerSocket welcomeSocket;  // TCP-Server-Socketklasse
        Socket connectionSocket;     // TCP-Standard-Socketklasse
                try {
            /* Server-Socket erzeugen */
            welcomeSocket = new ServerSocket(SERVER_PORT);

            while (true) { // Server laufen IMMER
                System.out.println("Warte auf Verbindungswunsch auf Port "
                        + SERVER_PORT);
                /*
                 * Blockiert auf Verbindungsanfrage warten --> nach
                 * Verbindungsaufbau Standard-Socket erzeugen und
                 * connectionSocket zuweisen
                 */
                connectionSocket = welcomeSocket.accept();

                /* Neuen Arbeits-Thread erzeugen und den Socket �bergeben */
                (new ServerThread(connectionSocket,clientsList)).start();
            }
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }
}


