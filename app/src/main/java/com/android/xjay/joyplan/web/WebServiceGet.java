package com.android.xjay.joyplan.web;

import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * 使用get方法获取Http服务器数据
 */

public class WebServiceGet {
    //登录接口
    public static String loginGet(String username, String password, String address){
        HttpURLConnection connection = null;
        InputStream in = null;

        try{
            String Url = "http://110.64.91.150:8080/joyweb3.0/"+address;
            String path = Url + "?phone_number=" + username + "&password=" + password;
            try {
                URL url = new URL(path);
                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(10000);//建立连接超时
                connection.setReadTimeout(8000);//传递数据超时
                in = connection.getInputStream();
                return parseInfo(in);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
         }catch (Exception e) {
            e.printStackTrace();
            }finally {
            //意外退出时，连接关闭保护
            if(connection != null){
                connection.disconnect();
            }
            if(in != null){
                try{
                    in.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    //注册接口
    public static String registerGet(String phone_number,String password,String nick_name,String university,String address){
        HttpURLConnection connection = null;
        InputStream in = null;

        try{
            String Url = "http://110.64.91.150:8080/joyweb3.0/"+address;
            String path = Url + "?username=" + URLEncoder.encode(phone_number,"UTF-8") + "&password=" + URLEncoder.encode(password,"UTF-8")
                    +"&nick_name="+URLEncoder.encode(nick_name,"UTF-8")
                    +"&university="+URLEncoder.encode(university,"UTF-8");
            try {
                System.out.println("问题1");
                URL url = new URL(path);
                System.out.println("问题2");
                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(10000);//建立连接超时
                connection.setReadTimeout(8000);//传递数据超时
                in = connection.getInputStream();
                System.out.println("问题3");
                return parseInfo(in);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            //意外退出时，连接关闭保护
            if(connection != null){
                connection.disconnect();
            }
            if(in != null){
                try{
                    in.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        System.out.println("问题4");
        return null;
    }
    //添加日程活动
    public static String addagendaGet(String title,String starttime,String endtime,String content,String address){
        HttpURLConnection connection = null;
        InputStream in = null;

        try{
            String Url = "http://110.64.91.150:8080/joyweb3.0/"+address;
            String path = Url + "?title=" + URLEncoder.encode(title,"UTF-8") + "&starttime=" + URLEncoder.encode(starttime,"UTF-8")
                    +"&endtime="+URLEncoder.encode(endtime,"UTF-8")
                    +"&content="+URLEncoder.encode(content,"UTF-8");
            try {
                URL url = new URL(path);
                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(10000);//建立连接超时
                connection.setReadTimeout(8000);//传递数据超时
                in = connection.getInputStream();
                return parseInfo(in);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            //意外退出时，连接关闭保护
            if(connection != null){
                connection.disconnect();
            }
            if(in != null){
                try{
                    in.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    //得到字节输入流，将字节输入流转化为String类型
    /*public static String parseInfo(InputStream inputStream){
        BufferedReader reader = null;
        String line = "";
        StringBuilder response = new StringBuilder();

        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            while((line = reader.readLine()) != null){
                response.append(line);
            }
            return response.toString();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(reader != null){
                try{
                    reader.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }*/
    // 将输入流转化为 String 型
    private static String parseInfo(InputStream inStream) throws Exception {
        byte[] data = read(inStream);
        // 转化为字符串
        System.out.println("问题5");
        return new String(data, "UTF-8");
    }

    // 将输入流转化为byte型
    public static byte[] read(InputStream inStream) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        inStream.close();
        return outputStream.toByteArray();
    }
}
