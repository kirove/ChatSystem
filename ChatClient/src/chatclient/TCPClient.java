/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

import java.io.*;
import java.net.*;

/**
 *
 * @author Tobi
 */
public class TCPClient {

    public static final int SERVER_PORT = 50000;
    public final String SERVER_HOSTNAME;
    private Socket clientSocket; // TCP-Standard-Socketklasse
    private DataOutputStream outToServer; // Ausgabestream zum Server
    private BufferedReader inFromServer;  // Eingabestream vom Server
    //private boolean serviceRequested = true; // Client beenden?
    //private Scanner inFromUser;
    //private String sendData; // vom User übergebener String
    //private String modifiedSentence; // vom Server modifizierter String

    public TCPClient(String serverHostName) {
        this.SERVER_HOSTNAME = serverHostName;
    }

    public void connect() {

        /* Ab Java 7: try-with-resources mit automat. close benutzen! */
        try {
            /* Socket erzeugen --> Verbindungsaufbau mit dem Server */
            clientSocket = new Socket("localhost", SERVER_PORT);

            /* Socket-Basisstreams durch spezielle Streams filtern */
            inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            outToServer = new DataOutputStream(clientSocket.getOutputStream());
            inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            
        } catch (IOException e) {
            System.err.println(e.toString());
            System.exit(1);
        }
    }

    public void close() {
        try {
            clientSocket.close();
        } catch (IOException ex) {
            System.err.println(ex.toString());
            System.exit(1);
        }
    }

    public void writeToServer(String request) {
        /* Sende den request-String als eine Zeile (mit newline) zum Server */
        try {
            outToServer.writeBytes(request + '\n');
        } catch (IOException e) {
            System.err.println(e.toString());
//            serviceRequested = false;
        }
        System.out.println("TCP Client has sent the message: " + request);
    }

    public String readFromServer() {
        /* Liefere die Antwort (reply) vom Server */
        String reply = "";

        try {
            reply = inFromServer.readLine();
        } catch (IOException e) {
            System.err.println("Connection aborted by server!");
//            serviceRequested = false;
        } catch (Exception e) {
            System.err.println("Something went wrong!");
        }
        System.out.println("TCP Client got from Server: " + reply);
        return reply;
    }
}
