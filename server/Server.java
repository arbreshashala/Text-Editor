package server;

import client.ClientInterface;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Server extends UnicastRemoteObject implements ServerInterface {

    private final ArrayList<ClientInterface> clients;

    protected Server() throws RemoteException {
        clients = new ArrayList<>();
    }

    @Override
    public synchronized void registerChatClient(ClientInterface client) throws RemoteException {
        this.clients.add(client);
    }

    @Override
    public void broadcastMessage(String message, int caretPosition, ClientInterface client) throws RemoteException {
        for (int i = 0; i < clients.size(); i++) {
            clients.get(i).retriveContent(message, caretPosition, client);
        }
    }

    public static void main(String[] args) throws RemoteException, MalformedURLException {
        String name = "RMI";
        try {
            java.rmi.registry.LocateRegistry.createRegistry(9010);
            Registry registry = LocateRegistry.getRegistry(9010);
            registry.rebind(name, new Server());
            System.out.println("Server started");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
