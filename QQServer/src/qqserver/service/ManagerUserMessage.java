package qqserver.service;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author wang zifan
 * @version 1.0
 * @date 2022/8/8  17:10
 *  用户请求处理类
 */
public class ManagerUserMessage {
    private static HashMap<String, ArrayList<String>> tempMessage;
    static {
        tempMessage = new HashMap<>();
    }

    public static void addMessage(String userId,String msg){
        ArrayList<String> arrayList = tempMessage.get(userId);
        //是否为空
        if (arrayList != null){
            arrayList.add(msg);
        }else {
            ArrayList<String> array = new ArrayList<>();
            array.add(msg);
            tempMessage.put(userId,array);
        }
    }

    /**
     * 根据用户id 获取离线内容
     * @param userId 用户id
     * @return 离线消息
     */
    public static String getMessage(String userId){
        ArrayList<String> arrayList = tempMessage.get(userId);
        StringBuffer sb = null;
        if (arrayList != null && arrayList.size() > 0){
            sb = new StringBuffer();
            for (String msg : arrayList){
                sb.append(msg).append("\r\n");
            }
            return sb.toString();
        }
        return "";
    }

    /**
     * 根据用户id将消息从容器中移除
     * @param userId 用户id
     */
    public static void removeMessage(String userId){
        ArrayList<String> strings = tempMessage.get(userId);
        strings = null;
        tempMessage.remove(userId);
    }

    public static HashMap<String, ArrayList<String>> getTempMessage(){
        return tempMessage;
    }
}


