package client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {

    void retriveContent(String message, int caretPosition,ClientInterface client) throws RemoteException;
}
