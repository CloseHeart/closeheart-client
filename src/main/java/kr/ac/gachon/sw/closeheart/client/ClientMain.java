package kr.ac.gachon.sw.closeheart.client;

import kr.ac.gachon.sw.closeheart.client.login.login.LoginForm;
import kr.ac.gachon.sw.closeheart.client.object.ConnectionInfo;
import kr.ac.gachon.sw.closeheart.client.util.Util;

/*
 * Application 구동용 Main Class
 * @author Minjae Seon
 */
public class ClientMain {
    public static void main(String[] args) {
        // 연결 정보 로드 후 정보를 LoginForm으로 보냄
        ConnectionInfo info = Util.getServerInfo();
        new LoginForm(info);
    }
}
