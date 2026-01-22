package ServerWORK;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class User {
    private Socket clientSocket;
    private ObjectOutputStream outStr;
    private ObjectInputStream inpStr;
    private String message;

    public User(String ipAddress, String port){
        try {
            clientSocket = new Socket(ipAddress, Integer.parseInt(port));
            outStr = new ObjectOutputStream(clientSocket.getOutputStream());
            inpStr = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            System.out.println("Сервер не найден: " + e.getMessage());
            System.exit(0);
        }
    }

    public void sendObject(Object object){
        try {
            outStr.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message){
        try {
            outStr.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readMessage() throws IOException {
        try {
            message = (String) inpStr.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        return message;
    }

    public Object readObject(){
        Object object = new Object();
        try {
            object = inpStr.readObject();
        } catch (ClassNotFoundException | IOException e) {

            e.printStackTrace();
        }
        return object;
    }
}
