package qqserver.service;

import qqcommon.Message;
import qqcommon.MessageType;
import utils.Utility;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author wang zifan
 * @version 1.0
 * @date 2022/8/7  17:07
 */
public class SendNewsToAllService implements Runnable {

    @Override
    public void run() {
        while (true) {

            System.out.println("请输入服务器要推送的消息/新闻:(exit表示退出)");
            String news = Utility.readString(100);
            if ("exit".equals(news)){
                break;
            }
            Message message = new Message();
            message.setMesType(MessageType.MESSAGE_To_All_MES);
            message.setContent(news);
            message.setSender("服务器");
            message.setSendTime(new Date().toString());
            System.out.println("服务器推送消息给所有人说" + news);
            HashMap<String, ServerConnectClientThread> hm = ManageClientThreads.getHm();
            Iterator<String> iterator = hm.keySet().iterator();
            while (iterator.hasNext()) {
                String onLineUserId = iterator.next().toString();
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(hm.get(onLineUserId).getSocket().getOutputStream());
                    oos.writeObject(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }
    }
}
