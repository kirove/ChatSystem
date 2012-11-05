/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

/**
 *
 * @author ned
 */
public class ChatClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
                
        ServerConnector sc = new ServerConnector("localhost", "iBot");
        sc.start();
        sc.getClientList();
        sc.getClientList();
        sc.getClientList();
        sc.closeConnection();
        
    }
}
