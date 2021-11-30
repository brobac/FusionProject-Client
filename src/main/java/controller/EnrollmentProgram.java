package controller;


import infra.dto.*;
import network.Protocol;

import java.io.*;
import java.sql.SQLOutput;
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
            AccountDTO user = null;
            int menu = 0;
            while (menu != 3) {
                System.out.println(Message.ENROLLMENT_MENU);  // 로그인 or 종료 선택
                menu = Integer.parseInt(sc.nextLine());
                switch (menu) {
                    case 1: //로그인
                        user = login();
                        if (user == null)
                            continue;
                        break;
                    case 2: //관리자 계정 생성
                        createAdmin();
                        break;
                    case 3: //종료
                    default:
                        continue;
                }

                if(user!=null){
                    EnrollmentService service = createService(user, is, os);
                    service.run();
                }
            }
            System.out.println("************* 프로그램 종료 **************");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e);
        }
    }

    private void createAdmin() throws Exception {
        while(true){
            Scanner sc = new Scanner(System.in);
            System.out.println(Message.ADMIN_CREATE_MESSAGE);

            System.out.print(Message.ADMIN_CODE_INPUT);
            String adminCode = sc.nextLine();

            System.out.print(Message.ADMIN_NAME_INPUT);
            String name = sc.nextLine();

            System.out.print(Message.ADMIN_DEPARTMENT_INPUT);
            String department = sc.nextLine();

            System.out.print(Message.ADMIN_BIRTHDATE_INPUT);
            String birthDate = sc.nextLine();

            AdminDTO newAdmin = AdminDTO.builder()
                    .name(name)
                    .adminCode(adminCode)
                    .department(department)
                    .birthDate(birthDate)
                    .build();

            Protocol sendPt = new Protocol(
                    Protocol.TYPE_REQUEST, Protocol.T1_CODE_CREATE, Protocol.ENTITY_ADMIN
            );
            sendPt.setObject(newAdmin);
            sendPt.send(os);

            Protocol recvPt = new Protocol();
            recvPt.read(is);

            if (recvPt.getType() == Protocol.TYPE_RESPONSE) {
                if (recvPt.getCode() == Protocol.T2_CODE_SUCCESS) {
                    System.out.println(Message.ADMIN_CREATE_SUCCESS);
                    return;
                } else if (recvPt.getCode() == Protocol.T2_CODE_FAIL) {
                    MessageDTO failMsg = (MessageDTO) recvPt.getObject();
                    System.out.println(Message.ADMIN_CREATE_FAIL);
                    System.out.println(failMsg);
                }
            }
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
                else if (recv.getCode() == Protocol.T2_CODE_FAIL){
                    MessageDTO failMsg = (MessageDTO) recv.getObject();
                    System.out.println(failMsg);
                }
            }
        }
    }

    private static EnrollmentService createService(AccountDTO account, InputStream is, OutputStream os) {
        if (account.getPosition().equals("ADMIN")) {
            return new AdminService(account, is, os);
        } else if (account.getPosition().equals("PROF")) {
            return new ProfessorService(account, is, os);
        } else if (account.getPosition().equals("STUD")) {
            return new StudentService(account, is, os);
        }
        return null;
    }
}
