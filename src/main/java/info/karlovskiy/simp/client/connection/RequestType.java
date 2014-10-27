package info.karlovskiy.simp.client.connection;

/**
 * Here will be javadoc
 *
 * @author karlovskiy
 * @since 1.0, 10/27/14
 */
public enum RequestType {

    CONNECT(0),
    DISCONNECT(1),
    MESSAGE(2);

    private byte code;

    private RequestType(int code) {
        this.code = (byte) code;
    }

    public byte getCode() {
        return code;
    }
}
