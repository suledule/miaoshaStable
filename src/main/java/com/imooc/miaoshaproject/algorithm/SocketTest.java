package com.imooc.miaoshaproject.algorithm;

import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketTest {
    public static byte[] socket(String ip,int port,byte[] bytes) throws IOException {
        byte[] result = {};

        Socket socket = new Socket(ip,port);
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(bytes);
        InputStream inputStream = socket.getInputStream();
        byte[] buffer = new byte[1024];
        int readLen = 0;
        while (readLen!=-1){
            readLen=inputStream.read(buffer);
            result = ArrayUtils.addAll(result,ArrayUtils.subarray(buffer,0,readLen));
        }
        outputStream.close();
        inputStream.close();
        socket.close();

        return result;
    }
}
