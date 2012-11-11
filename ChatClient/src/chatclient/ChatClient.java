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

    private static MJFrame frame;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {

        //frame = new MJFrame();
        ServerConnector sc = new ServerConnector("localhost", "Batman");
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
