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
    public static void main(String[] args) {

        frame = new MJFrame();
        ServerConnector sc = new ServerConnector("localhost", "iBot");
        sc.start();
        sc.getClientList();
        sc.getClientList();
        sc.getClientList();
        sc.closeConnection();

    }
}
