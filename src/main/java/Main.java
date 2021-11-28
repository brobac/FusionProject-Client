import controller.EnrollmentProgram;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main {
    public static void main(String[] args) {
        String address = "localhost";
        int port = 3000;
        try {
            Socket socket = new Socket(address, port);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            EnrollmentProgram program = new EnrollmentProgram(is, os);
            program.run();
            socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
