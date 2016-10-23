package com.proxy;

import java.io.*;
import java.net.*;

// internet server <--serverSocket--> proxy <--clientSocket--> client                                                                                                   

public class ProxyServer {
    public static void main(String[] args) {
        try {
            new Thread(new SimpleProxyServer(8989)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class SimpleProxyServer implements Runnable {
    private ServerSocket listenSocket;
    public SimpleProxyServer(int port) throws IOException {
        this.listenSocket = new ServerSocket(port);
    }

    public void run() {
        while(true) {
            try {
                Socket clientSocket = listenSocket.accept();
                System.out.println("Create a new Thread to handle this connection");
                new Thread(new ConnectionHandler(clientSocket)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


class ProxyCounter {
    static int sendLen = 0;
    static int recvLen = 0;

    public static void showStatistics() {
        System.out.println("sendLen = " + sendLen);
        System.out.println("recvLen = " + recvLen);
    }
}

// must close sockets after a transaction                                                                                                                               
class ConnectionHandler extends ProxyCounter implements Runnable {
    private Socket clientSocket;
    private Socket serverSocket;

    private static final int bufferlen = 8192;

    public ConnectionHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
    public void run() {
        // receive request from clientSocket,                                                                                                                           
        //extract hostname,                                                                                                                                             
        //create a serverSocket to communicate with the host                                                                                                            
        // count the bytes sent and received                                                                                                                            
        try {
            byte[] buffer = new byte[bufferlen];
            int count = 0;

            InputStream inFromClient = clientSocket.getInputStream();
            count = inFromClient.read(buffer);
            String request = new String(buffer, 0, count);
            String[] host = extractHost(request).split(":");
            // create serverSocket               
            
         
			serverSocket = new Socket(host[0], Integer.parseInt(host[1]));
            // forward request to internet host                                                                                                                         
            OutputStream outToHost = serverSocket.getOutputStream();
            outToHost.write(buffer, 0, count);
            outToHost.flush();
            sendLen += count;
            showStatistics();
            // forward response from internet host to client                                                                                                            
            InputStream inFromHost = serverSocket.getInputStream();
            OutputStream outToClient = clientSocket.getOutputStream();
            while (true) {
                count = inFromHost.read(buffer);
                if (count < 0)
                    break;
                outToClient.write(buffer, 0, count);
                outToClient.flush();
                recvLen += count;
                showStatistics();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String extractHost(String request) {
        int start = request.indexOf("Host: ") + 6;
        int end = request.indexOf('\n', start);
        String host = request.substring(start, end - 1);
        return host;
    }
}