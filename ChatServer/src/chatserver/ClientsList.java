package chatserver;

import java.util.*;

/**
 *
 * @author Nidal
 */
public class ClientsList {

    public Map<String, String> clientsList;

    public ClientsList() {
        this.clientsList = new HashMap<>();
    }

    public Map<String, String> getList() {
        return new HashMap(this.clientsList);
    }

    public synchronized void addClient(String chatName, String hostName) {
        this.clientsList.put(chatName, hostName);
    }

    public synchronized void removeClient(String chatname) {
        clientsList.remove(chatname);
    }

    public String size() {
        Integer size = clientsList.size();
        return size.toString();
    }

    public String toString() {
        String result ="";
        for (Map.Entry<String, String> entry : clientsList.entrySet()) {
            result += " " + entry.getValue() + " " + entry.getKey();
        }
        return result;
    }
    public boolean alreadyRegistered(String chatName){
        return clientsList.containsKey(chatName);
    }
}
