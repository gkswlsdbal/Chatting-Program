package Tcp;

import java.net.*;

import Tcp.tcp_chat;

import java.io.*;

public class Server_sendFile{


    /**
    *  클라이언트간의 파일전송을 목적으로 함.
    */

    public Server_sendFile() throws Exception {
        while(true) {
        ServerSocket ss = new ServerSocket(9999);
        System.out.println( "서버소켓을 생성하였습니다..!!" );

        Socket s = ss.accept();
        System.out.println("소켓 "+s+" 와 연결되었습니다..!!");

        InputStream in = s.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        System.out.println("스트림을 얻었습니다..!!");
        String fileName = br.readLine();

        File f = new File("c:\\text\\", fileName);
        System.out.println("객체 생성");

        FileOutputStream out = new FileOutputStream(f);

            int  i = 0;
            while( ( i = in.read() ) != -1) {
                out.write ((char)i);
            }

        // 시스템정리
        br.close();
        in.close();
        out.close();
        s.close();
        ss.close();

        }
    }

    public static void main(String[] args) {
        try {
            new Server_sendFile();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}