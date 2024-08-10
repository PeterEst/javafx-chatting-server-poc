package com.peterestephan.chattingserver;

import javafx.scene.layout.VBox;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public Server(ServerSocket serverSocket) {
        try {
            this.serverSocket = serverSocket;
            this.socket = serverSocket.accept();
            this.bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        } catch (IOException ex) {
            ex.printStackTrace();
            this.safelyClose();
        }
    }

    public void safelyClose(){
        try {
            if (this.bufferedWriter != null)
                bufferedWriter.close();

            if (this.bufferedReader != null)
                bufferedReader.close();

            if (this.socket != null)
                this.socket.close();
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void sendMessageToClient(String messageToSend) {
        try {
            this.bufferedWriter.write(messageToSend);
            this.bufferedWriter.newLine();
            this.bufferedWriter.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
            this.safelyClose();
        }
    }

    public void receiveMessageFromClient(VBox vBoxMessages){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()){
                    try {
                        String messageFromClient = bufferedReader.readLine();
                        Controller.addLabel(messageFromClient, vBoxMessages, true);
                    } catch (IOException ex){
                        ex.printStackTrace();
                        safelyClose();
                        break;
                    }
                }
            }
        }).start();
    }
}
