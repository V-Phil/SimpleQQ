package qqserver.service;

import qqcommon.Message;

import java.util.HashMap;
import java.util.Iterator;

/**
 * @author wang zifan
 * @version 1.0
 * @date 2022/8/1  16:40
 * 该类用于管理和客户端通信的线程
 */
public class ManageClientThreads {
    private static HashMap<String, ServerConnectClientThread> hm = new HashMap<>();
    public static void addClientThread(String userId, ServerConnectClientThread serverConnectClientThread) {
        hm.put(userId, serverConnectClientThread);
    }

    public static HashMap<String, ServerConnectClientThread> getHm() {
        return hm;
    }

    public static ServerConnectClientThread getServerConnectClientThread(String userId) {
        return hm.get(userId);
    }

    //返回在线列表
    public static String getOnlineUser() {
        Iterator<String> iterator = hm.keySet().iterator();
        String onlineUserList = "";
        while (iterator.hasNext()) {
            onlineUserList += iterator.next().toString() + " ";
        }
        return onlineUserList;
    }

    //退出程序
    public static void removeServerConnectClientThread(String userId) {
        hm.remove(userId);
    }


}
