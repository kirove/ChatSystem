/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

/**
 *
 * @author Tobi
 */



public class ServerConnector extends Thread{
    private TCPClient connectionToServer;
    
    public ServerConnector(String hostname){
        connectionToServer = new TCPClient(hostname);
    }
    
    @Override
    public void run(){
        boolean serviceRequested = true;
        
        connectionToServer.connect();
        
        while (serviceRequested){
            connectionToServer.writeToServer("BYE");
        }
        
        connectionToServer.close();
    }
}
