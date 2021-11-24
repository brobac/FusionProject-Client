package network;

import java.io.*;
import java.nio.ByteBuffer;

public class Protocol {

    public static final int UNDEFINED  = 0;
    // TYPE
    public static final int TYPE_REQUEST = 1;
    public static final int TYPE_RESPONSE = 2;
    // TYPE_REQUEST_CODE
    public static final int T1_CODE_CREATE = 1;
    public static final int T1_CODE_READ = 2;
    public static final int T1_CODE_UPDATE = 3;
    public static final int T1_CODE_DELETE = 4;
    public static final int T1_CODE_LOGIN = 5;
    public static final int T1_CODE_LOGOUT = 6;
    // TYPE_RESPONSE_CODE
    public static final int T2_CODE_SUCCESS = 1;
    public static final int T2_CODE_FAIL = 2;
    // ENTITY
    public static final int ENTITY_ACCOUNT = 1;
    public static final int ENTITY_COURSE = 2;
    public static final int ENTITY_LECTURE = 3;
    public static final int ENTITY_REGIS_PERIOD= 4;
    public static final int ENTITY_PLANNER_PERIOD = 5;
    public static final int ENTITY_REGISTRATION = 6;
    public static final int ENTITY_PLANNER = 7;
    public static final int ENTITY_PROF_TIMETABLE = 8;
    public static final int ENTITY_REGIS_STUD_LIST = 9;
    public static final int ENTITY_STUD_TIMETABLE = 10;

    // LENGTH
    public static final int LEN_HEADER = 7;
    public static final int LEN_TYPE = 1;
    public static final int LEN_CODE = 1;
    public static final int LEN_ENTITY = 1;
    public static final int LEN_BODYLENGTH = 4;

    private byte type;
    private byte code;
    private byte entity;
    private int bodyLength;
    private byte[] body;

    public Protocol() {
        this(UNDEFINED, UNDEFINED, UNDEFINED);
    }

    public Protocol(int type) {
        this(type, UNDEFINED, UNDEFINED);
    }

//    public Protocol(int type, int entity) {
//        this(type, UNDEFINED, entity);
//    }

    public Protocol(int type, int code, int entity) {
        setType(type);
        setCode(code);
        setEntity(entity);
        setBodyLength(0);
    }

    public byte getType() {
        return type;
    }

    public void setType(int type) {
        this.type = (byte) type;
    }

    public byte getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = (byte) code;
    }

    public byte getEntity() {
        return entity;
    }

    public void setEntity(int entity) {
        this.entity = (byte) entity;
    }

    public int getBodyLength() {
        return bodyLength;
    }

    private void setBodyLength(int bodyLength) {
        this.bodyLength = bodyLength;
    }

    public Object getBody() {
        return deserialize(body);
    }

    public void setBody(Object body) {
        byte[] serializedObject = serialize(body);
        this.body = serializedObject;
        setBodyLength(serializedObject.length);
    }

    // 현재 header와 body로 패킷을 생성하여 리턴 - 패킷 전송시 사용
    public byte[] getPacket() {
        byte[] packet = new byte[LEN_HEADER + getBodyLength()];
        packet[0] = getType();          // 타입 지정
        packet[LEN_TYPE] = getCode();   // 코드 지정
        packet[LEN_TYPE + LEN_CODE] = getEntity(); // 엔티티 지정
        // 바디길이의 길이 지정
        System.arraycopy(intToByte(getBodyLength()), 0, packet, LEN_TYPE + LEN_CODE + LEN_ENTITY, LEN_BODYLENGTH);
        // 바디 담기
        if (getBodyLength() > 0) {
            System.arraycopy(body, 0, packet, LEN_HEADER, getBodyLength());
        }
        return packet;
    }

    public void setPacketHeader(byte[] packet) {
        byte[] data;
        setType(packet[0]);
        setCode(packet[LEN_TYPE]);
        setEntity(packet[LEN_TYPE + LEN_CODE]);
        data = new byte[LEN_BODYLENGTH];
        System.arraycopy(packet, LEN_TYPE + LEN_CODE + LEN_ENTITY, data, 0, LEN_BODYLENGTH);
        setBodyLength(byteToInt(data));
    }

    public void setPacketBody(byte[] packet) {
        byte[] data;
        if (getBodyLength() > 0) {
            data = new byte[getBodyLength()];
            System.arraycopy(packet, 0, data, 0, getBodyLength());
            setBody(deserialize(data));
        }
    }

    private byte[] serialize(Object o) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(o);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Object deserialize(byte[] b) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(b);
            ObjectInputStream ois = new ObjectInputStream(bais);
            Object ob = ois.readObject();
            return ob;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] intToByte(int i) {
        return ByteBuffer.allocate(Integer.SIZE / 8).putInt(i).array();
    }

    private int byteToInt(byte[] b) {
        return ByteBuffer.wrap(b).getInt();
    }


}

