package info.karlovskiy.simp.client.connection;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

/**
 * Here will be javadoc
 *
 * @author karlovskiy
 * @since 1.0, 10/25/14
 */
public class SIMPConnection implements Closeable {

    public static final String ENCODING = "UTF-8";

    private final Socket socket;
    private final InputStream in;
    private final OutputStream out;

    public SIMPConnection(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedInputStream(socket.getInputStream());
        this.out = new BufferedOutputStream(socket.getOutputStream());
    }

    public void writeConnect(String user) throws IOException {
        byte[] header = new byte[]{1, RequestType.CONNECT.getCode()};
        out.write(header);
        byte[] buff = user.getBytes(ENCODING);
        out.write(buff.length);
        out.write(buff);
        out.flush();
    }

    public void writeDisconnect() throws IOException {
        byte[] header = new byte[]{1, RequestType.DISCONNECT.getCode()};
        out.write(header);
        out.flush();
    }

    public void writeMessage(String user, String message) throws IOException {
        byte[] header = new byte[]{1, RequestType.MESSAGE.getCode()};
        out.write(header);
        byte[] usr = user.getBytes(ENCODING);
        out.write(usr.length);
        out.write(usr);
        byte[] msg = message.getBytes(ENCODING);
        byte[] msgl = ByteBuffer.allocate(4).putInt(msg.length).array();
        out.write(msgl);
        out.write(msg);
        out.flush();
    }

    public ResponseType readResponse() throws IOException {
        byte[] header = new byte[2];
        int bytesRead = 0;
        while (bytesRead != header.length) {
            int read = in.read(header, bytesRead, header.length - bytesRead);
            if (read == -1) {
                throw new IOException("Error reading header, stream closed");
            }
            bytesRead += read;
        }
        if (header[0] != 1) {
            throw new IOException("Unsupported protocol version");
        }
        byte code = header[1];
        ResponseType responseType = ResponseType.valueOf(code);
        if (responseType == null) {
            throw new IOException("Null response type: " + code);
        }
        return responseType;
    }

    public List<String> readConnectSuccessfully() throws IOException {
        byte[] buff = new byte[2];
        int bytesRead = 0;
        while (bytesRead != buff.length) {
            int read = in.read(buff, bytesRead, buff.length - bytesRead);
            if (read == -1) {
                throw new IOException("Error reading users list length, stream closed");
            }
            bytesRead += read;
        }
        short ull = ByteBuffer.wrap(buff).getShort();
        buff = new byte[ull];
        bytesRead = 0;
        while (bytesRead != buff.length) {
            int read = in.read(buff, bytesRead, buff.length - bytesRead);
            if (read == -1) {
                throw new IOException("Error reading users list, stream closed");
            }
            bytesRead += read;
        }
        String message = new String(buff, ENCODING);
        List<String> users = Arrays.asList(message.split("[\\s,]+"));
        return users;
    }

    public String readUser() throws IOException {
        int len = in.read();
        if (len == -1) {
            throw new IOException("Error reading user length");
        }
        byte[] buff = new byte[len];
        int bytesRead = 0;
        while (bytesRead != buff.length) {
            int read = in.read(buff, bytesRead, buff.length - bytesRead);
            if (read == -1) {
                throw new IOException("Error reading user, stream closed");
            }
            bytesRead += read;
        }
        String user = new String(buff, ENCODING);
        return user;
    }

    public String readMessage() throws IOException {
        byte[]buff = new byte[4];
        int bytesRead = 0;
        while (bytesRead != buff.length) {
            int read = in.read(buff, bytesRead, buff.length - bytesRead);
            if (read == -1) {
                throw new IOException("Error reading message length, stream closed");
            }
            bytesRead += read;
        }
        int msgl = ByteBuffer.wrap(buff).getInt();
        byte[] msg = new byte[msgl];
        bytesRead = 0;
        while (bytesRead != msg.length) {
            int read = in.read(msg, bytesRead, msg.length - bytesRead);
            if (read == -1) {
                throw new IOException("Error reading message, stream closed");
            }
            bytesRead += read;
        }
        String message = new String(msg, ENCODING);
        return message;
    }

    public int readError() throws IOException {
        int errorCode = in.read();
        if (errorCode == -1) {
            throw new IOException("Error reading error code");
        }
        return errorCode;
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }
}
