package qqclient.service;
import qqcommon.Message;
import qqcommon.MessageType;
import qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author wang zifan
 * @version 1.0
 * @date 2022/7/24  16:46
 * 该类完成用户登陆验证和用户注册等功能
 */
public class UserClientService {
    private User u = new User();
    private Socket socket;
    boolean b = false;

    //登陆验证
    public boolean checkUser(String userId, String password) {
//        u = new User(userId, password);
        u.setUserId(userId);
        u.setPassward(password);
        try {
            socket = new Socket(InetAddress.getByName("192.168.0.102"), 9999);
            //socket = new Socket(InetAddress.getByName("192.168.1.13"), 9999);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(u);
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message ms = (Message) ois.readObject();
            if (ms.getMesType().equals(MessageType.MESSAGE_LOGIN_SUCCEED)) {
                if (ms.getContent() != null) {
                    System.out.println("亲，你有离线留言信息哦:");
                    System.out.print(ms.getContent());
                }
                ClientConnectServerThread clientConnectServerThread
                        = new ClientConnectServerThread(socket);
                clientConnectServerThread.start();
                ManageClientConnectServerThread.addClientConnectServerThread(userId, clientConnectServerThread);

                b = true;
            } else {
                socket.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return b;
    }

    //向服务端请求在线用户列表
    public void onlineFriendList() {
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
        message.setSender(u.getUserId());

        //发送服务器，应该得到当前线程的Socket对应的ObjectOutputStream对象
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(u.getUserId()).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //推出客户端，并向服务器发送消息
    public void logout() {
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_CLIENT_EXIT);
        message.setSender(u.getUserId());
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(u.getUserId()).getSocket().getOutputStream());
            oos.writeObject(message);
            System.out.println(u.getUserId() + "退出系统");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
