package client;

import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import server.ServerInterface;

public class ClientController extends UnicastRemoteObject implements ClientInterface {

    private static ServerInterface server;
    private static TextArea text;

    protected ClientController(ServerInterface server) throws IOException {
        ClientController.server = server;
        server.registerChatClient(this);
    }

    @Override
    public void retriveContent(String message, int caretPosition, ClientInterface client) throws RemoteException {
        if (client.hashCode() != this.hashCode()) {
            text.setText(message);
            //text.setCaretPosition(caretPosition);
        }
    }

    //@Override
    public void run() {
        new TextEditor(this);

    }

    public static class TextEditor extends JFrame implements ActionListener {

        private final MenuBar menu;
        private final Menu fileMenu;
        private final FileDialog fileDialog;
        private String fileName;

        /*inicializimi i menu's*/
        public TextEditor(ClientInterface c) {
            super("Simple Editor");
            menu = new MenuBar();
            setMenuBar(menu);
            fileMenu = new Menu("File");
            menu.add(fileMenu);
            fileMenu.add(new MenuItem("New"));
            fileMenu.add(new MenuItem("Open"));
            fileMenu.add(new MenuItem("Save"));
            fileMenu.add(new MenuItem("Save as..."));
            fileMenu.add(new MenuItem("Exit"));
            fileMenu.addActionListener(this);
            fileDialog = new FileDialog(this);
            text = new TextArea("", 20, 70, TextArea.SCROLLBARS_BOTH);
            text.setEditable(true);
            text.setFont(new Font("Courier", Font.PLAIN, 12));
            String buffer = "";
            text.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                    try {
                        String t = "" + e.getKeyChar();
                        server.broadcastMessage(text.getText() + t, 0, c);
                    } catch (RemoteException ex) {
                        Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    //throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    //throw new UnsupportedOperationException("Not supported yet."); 
                }
            });
            fileName = null;
            add(text);
            setVisible(true);
            pack();
        }

        /*actionPerformed i funksionalizon opcionet e menu's*/
        @Override
        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();
            switch (cmd) {
                case "Exit":
                    this.dispose();
                    System.exit(0);
                case "Open":
                    fileDialog.show();
                    fileName = fileDialog.getDirectory() + fileDialog.getFile();
                    displayFile();
                    break;
                case "Save":
                    if (fileName == null) {
                        fileDialog.show();
                        fileName = fileDialog.getDirectory() + fileDialog.getFile();
                    }
                    writeFile();
                    break;
                case "Save as...":
                    fileDialog.show();
                    fileName = fileDialog.getDirectory() + fileDialog.getFile();
                    writeFile();
                    break;
                case "New":
                    text.setText("");
                    fileName = null;
                    break;
                default:
                    break;
            }
        }

        /*writeFile metod e cila mundeson shkrimin ne TextArea per nje file specifik*/
        private void writeFile() {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
                String data = text.getText();
                bw.write(data);
            } catch (IOException ioe) {
                System.out.println("Exception " + ioe);
            }
        }

        /*displayFile metod e cila mundeson leximin dhe shfaqjen e file'it ne TextArea*/
        private void displayFile() {
            String line;
            text.setText(""); // Me kthy TextArea ne string te zbraset
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                line = br.readLine();
                while (line != null) { // Perderisa kemi text valid
                    text.append(line + '\n'); // shto rreshtin ne TextArea 
                    line = br.readLine(); // dhe merr rreshtin e rradhes
                }
            } catch (IOException ioe) {
                System.out.println("Exception " + ioe);
            }
        }
    }

}
