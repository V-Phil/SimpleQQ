package qqserver.service;

import qqcommon.Message;
import qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wang zifan
 * @version 1.0
 * @date 2022/7/30  17:56
 */
public class ServerConnectClientThread extends Thread {
    private Socket socket;
    private String userId;
    //private static ConcurrentHashMap<String, ArrayList<Message>> offLineDb = new ConcurrentHashMap<>();


    public ServerConnectClientThread(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run() {
        while(true) {
            try {
                System.out.println("服务端和客户端" + userId + "保持通信，读取数据...");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();
                //读取在线列表
                if (message.getMesType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)) {
                    System.out.println(message.getSender() + "要在线用户列表");
                    String onlineUser = ManageClientThreads.getOnlineUser();
                    Message message2 = new Message();
                    message2.setMesType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                    message2.setContent(onlineUser);
                    message2.setGetter(message.getSender());
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message2);
                }//转发文件
                else if (message.getMesType().equals(MessageType.MESSAGE_FILE_MES)) {
                    ObjectOutputStream oos = new ObjectOutputStream(ManageClientThreads.getServerConnectClientThread(message.getGetter()).socket.getOutputStream());
                    oos.writeObject(message);

                }
                //私聊通信
                else if (message.getMesType().equals(MessageType.MESSAGE_COMM_MES)) {
                    ServerConnectClientThread clientThread = ManageClientThreads.getServerConnectClientThread(message.getGetter());
                    if (clientThread == null){//用户不在线，存储到一个地方
                        ManagerUserMessage.addMessage(message.getGetter(),message.getContent());
                    } else {

                        ObjectOutputStream oos =
                                new ObjectOutputStream(ManageClientThreads.getServerConnectClientThread(message.getGetter()).socket.getOutputStream());
                        oos.writeObject(message);
                    }

                }
                //群发通信
                else if (message.getMesType().equals(MessageType.MESSAGE_To_All_MES)) {
                    HashMap<String, ServerConnectClientThread> hm = ManageClientThreads.getHm();
                    Iterator<String> iterator = hm.keySet().iterator();
                    while (iterator.hasNext()) {
                        String onLineUserId = iterator.next().toString();
                        if (!onLineUserId.equals(message.getSender())) {
                            ObjectOutputStream oos = new ObjectOutputStream(hm.get(onLineUserId).getSocket().getOutputStream());
                            oos.writeObject(message);

                        }
                    }
                } //退出程序
                else if (message.getMesType().equals(MessageType.MESSAGE_CLIENT_EXIT)) {
                    System.out.println(message.getSender() + "退出");
                    //ManageClientThreads.removeServerConnectClientThread(userId);
                    ManageClientThreads.removeServerConnectClientThread(message.getSender());
                    socket.close();
                    break;


                } else {
                    System.out.println("暂时不做处理");
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
}
