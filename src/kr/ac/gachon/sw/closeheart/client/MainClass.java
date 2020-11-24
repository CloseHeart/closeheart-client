package kr.ac.gachon.sw.closeheart.client;

import kr.ac.gachon.sw.closeheart.client.login.login.LoginForm;

import java.io.*;

/*
 * Application 구동용 Main Class
 * @author Minjae Seon
 */
public class MainClass {
    public static void main(String[] args) {
        // 연결 정보 로드 후 정보를 LoginForm으로 보냄
        ConnectionInfo info = getServerInfo();
        new LoginForm(info);
    }

    // Socket Connection 정보를 파일에서 얻기
    private static ConnectionInfo getServerInfo() {
        // Default Address & Port
        ConnectionInfo info = new ConnectionInfo("localhost", 21325);

        try {
            // 파일에서 접속 정보 가져오기
            File serverDataFile = new File("closeheart_server.dat");
            FileReader fileReader = new FileReader(serverDataFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            // Connection Info를 담고 있는 파일은 첫 번째 줄에 서버 주소, 두 번째 줄에 포트 넘버를 가지고 있음.
            info.serverAddress = bufferedReader.readLine();
            info.serverPort = Integer.parseInt(bufferedReader.readLine());
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage() + "\nUsing default infomation.");
        }
        return info;
    }
}
