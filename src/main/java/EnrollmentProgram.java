import domain.model.Account;
import dto.AccountDTO;
import network.Protocol;

import java.io.*;
import java.net.Socket;

public class EnrollmentProgram {
    private static Socket socket;
    private static InputStream is;
    private static OutputStream os;

    public static void run(String address, int portNumber) throws IOException {
        socket = new Socket(address, portNumber);
        is = socket.getInputStream();
        os = socket.getOutputStream();
        //login 해서 응답받기
        if (login()) {
            EnrollmentService service = createService("admin", is, os);
            service.run();
        }
        //직책에 따라 다른 서비스 실행
    }
    private static AccountDTO login() throws IOException, IllegalAccessException {
        while (true) {
            BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("아이디 : ");
            String id = userIn.readLine();
            System.out.print("비밀번호 : ");
            String pw = userIn.readLine();
            AccountDTO accountDTO = AccountDTO.builder()
                    .id(id)
                    .password(pw)
                    .build();
            Protocol protocol = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_CREATE, Protocol.ENTITY_ACCOUNT);
            protocol.setObject(accountDTO);
            os.write(protocol.getPacket());


            //TODO 로그인 정보 생성 및 패킷 전송
        }
    }
    private static EnrollmentService createService(String authority, InputStream is, OutputStream os) {
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
