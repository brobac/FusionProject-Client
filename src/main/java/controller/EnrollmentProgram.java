package controller;


import infra.dto.*;
import network.Protocol;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Scanner;

public class EnrollmentProgram {
    private static InputStream is;
    private static OutputStream os;

    public EnrollmentProgram(InputStream is, OutputStream os) {
        this.is = is;
        this.os = os;
    }

    public void run() {
        Scanner sc = new Scanner(System.in);
        System.out.println("************* 수강신청 프로그램 **************");
        try {
            AccountDTO user;
            int menu = 0;
            while (menu != 2) {
                System.out.println(Message.ENROLLMENT_MENU);  // 로그인 or 종료 선택
                menu = sc.nextInt();
                switch (menu) {
                    case 1:
                        user = login();
                        if (user == null)
                            continue;
                        break;
                    case 2:
                    default:
                        continue;
                }
                EnrollmentService service = createService(user, is, os);
                service.run();
            }
            System.out.println("************* 프로그램 종료 **************");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e);
        }
    }


    private AccountDTO login() throws Exception {
        while (true) {
            BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));
            System.out.print(Message.ID_INPUT);
            String id = userIn.readLine();
            System.out.print(Message.PW_INPUT);
            String pw = userIn.readLine();
            AccountDTO accountDTO = AccountDTO.builder()
                    .id(id)
                    .password(pw)
                    .build();
            Protocol protocol = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_LOGIN);
            protocol.setObject(accountDTO);
            protocol.send(os);

            Protocol recv = new Protocol();
            recv.read(is);

            if (recv.getType() == Protocol.TYPE_RESPONSE) {
                if (recv.getCode() == Protocol.T2_CODE_SUCCESS)
                    return (AccountDTO) recv.getObject();    // 어떤 사용자인지 return
                else if (recv.getCode() == Protocol.T2_CODE_FAIL) // 로그인 실패시 null return
                    return null;
            } else
                throw new Exception("서버에서 요청이 왜 와???"); //TODO 최종본에선 지울것
        }
    }

    private static EnrollmentService createService(AccountDTO account, InputStream is, OutputStream os) {
        if (account.getPosition().equals("ADMIN")) {
//            return new AdminService(account, is, os);
        } else if (account.getPosition().equals("PROF")) {
//            return new ProfessorService(account, is, os);
        } else if (account.getPosition().equals("STUD")) {
            return new StudentService(account, is, os);
        }
        return null;
    }
}
