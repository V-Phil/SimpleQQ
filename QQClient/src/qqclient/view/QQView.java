package qqclient.view;

import qqclient.service.FileClientService;
import qqclient.service.MessageClientService;
import qqclient.service.UserClientService;
import qqclient.utils.Utility;

/**
 * @author wang zifan
 * @version 1.0
 * @date 2022/7/23  22:03
 */
public class QQView {
    private boolean loop = true;  //控制是否显示菜单
    private String key = "";
    private UserClientService userClientService = new UserClientService();
    private MessageClientService messageClientService = new MessageClientService();
    private FileClientService fileClientService = new FileClientService();

    public static void main(String[] args) {
        new QQView().mainMenu();
    }

    private void mainMenu() {
        while (loop) {
            System.out.println("==========欢迎登陆网络通信系统==========");
            System.out.println("\t\t 1登陆系统");
            System.out.println("\t\t 9退出系统");
            System.out.println("请输入您的选择：");
            key = Utility.readString(1);

            switch (key) {
                case "1":
                    System.out.print("请输入用户号:");
                    String userId = Utility.readString(50);
                    System.out.print("请输入密 码:");
                    String pwd = Utility.readString(50);

                    if (userClientService.checkUser(userId, pwd)) {
                        System.out.println("==========欢迎(用户 " + userId + "登陆成功 ) ==========");
                        while (loop) {
                            System.out.println("\n==========网络通信系统二级菜单(用户 " + userId + " ) ==========");
                            System.out.println("\t\t 1 显示在线用户列表");
                            System.out.println("\t\t 2 群发消息");
                            System.out.println("\t\t 3 私聊消息");
                            System.out.println("\t\t 4 发送文件");
                            System.out.println("\t\t 9 退出系统");
                            System.out.print("请输入你的选择:");
                            key = Utility.readString(1);
                            switch (key) {
                                case "1":
//                                    System.out.println("显示在线用户列表");
                                    userClientService.onlineFriendList();
                                    break;
                                case "2":
                                    System.out.println("请输入想对大家说的话：");
                                    String s = Utility.readString(100);
                                    messageClientService.sendMessageToAll(userId, s);
                                    break;
                                case "3":
                                    System.out.print("请输入想聊天的用户号（在线）： ");
                                    String getterId = Utility.readString(50);
                                    System.out.print("请输入想说的话: ");
                                    String content = Utility.readString(100);
                                    messageClientService.sendMessageToOne(userId, getterId, content);
                                    break;
                                case "4":
                                    System.out.print("请输入你想把文件发给的用户:");
                                    getterId = Utility.readString(50);
                                    System.out.print("请输入发送文件的路径:");
                                    String src = Utility.readString(100);
                                    System.out.print("请输入把文件发送到的对应的路径：");
                                    String dest = Utility.readString(100);
                                    fileClientService.sendFileToOne(src, dest, userId, getterId);
                                    break;
                                case "9":
                                    userClientService.logout();
                                    loop = false;
                                    break;
                            }

                        }

                    } else {
                        System.out.println("登录失败");
                    }
                    break;
                case "9":
                    loop = false;
                    break;
            }

        }
    }

}
