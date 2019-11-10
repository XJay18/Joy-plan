package com.android.xjay.joyplan.web;

import com.android.xjay.joyplan.Agenda;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 使用get方法获取Http服务器数据
 */

public class WebServiceGet {
    /**
     * 登录接口
     */
    public static String loginGet(String username, String password){
        HttpURLConnection connection = null;
        InputStream in = null;

        try {
            String Url = "http://110.64.91.150:8080/joyweb3.0/LogLet";
            String path = Url + "?phone_number=" + username + "&password=" + password;
            try {
                URL url = new URL(path);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(10000);//建立连接超时
                connection.setReadTimeout(8000);//传递数据超时
                in = connection.getInputStream();
                return parseInfo(in);
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                connection.disconnect();
                return "no_connection";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            //意外退出时，连接关闭保护
            if (connection != null) {
                connection.disconnect();
            }
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 注册接口
     */
    public static String registerGet(String phone_number,String password,String nick_name,String university){
        HttpURLConnection connection = null;
        InputStream in = null;
        try {
            String Url = "http://110.64.91.150:8080/joyweb3.0/RegLet";
            String path = Url + "?phone_number=" + URLEncoder.encode(phone_number, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8")
                    + "&nick_name=" + URLEncoder.encode(nick_name, "UTF-8")
                    + "&university=" + URLEncoder.encode(university, "UTF-8");
            try {
                URL url = new URL(path);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(10000);//建立连接超时
                connection.setReadTimeout(8000);//传递数据超时
                in = connection.getInputStream();
                return parseInfo(in);
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                connection.disconnect();
                return "no_connection";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //意外退出时，连接关闭保护
            if (connection != null) {
                connection.disconnect();
            }
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 检查手机号码接口
     */
    public static String phoneGet(String phone_number){
        HttpURLConnection connection = null;
        InputStream in = null;

        try {
            String Url = "http://110.64.91.150:8080/joyweb3.0/PhoLet";
            String path = Url + "?phone_number=" + phone_number;
            try {
                URL url = new URL(path);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(10000);//建立连接超时
                connection.setReadTimeout(8000);//传递数据超时
                in = connection.getInputStream();
                return parseInfo(in);
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                connection.disconnect();
                return "no_connection";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            //意外退出时，连接关闭保护
            if (connection != null) {
                connection.disconnect();
            }
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 添加日程活动
     */
    public static String addagendaGet(String userid,String title,String starttime,String endtime,String content,String address){
        HttpURLConnection connection = null;
        InputStream in = null;

        try {
            String Url = "http://110.64.91.150:8080/joyweb3.0/Agenda";
            String path = Url + "?userid=" + URLEncoder.encode(userid, "UTF-8")
                    + "&title=" + URLEncoder.encode(title, "UTF-8")
                    + "&starttime=" + URLEncoder.encode(starttime, "UTF-8")
                    + "&endtime=" + URLEncoder.encode(endtime, "UTF-8")
                    + "&content=" + URLEncoder.encode(content, "UTF-8")
                    + "&address=" + URLEncoder.encode(address, "UTF-8");
            try {
                URL url = new URL(path);
                connection = (HttpURLConnection) url.openConnection();
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //意外退出时，连接关闭保护
            if (connection != null) {
                connection.disconnect();
            }
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 查找用户的课程详情
     */
    public static Agenda[] selectagendaGet(String userid){
        HttpURLConnection connection = null;
        InputStream in = null;

        try {
            String Url = "http://110.64.91.150:8080/joyweb3.0/SelAgen";
            String path = Url + "?userid=" + URLEncoder.encode(userid, "UTF-8");
            try {

                URL url = new URL(path);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(10000);//建立连接超时
                connection.setReadTimeout(8000);//传递数据超时
                in = connection.getInputStream();
                //解析json数据
                byte[] data = read(in);
                String json = new String(data);
                JSONObject obj = new JSONObject(json);
                JSONArray jsonAgenda = obj.getJSONArray("agenda");
                Agenda[] ao = new Agenda[jsonAgenda.length()];
                //将json对象转化为java对象
                for (int i = 0; i < jsonAgenda.length(); i++) {
                    JSONObject agenda = jsonAgenda.getJSONObject(i);
                    String title = agenda.getString("title");
                    String starttime = agenda.getString("starttime");
                    String endtime = agenda.getString("endtime");
                    String content = agenda.getString("content");
                    String add = agenda.getString("address");
                    ao[i] = new Agenda(title, starttime, endtime, content, add);
                    //System.out.println("json解析="+title+starttime+endtime+content+add);
                }

                return ao;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //意外退出时，连接关闭保护
            if (connection != null) {
                connection.disconnect();
            }
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    //得到字节输入流，将字节输入流转化为String类型
//    public static String parseInfo(InputStream inputStream){
//        BufferedReader reader = null;
//        String line = "";
//        StringBuilder response = new StringBuilder();
//
//        try {
//            reader = new BufferedReader(new InputStreamReader(inputStream));
//            while((line = reader.readLine()) != null){
//                response.append(line);
//            }
//            return response.toString();
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            if(reader != null){
//                try{
//                    reader.close();
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//        }
//        return null;
//    }

    /**
     * 将输入流转化为 String 型
     */
    private static String parseInfo(InputStream inStream) throws Exception {
        byte[] data = read(inStream);
        // 转化为字符串
        return new String(data, StandardCharsets.UTF_8);
    }

    /**
     * 将输入流转化为byte型
     */
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
