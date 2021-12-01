package network;

import infra.database.option.lecture.LectureOption;
import infra.database.option.lecture.ProfessorCodeOption;
import infra.dto.AccountDTO;
import infra.dto.LectureDTO;
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


        return pt;
    }

    // 개인 정보 조회 요청
    public void requestReadPersonalInfo(ProfessorDTO professorDTO) throws IOException, IllegalAccessException {
        Protocol pt = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_READ, Protocol.ENTITY_PROFESSOR, Protocol.READ_BY_ID);
        pt.setObject(professorDTO);
        pt.send(os);
    }

    // 개인정보 변경 요청
    public void requestUpdatePersonalInfo(ProfessorDTO dto) throws IllegalAccessException, IOException {
        Protocol pt = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_UPDATE, Protocol.ENTITY_PROFESSOR);
        pt.setObject(dto);
        pt.send(os);
    }

    // 비밀번호 변경 요청
    public void requestUpdateAccount(AccountDTO dto) throws IllegalAccessException, IOException {
        Protocol pt = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_UPDATE, Protocol.ENTITY_ACCOUNT);
        pt.setObject(dto);
        pt.send(os);
    }

    public void requestMyLectureList(){
        Protocol pt  = new Protocol(Protocol.TYPE_REQUEST,Protocol.T1_CODE_READ,Protocol.ENTITY_LECTURE);
    }

    public void requestAllLectureList() throws IOException {
        Protocol pt = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_READ, Protocol.ENTITY_LECTURE, Protocol.READ_ALL);
        pt.send(os);
    }

    public void requestLectureListByOption(LectureOption[] options) throws IllegalAccessException, IOException {
        Protocol pt = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_READ, Protocol.ENTITY_LECTURE, Protocol.READ_BY_OPTION);
        pt.setObjectArray(options);
        pt.send(os);
    }

    public void requestMyLecture(ProfessorDTO profDTO) throws IllegalAccessException, IOException {
        Protocol pt = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_READ, Protocol.ENTITY_LECTURE, Protocol.READ_BY_OPTION);
        LectureOption[] options = new LectureOption[]{
                new ProfessorCodeOption(profDTO.getProfessorCode())
        };
        pt.setObjectArray(options);
        pt.send(os);
    }

    public void requestReadRegisteringStd(LectureDTO lectureDTO) throws IOException, IllegalAccessException {
        Protocol pt = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_READ, Protocol.ENTITY_REGISTRATION);
        pt.setObject(lectureDTO);
        pt.send(os);
    }

    public void requestUpdateLectrue(LectureDTO selectedLecture) throws IllegalAccessException, IOException {
        Protocol pt = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_UPDATE, Protocol.ENTITY_LECTURE);
        pt.setObject(selectedLecture);
        pt.send(os);
    }

    public void requestLogout() throws IOException {
        Protocol pt = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_LOGOUT);
        pt.send(os);
    }


    // 개설 교과목 목록 조회 요청(옵션)

    // 강의 계획서 조회 요청

    // (본인) 강의 계획서 입력

    // (본인) 강의 계획서 수정

    // (본인) 수강 신청 학생 목록 조회

    // (본인) 시간표 조회

}
