/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

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
    private DatagramSocket clientSocket;  // UDP-Socketklasse
    private InetAddress serverIpAddress;  // IP-Adresse des Zielservers

    public UDPClient() {
        
    }

    void sendMessage(Map<String, String> clientList, String message) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
