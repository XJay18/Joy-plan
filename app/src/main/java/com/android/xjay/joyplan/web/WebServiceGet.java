package com.android.xjay.joyplan.web;

import com.android.xjay.joyplan.Agenda;
import com.android.xjay.joyplan.AgendaStatistics;
import com.android.xjay.joyplan.Course;
import com.android.xjay.joyplan.StudentActivityInfo;

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
    public static String loginGet(String username, String password) {
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
    public static String registerGet(String phone_number, String password, String nick_name, String university) {
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
        return "no_connection";
    }

    /**
     * 检查手机号码接口
     */
    public static String phoneGet(String phone_number) {
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
     * 添加日程
     */
    public static String addAgendaGet(String userid, String title, String starttime, String endtime, String notation, String address) {
        HttpURLConnection connection = null;
        InputStream in = null;

        try {
            String Url = "http://110.64.91.150:8080/joyweb3.0/Agenda";
            String path = Url + "?userid=" + URLEncoder.encode(userid, "UTF-8")
                    + "&title=" + URLEncoder.encode(title, "UTF-8")
                    + "&starttime=" + URLEncoder.encode(starttime, "UTF-8")
                    + "&endtime=" + URLEncoder.encode(endtime, "UTF-8")
                    + "&notation=" + URLEncoder.encode(notation, "UTF-8")
                    + "&address=" + URLEncoder.encode(address, "UTF-8");
            try {
                URL url = new URL(path);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);//建立连接超时
                connection.setReadTimeout(8000);//传递数据超时
                in = connection.getInputStream();
                return parseInfo(in);
            }  catch (SocketTimeoutException e) {
                e.printStackTrace();
                if(connection !=null) {
                    connection.disconnect();
                }
                return "no_connection";
            }catch (MalformedURLException e) {
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
        return "no_connection";
    }

    /**
     * 查找日程
     */
    public static Agenda[] selAgendaGet(String userid) {
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
                    String notation = agenda.getString("notation");
                    String add = agenda.getString("address");
                    ao[i] = new Agenda(title, starttime, endtime, notation, add);
                    //System.out.println("解析="+title+starttime+endtime+notation+add);
                }

                return ao;
            }  catch (SocketTimeoutException e) {
                e.printStackTrace();
                connection.disconnect();
                return null;
            }catch (MalformedURLException e) {
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
     * 删除日程
     */
    public static String delAgendaGet(String userid, String title, String starttime) {
        HttpURLConnection connection = null;
        InputStream in = null;

        try {
            String Url = "http://110.64.91.150:8080/joyweb3.0/DelAgenda";
            String path = Url + "?userid=" + URLEncoder.encode(userid, "UTF-8")
                    + "&title=" + URLEncoder.encode(title, "UTF-8")
                    + "&starttime=" + URLEncoder.encode(starttime, "UTF-8");
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
        return "no_connection";
    }

    /**
     * 删除日程
     */
    public static String upAgendaGet(String userid, String title, String starttime,String notation) {
        HttpURLConnection connection = null;
        InputStream in = null;

        try {
            String Url = "http://110.64.91.150:8080/joyweb3.0/UpAgenda";
            String path = Url + "?userid=" + URLEncoder.encode(userid, "UTF-8")
                    + "&title=" + URLEncoder.encode(title, "UTF-8")
                    + "&starttime=" + URLEncoder.encode(starttime, "UTF-8")
                    + "&notation=" + URLEncoder.encode(notation, "UTF-8");
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
        return "no_connection";
    }


    /**
     * 添加活动
     */
    public static String addActiGet(String userid, String title, String info, String starttime, String endtime, String address, String img) {
        HttpURLConnection connection = null;
        InputStream in = null;

        try {
            String Url = "http://110.64.91.150:8080/joyweb3.0/Activity";
            String path = Url + "?userid=" + URLEncoder.encode(userid, "UTF-8")
                    + "&title=" + URLEncoder.encode(title, "UTF-8")
                    + "&info=" + URLEncoder.encode(info, "UTF-8")
                    + "&starttime=" + URLEncoder.encode(starttime, "UTF-8")
                    + "&endtime=" + URLEncoder.encode(endtime, "UTF-8")
                    + "&address=" + URLEncoder.encode(address, "UTF-8")
                    + "&img=" + URLEncoder.encode(img, "UTF-8");
            try {
                URL url = new URL(path);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(10000);//建立连接超时
                connection.setReadTimeout(8000);//传递数据超时
                in = connection.getInputStream();
                return parseInfo(in);
            }  catch (SocketTimeoutException e) {
                e.printStackTrace();
                connection.disconnect();
                return "no_connection";
            }catch (MalformedURLException e) {
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
        return "no_connection";
    }

    /**
     * 查找活动
     */
    public static StudentActivityInfo[] selActiGet(String userid) {
        HttpURLConnection connection = null;
        InputStream in = null;

        try {
            String Url = "http://110.64.91.150:8080/joyweb3.0/SelActivity";
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
                JSONArray jsonActivity = obj.getJSONArray("activity");
                StudentActivityInfo[] aco = new StudentActivityInfo[jsonActivity.length()];
                //将json对象转化为java对象
                for (int i = 0; i < jsonActivity.length(); i++) {
                    JSONObject agenda = jsonActivity.getJSONObject(i);
                    String title = agenda.getString("title");
                    String info = agenda.getString("info");
                    String starttime = agenda.getString("starttime");
                    String endtime = agenda.getString("endtime");
                    String address = agenda.getString("address");
                    String imgS = agenda.getString("img");
                    byte[] img=imgS.getBytes();
                    aco[i] = new StudentActivityInfo(title, info, starttime, endtime, address, img);
                    //System.out.println("解析="+title+starttime+endtime+notation+add);
                }

                return aco;
            }  catch (SocketTimeoutException e) {
                e.printStackTrace();
                connection.disconnect();
                return null;
            }catch (MalformedURLException e) {
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
     * 添加课程表
     */
    public static String addCourseGet(String userid,String year, String indexofsemester,String coursename,String dayofweek,String startweek,String endweek,String startindex,String numofcourse,String address,String teachername,String notation) {
        HttpURLConnection connection = null;
        InputStream in = null;

        try {
            String Url = "http://110.64.91.150:8080/joyweb3.0/Course";
            String path = Url + "?userid=" + URLEncoder.encode(userid, "UTF-8")
                    + "&year=" + URLEncoder.encode(year, "UTF-8")
                    + "&indexofsemester=" + URLEncoder.encode(indexofsemester, "UTF-8")
                    + "&coursename=" + URLEncoder.encode(coursename, "UTF-8")
                    + "&dayofweek=" + URLEncoder.encode(dayofweek, "UTF-8")
                    + "&startweek=" + URLEncoder.encode(startweek, "UTF-8")
                    + "&endweek=" + URLEncoder.encode(endweek, "UTF-8")
                    + "&startindex=" + URLEncoder.encode(startindex, "UTF-8")
                    + "&numofcourse=" + URLEncoder.encode(numofcourse, "UTF-8")
                    + "&address=" + URLEncoder.encode(address, "UTF-8")
                    + "&teachername=" + URLEncoder.encode(teachername, "UTF-8")
                    + "&notation=" + URLEncoder.encode(notation, "UTF-8");
            try {
                URL url = new URL(path);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(10000);//建立连接超时
                connection.setReadTimeout(8000);//传递数据超时
                in = connection.getInputStream();
                return parseInfo(in);
            }  catch (SocketTimeoutException e) {
                e.printStackTrace();
                connection.disconnect();
                return "no_connection";
            }catch (MalformedURLException e) {
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
        return "no_connection";
    }

    /**
     * 查找课程表
     */
    public static Course[] selCourseGet(String userid) {
        HttpURLConnection connection = null;
        InputStream in = null;

        try {
            String Url = "http://110.64.91.150:8080/joyweb3.0/SelCourse";
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
                JSONArray jsonActivity = obj.getJSONArray("course");
                Course[] co = new Course[jsonActivity.length()];
                //将json对象转化为java对象
                for (int i = 0; i < jsonActivity.length(); i++) {
                    JSONObject agenda = jsonActivity.getJSONObject(i);
                    int year = agenda.getInt("year");
                    int indexofsemester = agenda.getInt("indexofsemester");
                    String coursename = agenda.getString("coursename");
                    int dayofweek = agenda.getInt("dayofweek");
                    int startweek = agenda.getInt("startweek");
                    int endweek = agenda.getInt("endweek");
                    int startindex = agenda.getInt("startindex");
                    int numofcourse = agenda.getInt("numofcourse");
                    String address = agenda.getString("address");
                    String teachername = agenda.getString("teachername");
                    String notation = agenda.getString("notation");

                    co[i] = new Course(year, indexofsemester, coursename,dayofweek,startweek,endweek,startindex,numofcourse,address,teachername,notation);
                    //System.out.println("解析="+title+starttime+endtime+notation+add);
                }

                return co;
            }  catch (SocketTimeoutException e) {
                e.printStackTrace();
                connection.disconnect();
                return null;
            }catch (MalformedURLException e) {
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
     * 删除课程表
     */
    public static String delCourseGet(String userid,int year,int indexofsemester,int dayofweek,int startweek,int startindex,String coursename) {
        HttpURLConnection connection = null;
        InputStream in = null;

        try {
            String Url = "http://110.64.91.150:8080/joyweb3.0/DelCourse";
            String path = Url + "?userid=" + URLEncoder.encode(userid, "UTF-8")
                    + "&year=" + year
                    + "&indexofsemester=" + indexofsemester
                    + "&dayofweek=" + dayofweek
                    + "&startweek=" + startweek
                    + "&startindex=" + startindex
                    + "&coursename=" + URLEncoder.encode(coursename, "UTF-8");

            try {
                URL url = new URL(path);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(10000);//建立连接超时
                connection.setReadTimeout(8000);//传递数据超时
                in = connection.getInputStream();
                return parseInfo(in);
            }  catch (SocketTimeoutException e) {
                e.printStackTrace();
                connection.disconnect();
                return "no_connection";
            }catch (MalformedURLException e) {
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
        return "no_connection";
    }

    /**
     * 修改课程表
     */
    public static String upCourseGet(String userid,int year,int indexofsemester,int dayofweek,int startweek,int startindex,String coursename,String notation) {
        HttpURLConnection connection = null;
        InputStream in = null;

        try {
            String Url = "http://110.64.91.150:8080/joyweb3.0/UpCourse";
            String path = Url + "?userid=" + URLEncoder.encode(userid, "UTF-8")
                    + "&year=" + year
                    + "&indexofsemester=" + indexofsemester
                    + "&dayofweek=" + dayofweek
                    + "&startweek=" + startweek
                    + "&startindex=" + startindex
                    + "&coursename=" + URLEncoder.encode(coursename, "UTF-8")
                    + "&notation=" + URLEncoder.encode(notation, "UTF-8");

            try {
                URL url = new URL(path);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(10000);//建立连接超时
                connection.setReadTimeout(8000);//传递数据超时
                in = connection.getInputStream();
                return parseInfo(in);
            }  catch (SocketTimeoutException e) {
                e.printStackTrace();
                connection.disconnect();
                return "no_connection";
            }catch (MalformedURLException e) {
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
        return "no_connection";
    }

    /**
     * 添加自己的活动
     */
    public static String addReserveGet(String userid, String title, String info, String starttime, String endtime, String address, String img) {
        HttpURLConnection connection = null;
        InputStream in = null;

        try {
            String Url = "http://110.64.91.150:8080/joyweb3.0/AReserve";
            String path = Url + "?userid=" + URLEncoder.encode(userid, "UTF-8")
                    + "&title=" + URLEncoder.encode(title, "UTF-8")
                    + "&info=" + URLEncoder.encode(info, "UTF-8")
                    + "&starttime=" + URLEncoder.encode(starttime, "UTF-8")
                    + "&endtime=" + URLEncoder.encode(endtime, "UTF-8")
                    + "&address=" + URLEncoder.encode(address, "UTF-8")
                    + "&img=" + URLEncoder.encode(img, "UTF-8");
            try {
                URL url = new URL(path);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(10000);//建立连接超时
                connection.setReadTimeout(8000);//传递数据超时
                in = connection.getInputStream();
                return parseInfo(in);
            }  catch (SocketTimeoutException e) {
                e.printStackTrace();
                connection.disconnect();
                return "no_connection";
            }catch (MalformedURLException e) {
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
        return "no_connection";
    }

    /**
     * 查找自己的活动
     */
    public static StudentActivityInfo[] selReserveGet(String userid) {
        HttpURLConnection connection = null;
        InputStream in = null;

        try {
            String Url = "http://110.64.91.150:8080/joyweb3.0/SelReserve";
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
                JSONArray jsonActivity = obj.getJSONArray("activity");
                StudentActivityInfo[] aco = new StudentActivityInfo[jsonActivity.length()];
                //将json对象转化为java对象
                for (int i = 0; i < jsonActivity.length(); i++) {
                    JSONObject agenda = jsonActivity.getJSONObject(i);
                    String title = agenda.getString("title");
                    String info = agenda.getString("info");
                    String starttime = agenda.getString("starttime");
                    String endtime = agenda.getString("endtime");
                    String address = agenda.getString("address");
                    String imgS = agenda.getString("img");
                    byte[] img=imgS.getBytes();
                    aco[i] = new StudentActivityInfo(title, info, starttime, endtime, address, img);
                    //System.out.println("解析="+title+starttime+endtime+notation+add);
                }

                return aco;
            }  catch (SocketTimeoutException e) {
                e.printStackTrace();
                connection.disconnect();
                return null;
            }catch (MalformedURLException e) {
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
     * 删除课程表
     */
    public static String delReserveGet(String userid,String title,String starttime) {
        HttpURLConnection connection = null;
        InputStream in = null;

        try {
            String Url = "http://110.64.91.150:8080/joyweb3.0/DelReserve";
            String path = Url + "?userid=" + URLEncoder.encode(userid, "UTF-8")
                    + "&title=" + URLEncoder.encode(title, "UTF-8")
                    + "&starttime=" + URLEncoder.encode(starttime, "UTF-8");

            try {
                URL url = new URL(path);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(10000);//建立连接超时
                connection.setReadTimeout(8000);//传递数据超时
                in = connection.getInputStream();
                return parseInfo(in);
            }  catch (SocketTimeoutException e) {
                e.printStackTrace();
                connection.disconnect();
                return "no_connection";
            }catch (MalformedURLException e) {
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
        return "no_connection";
    }

    /**
     * 查询一天内日程数据
     */
    public static AgendaStatistics[] selectagendaStaGet(String userid){
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
                JSONArray jsonAgenda = obj.getJSONArray("agendastatictis");
                AgendaStatistics[] aso = new AgendaStatistics[jsonAgenda.length()];
                //将json对象转化为java对象
                for (int i = 0; i < jsonAgenda.length(); i++) {
                    JSONObject agenda = jsonAgenda.getJSONObject(i);
                    String theday = agenda.getString("theday");
                    int entertainment = agenda.getInt("entertainment");
                    int learning = agenda.getInt("learning");
                    int sport = agenda.getInt("sport");
                    int others = agenda.getInt("others");
                    aso[i] = new AgendaStatistics(theday, entertainment, learning, sport, others);
                    System.out.println("json解析="+theday+entertainment+learning+sport+others);
                }

                return aso;
            }  catch (SocketTimeoutException e) {
                e.printStackTrace();
                connection.disconnect();
                return null;
            }catch (MalformedURLException e) {
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
