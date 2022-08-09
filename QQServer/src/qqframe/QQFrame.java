package qqframe;

import qqserver.service.QQServer;

/**
 * @author wang zifan
 * @version 1.0
 * @date 2022/8/1  16:52
 * 该类创建QQServer，启动后台服务
 */
public class QQFrame {
    public static void main(String[] args) {
        new QQServer();
    }
}
