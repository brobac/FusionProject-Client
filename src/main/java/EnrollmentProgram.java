import java.io.*;
import java.net.Socket;

public class EnrollmentProgram {

    public static void run(String address, int portNumber) throws IOException {
        Socket socket = new Socket(address, portNumber);
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();
        //login 해서 응답받기
        if (login()) {
            EnrollmentService service = createService("admin", is, os);
            service.run();
        }
        //직책에 따라 다른 서비스 실행
    }

    public static boolean login() throws IOException {
        while (true) {
            BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("아이디 : ");
            String id = userIn.readLine();
            System.out.print("비밀번호 : ");
            String pw = userIn.readLine();
            //TODO 로그인 정보 생성 및 패킷 전송
        }
    }

    public static EnrollmentService createService(String authority, InputStream is, OutputStream os) {
        if (authority.equals("admin")) {
            return new AdminService(is, os);
        } else if (authority.equals("professor")) {
            return new ProfessorService(is, os);
        } else if (authority.equals("student")) {
            return new StudentService(is, os);
        } else {
            return null;
        }
    }
}
