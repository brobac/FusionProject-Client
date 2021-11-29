package network;

import infra.dto.ProfessorDTO;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ProfProtocolService {
    private InputStream is;
    private OutputStream os;

    public ProfProtocolService(InputStream is, OutputStream os) {
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
    public void requestReadPersonalInfo(ProfessorDTO professorDTO) throws IOException, IllegalAccessException {
        Protocol pt = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_READ, Protocol.ENTITY_PROFESSOR, Protocol.READ_BY_ID);
        pt.setObject(professorDTO);
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

    public void requestMyLectureList(){
        Protocol pt  = new Protocol(Protocol.TYPE_REQUEST,Protocol.T1_CODE_READ,Protocol.ENTITY_LECTURE);
    }


    // 개설 교과목 목록 조회 요청(옵션)

    // 강의 계획서 조회 요청

    // (본인) 강의 계획서 입력

    // (본인) 강의 계획서 수정

    // (본인) 수강 신청 학생 목록 조회

    // (본인) 시간표 조회

}
