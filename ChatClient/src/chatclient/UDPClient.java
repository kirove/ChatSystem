/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Map;

/**
 *
 * @author Tobi
 */
public class UDPClient {

    public final int SERVER_PORT = 50001;
    public static final int BUFFER_SIZE = 123;

    public UDPClient() {
    }

    void sendMessage(Map<String, String> clientList, String message) {
        DatagramSocket clientSocket;  // UDP-Socketklasse
        InetAddress serverIpAddress;  // IP-Adresse des Zielservers

        try {

            for (String hostname : clientList.values()) {
                /* UDP-Socket erzeugen (kein Verbindungsaufbau!)
                 * Socket wird an irgendeinen freien (Quell-)Port gebunden, da kein Port angegeben */
                clientSocket = new DatagramSocket();
                serverIpAddress = InetAddress.getByName(hostname); // Zieladresse


                System.out.println("ENTER UDP-DATA: ");

                /* Sende den String als UDP-Paket zum Server */
                writeToServer(serverIpAddress, clientSocket, (message + "\n"));


                clientSocket.close();
            }


            /* Socket schließen (freigeben)*/

        } catch (IOException e) {
            System.err.println(e.toString());
            System.exit(1);
        }
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void writeToServer(InetAddress serverIpAddress, DatagramSocket clientSocket, String sendString) {
        /* Sende den String als UDP-Paket zum Server */
        try {
            /* String in Byte-Array umwandeln */
            byte[] sendData = sendString.getBytes();
            /* Paket erzeugen */
            DatagramPacket sendPacket =
                    new DatagramPacket(
                    sendData,
                    sendData.length,
                    serverIpAddress,
                    SERVER_PORT);
            /* Senden des Pakets */
            clientSocket.send(sendPacket);
        } catch (IOException e) {
            System.err.println(e.toString());
        }
        System.out.println("UDP Client has sent the message: " + sendString);
    }
}
