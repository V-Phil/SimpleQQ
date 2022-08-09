package qqclient.service;

import qqcommon.Message;
import qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Date;

/**
 * @author wang zifan
 * @version 1.0
 * @date 2022/8/4  16:47
 * 该类提供和消息相关的服务方法
 */
public class MessageClientService {
    //群发
    public void sendMessageToAll(String sender, String content) {
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_To_All_MES);
        message.setSender(sender);
        message.setContent(content);
        message.setSendTime(new Date().toString());


        System.out.println(sender + "对大家说" + content);
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(sender).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //私聊
    public void sendMessageToOne(String sender, String getter, String content) {
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_COMM_MES);
        message.setSender(sender);
        message.setGetter(getter);
        message.setContent(content);
        message.setSendTime(new Date().toString());
        System.out.println(sender + "对" + getter + "说" + content);
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(sender).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

