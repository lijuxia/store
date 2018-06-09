package org.ljx;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by ljx on 2018/6/4.
 */
public class GreetingClient {

    public static void main(String[] args) {
//        for(int i=0;i<10000;i++){
            new Thread(new SendThread(1)).start();
//        }
    }

    static class SendThread implements Runnable{
        private int num;
        public SendThread(int num){
            this.num = num;
        }
        @Override
        public void run() {
            try {
                Socket socket = new Socket("127.0.0.1",9999);
                OutputStream out = socket.getOutputStream();
                InputStream in = socket.getInputStream();
                int i=0;
                long time = 0;
                for (int y=0;y<20;y++){
                    long begin = System.currentTimeMillis();
                    out.write("test message! by123123e".getBytes());
                    out.flush();
                    int len =0 ;
                    byte[] bytes = new byte[0];
                    len = in.read(bytes);
                    long end = System.currentTimeMillis();
                    System.out.println("["+i+"]["+num+"]["+socket.getPort()+"]["+(end-begin)+" ms]"+new String(bytes));
                    time = end-begin;
                    i++;
                    Thread.sleep(1000);
                }
                System.out.println("["+num+"]"+time/i+"ms");
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
