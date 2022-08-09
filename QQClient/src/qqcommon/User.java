package qqcommon;

import java.io.Serializable;

/**
 * @author wang zifan
 * @version 1.0
 * @date 2022/7/22  20:50
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;
    private String password;

    public User() {}

    public User(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassward() {
        return password;
    }

    public void setPassward(String password) {
        this.password = password;
    }
}
