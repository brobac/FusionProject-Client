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
                System.out.println(Message.ENROLLMENT_MENU);  
                menu = Integer.parseInt(sc.nextLine());
                switch (menu) {
                    case 1:     //로그인
                        user = login();
                        if (user == null)
                            continue;
                        break;
                    case 2:     //관리자 계정 생성
                        createAdmin();
                        break;
                    case 3:     //종료
                    default:
                        continue;
                }

                // 사용자 직책에 맞는 서비스 실행
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

    // 관리자 생성
    private void createAdmin() throws Exception {
        while(true){
            Scanner sc = new Scanner(System.in);
            System.out.println(Message.ADMIN_CREATE_MESSAGE);

            System.out.print(Message.ADMIN_CODE_INPUT);  //교원번호 입력
            String adminCode = sc.nextLine();

            System.out.print(Message.ADMIN_NAME_INPUT);  //이름 입력
            String name = sc.nextLine();

            System.out.print(Message.ADMIN_DEPARTMENT_INPUT);  //학과입력
            String department = sc.nextLine();

            System.out.print(Message.ADMIN_BIRTHDATE_INPUT); //생년월일 입력
            String birthDate = sc.nextLine();

            // 사용자가 입력한 관리자 정보로 AdminDTO 생성
            AdminDTO newAdmin = AdminDTO.builder()
                    .name(name)
                    .adminCode(adminCode)
                    .department(department)
                    .birthDate(birthDate)
                    .build();

            // 서버에게 관리자 생성 요청
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
                } else if (recvPt.getCode() == Protocol.T2_CODE_FAIL) { // 생성 실패한 경우 실패 메시지 출력
                    MessageDTO failMsg = (MessageDTO) recvPt.getObject();
                    System.out.println(Message.ADMIN_CREATE_FAIL);
                    System.out.println(failMsg);
                }
            }
        }
    }

    // 로그인
    private AccountDTO login() throws Exception {
        BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));
        System.out.print(Message.ID_INPUT);
        String id = userIn.readLine();
        System.out.print(Message.PW_INPUT);
        String pw = userIn.readLine();
        // 사용자가 입력한 id, pw 정보로 accountDTO 생성
        AccountDTO accountDTO = AccountDTO.builder()
                .id(id)
                .password(pw)
                .build();
        // 서버에게 accoutDTO를 담은 패킷으로 로그인 요청
        Protocol protocol = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_LOGIN);
        protocol.setObject(accountDTO);
        protocol.send(os);

        Protocol recv = new Protocol();
        recv.read(is);

        if (recv.getType() == Protocol.TYPE_RESPONSE) {
            if (recv.getCode() == Protocol.T2_CODE_SUCCESS)
                return (AccountDTO) recv.getObject();    // 어떤 사용자인지 return
            // 로그인 실패 한 경우
            else if (recv.getCode() == Protocol.T2_CODE_FAIL){
                MessageDTO failMsg = (MessageDTO) recv.getObject();
                System.out.println(failMsg);
            }
        }
        return null;
    }


    //서비스 생성
    private static EnrollmentService createService(AccountDTO account, InputStream is, OutputStream os) {
        if (account.getPosition().equals("ADMIN")) {  //관리자 서비스 생성
            return new AdminService(account, is, os);
        } else if (account.getPosition().equals("PROF")) { //교수 서비스 생성
            return new ProfessorService(account, is, os);
        } else if (account.getPosition().equals("STUD")) {  // 학생 서비스 생성
            return new StudentService(account, is, os);
        }
        return null;
    }
}
