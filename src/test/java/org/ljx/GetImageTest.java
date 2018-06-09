package org.ljx;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ljx on 2018/6/4.
 */
public class GetImageTest {
    public static void main(String[] args) {
        int str1 = 1482399410;
        String sj = "//topcoimgtw.akamaized.net/assets/upfile/ep_content/x2/692_17683_1526455433.0392.jpg";
        int str2 = 0;
        for(int x = str1;x<2082399409;x++){
            for(int i=1;i<10000;i++){
                String url = "http://topcoimgtw.akamaized.net/assets/upfile/ep_content/x2/380_9351_"+str1+"."+i+".jpg";
                String path="d:/test/"+str1+"-"+i+".jpg";
                downloadPicture(url,path);
            }
        }
    }
    //链接url下载图片
    private static void downloadPicture(String urlList,String path) {
        URL url = null;
        try {
            url = new URL(urlList);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;

            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
