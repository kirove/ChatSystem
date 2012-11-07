package chatserver;

import java.io.*;
import java.net.*;

/**
 *
 * @author NED
 */
public class ChatServer {

    private Socket socket;
    BufferedReader inFromClient;
    DataOutputStream outToClient;
    //Client Commands
    private final String _NEW = "NEW";
    private final String _INFO = "INFO";
    public final String _BYECLIENT = "BYE";
    //Server Commands
    private final String _OK = "OK\n";
    private final String _ERROR = "ERR";
    private final String _LIST = "LIST";
    public final String _BYESERVER = "BYE";
    public String client_ChatName;
    private String client_Hostname;
    private ClientsList clientsList;

    ChatServer(Socket sock, ClientsList clientsList) throws IOException {
        this.socket = sock;
        this.clientsList = clientsList;


        /* Socket-Basisstreams durch spezielle Streams filtern */
        inFromClient = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        outToClient = new DataOutputStream(socket.getOutputStream());
    }

    //Gets the reply from the server
    public String getCommand() throws IOException {

        String line;

        inFromClient = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));

        line = inFromClient.readLine();

        return line;
    }

    void doJob(String clientCommand) throws IOException {

        //NEW <chatname> Der Client möchte sich unter dem angegeben Chat‐Namen anmelden. Der Chat‐Name darf keine
        //Sonderzeichen und Leerzeichen enthalten!
        if (clientCommand.startsWith(_NEW) || clientCommand.startsWith(_NEW.toLowerCase())) {
            boolean registered = false;
            //register User in ClientsList
            registered = anmelden(clientCommand.substring(4));

            //Der Server hat den übergebenen Chat‐Namen für den Client‐Hostnamen in die Teilnehmerliste eingetragen.
            //Die Client‐Anmeldung ist somit korrekt erfolgt.
            if (registered) {
                outToClient.writeBytes(_OK);
            }

        } else //Der Client fordert die aktuelle Liste aller aktiven Teilnehmer an.
        if (clientCommand.equalsIgnoreCase(_INFO)) {
            //todo LIST <n> Hostname-1 chatname-1 ... Hostname-n chatname-n
            //Die Liste aller aktiven Teilnehmer (Clients) wird zurückgeliefert. Der erste Parameter n ist eine Zahl,
            //die angibt, wieviele Paare (Hostname chatname) folgen.

            sendClientsList();
        } else //Der Client meldet sich ab.
        if (clientCommand.equalsIgnoreCase(_BYECLIENT)) {
            //Logout User
            outToClient.writeBytes(_BYESERVER);
            //Delete user
            deleteClient(client_ChatName);
            //Terminate connection
            socket.close();
            //Terminate Thread
            ServerThread.serviceRequested = false;
        } else {
            //Die letzte Anfrage konnte aus dem mitgelieferten Grund nicht verarbeitet werden. Falls der Client
            //angemeldet war, wird er sofort abgemeldet. Die TCP‐Verbindung wird in jedem Fall vom Server geschlossen!
            outToClient.writeBytes(_ERROR + " <Unknown Command, " + clientCommand + "\nTerminating Connection!>");
            deleteClient(client_ChatName);
            //Thread Beenden
            ServerThread.serviceRequested = false;
        }
    }

    // LIST <n> Hostname-1 chatname-1 ... Hostname-n chatname-n
    //Die Liste aller aktiven Teilnehmer (Clients) wird zurückgeliefert. Der erste Parameter n ist eine Zahl,
    //die angibt, wieviele Paare (Hostname chatname) folgen.
    private void sendClientsList() throws IOException {
        outToClient.writeBytes(_LIST + " " + clientsList.size() + clientsList.toString() + "\n");

    }

    private void deleteClient(String chatName) {
        clientsList.removeClient(chatName);
    }

    private boolean anmelden(String client_ChatName) throws IOException {
        boolean registered = false;
        if (client_ChatName.matches("[0-9a-zA-Z]+")) {
            if (!clientsList.alreadyRegistered(client_ChatName)) {
                this.client_ChatName = client_ChatName;
                this.client_Hostname = socket.getInetAddress().getCanonicalHostName();

                clientsList.addClient(this.client_ChatName, this.client_Hostname);
                registered = true;

            } else {
                outToClient.writeBytes("Already registered!!");
            }
        } else {
            outToClient.writeBytes("Sonder Zeichen nicht erlaubt!!");
        }
        return registered;
    }
}
