import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class EnrollmentProgram {
    public void login() throws IOException {
        while (true){
            BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("아이디 : ");
            String id = userIn.readLine();
            System.out.print("비밀번호 : ");
            String pw = userIn.readLine();
            //TODO 로그인 정보 생성 및 패킷 전송
        }
    }
}
