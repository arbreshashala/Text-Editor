package client;

import server.ServerInterface;
import java.awt.HeadlessException;
import java.io.IOException;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    public static void main(String[] args) throws RemoteException, NotBoundException {
        try {
            Registry myRegistry = LocateRegistry.getRegistry("localhost", 9010);
            ServerInterface serverInterface = (ServerInterface) myRegistry.lookup("RMI");
            new ClientController(serverInterface).run();
        } catch (NotBoundException | HeadlessException | IOException ex) {
            ex.printStackTrace();
        }
    }
}
