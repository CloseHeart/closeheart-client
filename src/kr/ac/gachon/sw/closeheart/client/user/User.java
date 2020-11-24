package kr.ac.gachon.sw.closeheart.client.user;

public class User {
    private String userToken;
    private String userID;
    private String userNick;
    private String userMsg;
    private boolean isOnline;


    public User(String userID, String userNick, String userMsg, boolean isOnline) {
        this.userID = userID;
        this.userNick = userNick;
        this.userMsg = userMsg;
        this.isOnline = isOnline;
    }

    public User(String userID, String userToken, String userNick, String userMsg) {
        this.userID = userID;
        this.userNick = userNick;
        this.userMsg = userMsg;
        this.userToken = userToken;
    }

    public String getUserToken() {
        return userToken;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserNick() {
        return userNick;
    }

    public String getUserMsg() {
        return userMsg;
    }

    public boolean getOnline() {
        return isOnline;
    }

    public void setUserMsg(String userMsg) {
        this.userMsg = userMsg;
    }

    public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }
}
