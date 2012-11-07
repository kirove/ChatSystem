/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author Tobi
 */
public class ServerConnector extends Thread {

    private TCPClient connectionToServer;
    private final String username;
    private Map<String, String> clientList;
    private STAT status = STAT.LOGIN;
    private static String OK = "OK";
    private static String ERROR = "ERROR ";
    private static String LIST = "LIST ";
    private static String BYE = "BYE";
    private static String NEW = "NEW ";
    private static String INFO = "INFO ";
   
    public enum STAT {LOGIN, UPDATE, EXIT};
    

    //Konstruktor
    public ServerConnector(String hostname, String username) {
        this.connectionToServer = new TCPClient(hostname);
        this.username = username;
        this.clientList = new HashMap<>(); //Hostname -> Chatname
    }

    @Override
    public void run() {
        connectionToServer.connect();
        if (login()) {
            System.out.println("Verbindung steht.");
        } else {
            System.out.println("Verbindung stehtn nicht.");
        }
        while (status == STAT.UPDATE) {
            updateClientList();
            sleepUntilNextUpdate();
        }
        logout();
        connectionToServer.close();
    }

    //Anmeldung am Server
    private boolean login() {
        boolean erfolgreich = false;
        connectionToServer.writeToServer(NEW + username);
        String antwort = connectionToServer.readFromServer();
        if (antwort.toUpperCase().equals(OK)) {
            erfolgreich = true;
            status = STAT.UPDATE;
            System.out.println(String.format("Anmeldung als %s war erfolgreich.", username));
        } else if (antwort.toUpperCase().startsWith(ERROR)) {
            System.out.println(String.format("Anmeldung als %s ist fehlgeschlagen:\n\t%s", username, antwort));
        } else {
            System.out.println(String.format("Ein unerwarteter Fehler ist aufgetreten:\n\t%s", username, antwort));
        }
        return erfolgreich;
    }

    //Thread für einige Sekunden Schlafenlegen, bevor das nächste Update durchgeführt werden soll
    private void sleepUntilNextUpdate() {
        try {
            Thread.currentThread().sleep(2500);
        } catch (InterruptedException ex) {
            sleepUntilNextUpdate();
        }
    }

    //INFO-Befehl an den Server senden, um 
    private boolean updateClientList() {
        boolean erfolgreich = false;
        connectionToServer.writeToServer(INFO);
        String antwort = connectionToServer.readFromServer();
        if (antwort.toUpperCase().startsWith(LIST)) {
            if (analyseListStringandSetClientList(antwort)) {
                System.out.println("Client-Liste upgedatet.");
            } else {
                System.out.println("Update der Client-Liste fehlgeschlagen.");
            }
        } else if (antwort.toUpperCase().startsWith(ERROR)) {
            closeConnection();
            System.out.println(String.format("LIST-Befehl fehlgeschlagen:\n\t%s", antwort));
        } else {
            closeConnection();
            System.out.println(String.format("Ein unerwarteter Fehler ist aufgetreten:\n\t%s", antwort));
        }
        return erfolgreich;
    }

    private boolean analyseListStringandSetClientList(String listString) {
        boolean erfolgreich = true;
        String[] splittedClientList = listString.split("\\s");
        Map<String, String> clients = new HashMap<>();
        try {
            int n = Integer.parseInt(splittedClientList[1]);

            for (int i = 2; i < splittedClientList.length; i += 2) {
                clients.put(splittedClientList[i], splittedClientList[i + 1]);
            }

            if (clients.size() != n) {
                System.err.println(String.format("LIST-String: Falsche Anzahl von Host-/Chat-Name Pärchen. Erwartet: %d, Tatsächlich: %d", n, clients.size()));
                erfolgreich = false;
            } else {
                this.clientList = clients;
            }
        } catch (NumberFormatException nfe) {
            System.err.println("LIST-String: Angabe von <n> ist kein Integer-Wert.");
            erfolgreich = false;
        } catch (IndexOutOfBoundsException iobe) {
            System.err.println("LIST-String: Falsche Anzahl von Parametern.");
            erfolgreich = false;
        }
        return erfolgreich;
    }

    private boolean logout() {
        boolean erfolgreich = false;
        
        connectionToServer.writeToServer(BYE);
        String antwort = connectionToServer.readFromServer();
        if (antwort.toUpperCase().equals(BYE)) {
            
                System.out.println("Server schließt Verbindung.");
            
        } else if (antwort.toUpperCase().startsWith(ERROR)) {
            closeConnection();
            System.out.println(String.format("BYE-Befehl fehlgeschlagen:\n\t%s", antwort));
        } else {
            closeConnection();
            System.out.println(String.format("Ein unerwarteter Fehler ist aufgetreten:\n\t%s", antwort));
        }
        
        return erfolgreich;
    }
    
    public synchronized Map<String, String> getClientList(){
        return (status != STAT.LOGIN)? new HashMap<>(clientList) : null;
    }
    
    public synchronized void closeConnection(){
        status = STAT.EXIT;
    }
    
    public STAT getStatus(){
        return status;
    }
}
