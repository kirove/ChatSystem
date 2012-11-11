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
    public static void main(String[] args) throws InterruptedException {
                
        ServerConnector sc = new ServerConnector("localhost", "iBot");
        sc.start();
        Thread.currentThread().sleep(5000);
        System.out.println(sc.getClientList());
        Thread.currentThread().sleep(5000);
        System.out.println(sc.getClientList());
        Thread.currentThread().sleep(5000);
        System.out.println(sc.getClientList());
        sc.closeConnection();
        
    }
}
