package network;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Serializer {
    public static byte[] objectArrToBytes(Object[] objs) throws IllegalAccessException, IllegalArgumentException{
        if(objs.length==0){
            throw new IllegalArgumentException();
        }

        List<byte[]> byteList = new LinkedList<>();

        byte[] classNameBytes = new byte[32];
        String className = objs.getClass().getName().replace(";","");
        System.arraycopy(
                className.getBytes(StandardCharsets.UTF_8), 0,
                classNameBytes, 0, className.getBytes(StandardCharsets.UTF_8).length
        );
        byteList.add(classNameBytes);


        int allLength = 32;
        int count = 0;

        try{
            for(Object obj : objs){
                List<byte[]> tempByteList = new LinkedList<>();
                int objLength = 0;

                for(Field f : getAllFields(obj.getClass())){
                    byte[] fieldBytes = getBytesFrom(obj, f);
//                    System.out.println("name = " + f.getName());
//                    System.out.println("simpleType = " + f.getType());
//                    System.out.println("genericType = " + f.getGenericType());
                    byte[] fieldLength = intToBytes(fieldBytes.length);

                    tempByteList.add(fieldLength);
                    tempByteList.add(fieldBytes);
                    objLength+=fieldBytes.length+4;
                }
//            tempByteList.add(0, intToBytes(objLength+4));

                byte[] objBytes = new byte[objLength];
                int cursor = 0;
                for(byte[] b : tempByteList){
                    System.arraycopy(b, 0, objBytes, cursor, b.length);
                    cursor+=b.length;
                }

                count++;
                allLength+=objLength;
                byteList.add(objBytes);
            }
        }catch(NullPointerException e){
        }

//        byteList.add(0, intToBytes(allLength));
        byteList.add(1, intToBytes(count));

        byte[] result = new byte[allLength+4];
        int cursor = 0;
        for(byte[] b : byteList){
            System.arraycopy(b, 0, result, cursor, b.length);
            cursor+=b.length;
        }

        return result;
    }

    public static byte[] objectToBytes(Object obj) throws IllegalAccessException {
        List<byte[]> byteList = new LinkedList<>();

        byte[] classNameBytes = new byte[32];
        String className = obj.getClass().getName().replace(";","");
        System.arraycopy(
                className.getBytes(StandardCharsets.UTF_8), 0,
                classNameBytes, 0, className.getBytes(StandardCharsets.UTF_8).length
        );
        byteList.add(classNameBytes);

        int allLength = 32;

        try{
            for(Field f : getAllFields(obj.getClass())){
                byte[] fieldBytes = getBytesFrom(obj, f);
                byte[] fieldLength = intToBytes(fieldBytes.length);

                byteList.add(fieldLength);
                byteList.add(fieldBytes);
                allLength+=fieldBytes.length+4;
            }

        }catch(NullPointerException e){
        }

//        allLength += 4;
//        byteList.add(0, intToBytes(allLength));

        byte[] result = new byte[allLength];
        int cursor = 0;
        for(byte[] b : byteList){
            System.arraycopy(b, 0, result, cursor, b.length);
            cursor+=b.length;
        }

        return result;
    }

    public static List<Field> getAllFields(Class clazz){
        if(clazz==Object.class){
            return Collections.emptyList();
        }

        List<Field> result = new ArrayList<>(getAllFields(clazz.getSuperclass()));
        for(Field f : clazz.getDeclaredFields()){
            if(Modifier.isStatic(f.getModifiers())){
                continue;
            }

            result.add(f);
        }

        return result;
    }

    public static byte[] getBytesFrom(Object obj, Field f) throws IllegalAccessException {
        String typeName = f.getType().getSimpleName();
        f.setAccessible(true);

        if(typeName.contains("[")){
            typeName = "array";
        }

        //TODO : primitive 타입 추가필요
        switch(typeName){
            case "byte":
                return new byte[]{f.getByte(obj)};
            case "char":
                return charToBytes(f.getChar(obj));
            case "short":
                return shortToBytes(f.getShort(obj));
            case "int":
                return intToBytes(f.getInt(obj));
            case "long":
                return longToBytes(f.getLong(obj));
            case "String":
                try{
                    return new String((String)f.get(obj)).getBytes(StandardCharsets.UTF_8);
                }catch(NullPointerException e){
                    return new byte[0];
                }
            case "array":
                try{
                    return objectArrToBytes((Object[])f.get(obj));
                }catch(NullPointerException | IllegalArgumentException e){
                    return new byte[0];
                }
            default:
                try{
                    return objectToBytes(f.get(obj));
                }catch(NullPointerException e){
                    return new byte[0];
                }
        }
    }

    public static byte[] longToBytes(long l) {
        byte[] bytes = new byte[8];

        for(int i=0, shift=56; i<bytes.length; i++, shift-=8){
            bytes[i] = (byte) (l>>shift);
        }

        return bytes;
    }

    public static byte[] charToBytes(char c){
        byte[] bytes = new byte[2];

        for(int i=0, shift=8; i<bytes.length; i++, shift-=8){
            bytes[i] = (byte) (c>>shift);
        }

        return bytes;
    }

    public static byte[] shortToBytes(short s){
        byte[] bytes = new byte[2];

        for(int i=0, shift=8; i<bytes.length; i++, shift-=8){
            bytes[i] = (byte) (s>>shift);
        }

        return bytes;
    }

    public static byte[] intToBytes(int i){
        byte[] bytes = new byte[4];

        for(int j=0, shift=24; j<bytes.length; j++, shift-=8){
            bytes[j] = (byte) (i>>shift);
        }

        return bytes;
    }
}