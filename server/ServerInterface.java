package server;

import client.ClientInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface ServerInterface extends Remote {

    void registerChatClient(ClientInterface client) throws RemoteException;

    void broadcastMessage(String message, int caretPosition,ClientInterface client) throws RemoteException;
}
