package com.android.xjay.joyplan.web;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 使用Post方法获取Http服务器数据
 */

public class WebServicePost {
    public static String registerPost(String phone_number, String password, String nick_name, String university, String address) {
        HttpURLConnection connection = null;
        InputStream in = null;

        try {
            String Url = "http://110.64.91.150:8080/joyweb3.0/" + address;
            try {
                URL url = new URL(Url);
                connection = (HttpURLConnection) url.openConnection();

                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setReadTimeout(8000);//传递数据超时

                connection.setUseCaches(false);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

                connection.connect();

                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                String data = "phone_number=" + URLEncoder.encode(phone_number, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8")
                        + "&nick_name=" + URLEncoder.encode(nick_name, "UTF-8")
                        + "&university=" + URLEncoder.encode(university, "UTF-8");
                out.writeBytes(data);
                out.flush();
                out.close();

                int resultCode = connection.getResponseCode();
                if (HttpURLConnection.HTTP_OK == resultCode) {
                    in = connection.getInputStream();

                    return parseInfo(in);
                }
                return null;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("出现错误");
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

    public static String loginPost(String phone_number, String password, String address) {
        HttpURLConnection connection = null;
        InputStream in = null;

        try {
            String Url = "http://110.64.91.150:8080/joyweb3.0/" + address;
            try {
                URL url = new URL(Url);
                connection = (HttpURLConnection) url.openConnection();

                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setReadTimeout(8000);//传递数据超时

                connection.setUseCaches(false);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

                connection.connect();

                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                String data = "phone_number=" + URLEncoder.encode(phone_number, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8");
                out.writeBytes(data);
                out.flush();
                out.close();

                int resultCode = connection.getResponseCode();
                if (HttpURLConnection.HTTP_OK == resultCode) {
                    in = connection.getInputStream();

                    return parseInfo(in);
                }
                return null;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("出现错误");
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

    public static String activityPost(String title, String time, String description, String addr, String address) {
        HttpURLConnection connection = null;
        InputStream in = null;

        try {
            String Url = "http://110.64.91.150:8080/joyplan3.0/" + address;
            try {
                URL url = new URL(Url);
                connection = (HttpURLConnection) url.openConnection();

                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setReadTimeout(8000);//传递数据超时

                connection.setUseCaches(false);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

                connection.connect();

                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                String data = "title=" + URLEncoder.encode(title, "UTF-8") + "&time=" + URLEncoder.encode(time, "UTF-8")
                        + "&description=" + URLEncoder.encode(description, "UTF-8")
                        + "&address=" + URLEncoder.encode(addr, "UTF-8");
                out.writeBytes(data);
                out.flush();
                out.close();

                int resultCode = connection.getResponseCode();
                if (HttpURLConnection.HTTP_OK == resultCode) {
                    in = connection.getInputStream();

                    return parseInfo(in);
                }
                return null;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("出现错误");
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

    public static String checkphonePost(String phone_number, String address) {
        HttpURLConnection connection = null;
        InputStream in = null;

        try {
            String Url = "http://110.64.91.150:8080/joyplan3.0/" + address;
            try {
                URL url = new URL(Url);
                connection = (HttpURLConnection) url.openConnection();

                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setReadTimeout(8000);//传递数据超时

                connection.setUseCaches(false);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

                connection.connect();

                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                String data = "phone_number=" + URLEncoder.encode(phone_number, "UTF-8");
                out.writeBytes(data);
                out.flush();
                out.close();

                int resultCode = connection.getResponseCode();
                if (HttpURLConnection.HTTP_OK == resultCode) {
                    in = connection.getInputStream();

                    return parseInfo(in);
                }
                return null;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("出现错误");
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
    public static String parseInfo(InputStream inputStream) {
        BufferedReader reader = null;
        String line;
        StringBuilder response = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            while ((line = reader.readLine()) != null) {
                Log.d("RegisterActivity", line);
                response.append(line);
            }
            Log.d("RegisterActivity", "response.toString():" + response.toString());
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
