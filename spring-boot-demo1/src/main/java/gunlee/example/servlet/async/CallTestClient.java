package gunlee.example.servlet.async;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 2. 9.
 */
public class CallTestClient {

    public static void main(String[] args) throws InterruptedException, IOException {

        String url = "http://localhost:8080/asyncservlet";

        Socket so = new Socket("localhost", 8080);
        OutputStream out =  so.getOutputStream();
        InputStream in = so.getInputStream();

        PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        pw.println("GET /asyncservlet HTTP/1.1");
        pw.println("host: localhost");
        pw.println("connection: close");
        pw.println();
        pw.flush();

        while (true) {
            String line = br.readLine();
            System.out.println(line);
        }
    }
}
