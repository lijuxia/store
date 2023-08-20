package org.ljx;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.stream.Collectors;

/**
 * Created by ljx on 2018/6/4.
 */
public class GreetingServer{

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(9999);
            System.out.println("开启9999端口监听");
            int i=0;
            while (true){
                System.out.println("等待客户端连接");
                Socket client = serverSocket.accept();
                System.out.println("客户端连接成功");
                new Thread(new Client(client)).start();
                i++;
                System.out.println(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class Client implements Runnable{
        private Socket client;
        public Client(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            try {
                System.out.println("进去客户端数据解析方法");
                System.out.println("解析完成");
                //读取客户端发送的消息
                InputStream in = client.getInputStream();
                int len =0 ;
                byte[] bytes = new byte[17];
                int i=0;
                while((len = in.read(bytes)) != -1){
                    System.out.println("["+len+"]["+client.getPort()+"]"+new String(bytes));
                    i++;
                    OutputStream out=client.getOutputStream();
                    out.write(bytes);
                }
//                System.out.println("数据接收完成");
                //向客户端发送结果消息
                OutputStream out=client.getOutputStream();
                out.write("接收成功！".getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
