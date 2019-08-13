package com.project.utils;

import java.io.IOException;
import java.net.ServerSocket;

public class RandomPortUtil {

    public static int findRandomPort(){
        try {
            ServerSocket serverSocket = new ServerSocket(0);
            return serverSocket.getLocalPort();

        } catch(IOException e){
            throw new RuntimeException(e);
        }
    }
}
