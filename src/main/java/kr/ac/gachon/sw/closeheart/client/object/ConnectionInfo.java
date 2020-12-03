package kr.ac.gachon.sw.closeheart.client.object;

/*
 * 서버 연결 정보를 담고 있는 Class
 * @author Minjae Seon
 */

public class ConnectionInfo {
    public String serverAddress;
    public int serverPort;

    public ConnectionInfo(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }
}
