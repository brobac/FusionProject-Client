package network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StudentProtocolService {
    private InputStream is;
    private OutputStream os;

    public StudentProtocolService(InputStream is, OutputStream os) {
        this.is = is;
        this.os = os;
    }

    // 요청에 대한 응답
    public Protocol response() throws Exception {
        Protocol pt = new Protocol();
        pt.read(is);

        if (pt.getType() == Protocol.TYPE_RESPONSE) {
            if (pt.getCode() == Protocol.T2_CODE_SUCCESS) {  // 조회 성공
                return pt;
            }
        }
        return null;  // 조회 실패
    }

    // 개인 정보 조회 요청
    public void requestReadPersonalInfo() throws IOException {
        Protocol pt = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_READ, Protocol.ENTITY_ACCOUNT);
        pt.send(os);
    }

    // 개인정보 변경 요청
    public void requestUpdatePersonalInfo(Object dto) throws IllegalAccessException, IOException {
        Protocol pt = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_UPDATE, Protocol.ENTITY_STUDENT);
        pt.setObject(dto);
        pt.send(os);
    }

    // 비밀번호 변경 요청
    public void requestUpdateAccount(Object dto) throws IllegalAccessException, IOException {
        Protocol pt = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_UPDATE, Protocol.ENTITY_ACCOUNT);
        pt.setObject(dto);
        pt.send(os);
    }

    // 수강신청기간 조회 요청
    public void requestRegisteringReriod() throws IOException {
        Protocol pt = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_READ, Protocol.ENTITY_REGIS_PERIOD);
        pt.send(os);
    }


    // 수강신청 요청
    public void requestRegistering(Object dto) throws IllegalAccessException, IOException {
        Protocol pt = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_CREATE, Protocol.ENTITY_REGISTRATION);
        pt.setObject(dto);
        pt.send(os);
    }

    // 수강신청 취소 요청
    public void requestDeleteRegistering(Object dto) throws IllegalAccessException, IOException {
        Protocol pt = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_DELETE, Protocol.ENTITY_REGISTRATION);
        pt.setObject(dto);
        pt.send(os);
    }

    // 수강신청 목록 요청 (내꺼)
    public void requestReadRegistering() throws IOException {
        Protocol pt = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_READ, Protocol.ENTITY_REGISTRATION);
        pt.send(os);
    }

    // 개설 교과목 조회 요청 (전학년)
    public void requestAllLectureList() throws IOException  {
        Protocol pt = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_READ, Protocol.ENTITY_LECTURE);
        pt.send(os);
    }

}