/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;




/**
 *
 * @author Tobi
 */
public class ClientConnector {
    private final BlockingQueue<String> messageQueue;
    private UDPServer server;
    private UDPClient client;
    private final String chatName;
    
    public ClientConnector(String chatName){
        this.messageQueue = new LinkedBlockingDeque<String>();
        server = new UDPServer(this.messageQueue);
        client = new UDPClient();
        if (chatName.length()>20){
            throw new Error("Chatname ist to long. Max. 20 chars.");
        }
        this.chatName=chatName;
    }
    
    public String getMessage(){
        return this.messageQueue.remove();
    }
    
    public void sendMessage(Map<String, String> clientList, String message){
        if (message.length()>100){
            throw new Error("Message is to long. Max 100 chars.");
        }
        client.sendMessage(clientList, String.format("%s: %s", chatName, message));
    }
}
