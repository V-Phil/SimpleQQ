package qqserver.service;

import qqcommon.Message;
import qqcommon.MessageType;
import qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wang zifan
 * @version 1.0
 * @date 2022/7/30  17:39
 * 服务器，监听端口9999，等待客户端连接，并保持通信
 */
public class QQServer {
    private ServerSocket ss = null;
    //创建一个集合，存放多个用户
    //HashMap没有处理线程安全，多线程不安全
    //ConcurrentHashMap多线程安全
    private static ConcurrentHashMap<String, User> validUsers = new ConcurrentHashMap<>();
    static {
        validUsers.put("100", new User("100", "123456"));
        validUsers.put("200", new User("200", "123456"));
        validUsers.put("300", new User("300", "123456"));
        validUsers.put("至尊宝", new User("至尊宝", "123456"));
        validUsers.put("紫霞仙子", new User("紫霞仙子", "123456"));
        validUsers.put("菩提老祖", new User("菩提老祖", "123456"));
        }


    //验证登录信息
    private boolean checkUser(String userId, String password) {
        User user = validUsers.get(userId);
        //用户名不存在
        if (user == null) {
            return false;
        }
        //密码不正确
        if (!user.getPassward().equals(password)) {
            return false;
        }
        return true;
    }

    public QQServer() {
        try {
            System.out.println("服务器在9999端口监听");
            new Thread(new SendNewsToAllService()).start();
            ss = new ServerSocket(9999);
            while (true) {
                Socket socket = ss.accept();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                User u = (User) ois.readObject();
                Message message = new Message();

                if (checkUser(u.getUserId(), u.getPassward())) {
                    //登陆成功
                    message.setMesType(MessageType.MESSAGE_LOGIN_SUCCEED);

                    String msg = ManagerUserMessage.getMessage(u.getUserId());
                    if (!("".equals(msg))){
                        message.setContent(msg);
                    }

                    oos.writeObject(message);
                    ServerConnectClientThread serverConnectClientThread =
                            new ServerConnectClientThread(socket, u.getUserId());
                    serverConnectClientThread.start();
                    ManageClientThreads.addClientThread(u.getUserId(), serverConnectClientThread);


                } else {
                    //登陆失败
                    System.out.println("用户" + u.getUserId() +  "验证失败");
                    message.setMesType(MessageType.MESSAGE_LOGIN_FAIL);
                    oos.writeObject(message);
                    socket.close();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //服务器退出while，不在监听，关闭端口
            try {
                ss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}

