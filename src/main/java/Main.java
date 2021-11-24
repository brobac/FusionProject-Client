import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length < 2) System.out.println("사용법 : java Main 주소 포트번호");
        else {
            String address = args[0];
            int port = Integer.parseInt(args[1]);
            EnrollmentProgram.run(address, port);
        }
    }
}
