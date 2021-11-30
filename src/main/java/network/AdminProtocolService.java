package network;

import infra.dto.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AdminProtocolService {
    private InputStream is;
    private OutputStream os;

    public AdminProtocolService(InputStream is, OutputStream os) {
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

    // 관리자 계정 생성 요청
    public void reqCreateAdminAccount(Object data) throws IllegalAccessException, IOException {
        Protocol pt = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_CREATE, Protocol.ENTITY_ADMIN);
        pt.setObject(data);
        pt.send(os);
        ;
    }

    // 교수 계정 생성 요청
    public void reqCreateProfAccount(ProfessorDTO professorDTO) throws IllegalAccessException, IOException {
        Protocol pt = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_CREATE, Protocol.ENTITY_PROFESSOR);
        pt.setObject(professorDTO);
        pt.send(os);
        ;
    }

    // 학생 계정 생성 요청
    public void reqCreateStudAccount(StudentDTO studentDTO) throws IllegalAccessException, IOException {
        Protocol pt = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_CREATE, Protocol.ENTITY_STUDENT);
        pt.setObject(studentDTO);
        pt.send(os);
        ;
    }

    public void reqReadCourse(CourseDTO courseDTO) throws IllegalAccessException, IOException {
        Protocol pt = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_READ, Protocol.ENTITY_COURSE, Protocol.READ_BY_ID);
        pt.setObject(courseDTO);
        pt.send(os);
        ;
    }

    // 교과목 생성 요청
    public void reqCreateCourse(CourseDTO courseDTO) throws IllegalAccessException, IOException {
        Protocol pt = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_CREATE, Protocol.ENTITY_COURSE);
        pt.setObject(courseDTO);
        pt.send(os);
        ;
    }

    // 교과목 수정 요청
    public void reqUpdateCourse(CourseDTO courseDTO) throws IllegalAccessException, IOException {
        Protocol pt = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_UPDATE, Protocol.ENTITY_COURSE);
        pt.setObject(courseDTO);
        pt.send(os);
        ;
    }

    // 교과목 삭제 요청
    public void reqDeleteCourse(CourseDTO courseDTO) throws IllegalAccessException, IOException {
        Protocol pt = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_DELETE, Protocol.ENTITY_COURSE);
        pt.setObject(courseDTO);
        pt.send(os);
        ;
    }

    // 개설 교과목 생성 요청
    public void reqCreateLecture(LectureDTO lectureDTO) throws IllegalAccessException, IOException {
        Protocol pt = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_CREATE, Protocol.ENTITY_LECTURE);
        pt.setObject(lectureDTO);
        pt.send(os);
        ;
    }

    // 개설 교과목 삭제 요청
    public void reqDeleteLecture(Object data) throws IllegalAccessException, IOException {
        Protocol pt = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_DELETE, Protocol.ENTITY_LECTURE);
        pt.setObject(data);
        pt.send(os);
        ;
    }


    // 강의계획서 입력 기간 설정 요청
    public void reqCreatePlannerPeriod(PeriodDTO periodDTO) throws IllegalAccessException, IOException {
        Protocol pt = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_CREATE, Protocol.ENTITY_PLANNER_PERIOD);
        pt.setObject(periodDTO);
        pt.send(os);
        ;
    }

    public void reqReadPlannerPeriod() throws IOException {
        Protocol pt = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_READ, Protocol.ENTITY_PLANNER_PERIOD, Protocol.READ_ALL);
        pt.send(os);
    }


    //수강신청기간 조회
    public void reqReadRegisteringPeriod() throws IOException {
        Protocol pt = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_READ, Protocol.ENTITY_REGIS_PERIOD, Protocol.READ_ALL);
        pt.send(os);
    }


    // 학년별 수강 신청 기간 설정 요청
    public void reqCreateRegisteringPeriod(RegisteringPeriodDTO registeringPeriodDTO) throws IllegalAccessException, IOException {
        Protocol pt = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_CREATE, Protocol.ENTITY_REGIS_PERIOD);
        pt.setObject(registeringPeriodDTO);
        pt.send(os);
        ;
    }

    // 교수 정보 조회 요청
    public void reqReadProfessorInfo(Object data) throws IllegalAccessException, IOException {
        Protocol pt = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_READ, Protocol.ENTITY_PROFESSOR);
        pt.setObject(data);
        pt.send(os);
        ;
    }

    // 학생 정보 조회 요청
    public void reqReadStudentInfo(Object data) throws IllegalAccessException, IOException {
        Protocol pt = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_READ, Protocol.ENTITY_STUDENT);
        pt.setObject(data);
        pt.send(os);
        ;
    }

    // 개설 교과목 정보 조회 요청
    public void reqReadLectureInfo(Object data) throws IllegalAccessException, IOException {
        Protocol pt = new Protocol(Protocol.TYPE_REQUEST, Protocol.T1_CODE_READ, Protocol.ENTITY_LECTURE);
        pt.setObject(data);
        pt.send(os);
        ;
    }
}
