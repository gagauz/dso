package dso.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class StreamUtils {

    private StreamUtils() {
    }

    public static interface OutputStreamOwner {
        OutputStream getOutputStream();
    }

    public static interface InputStreamOwner {
        InputStream getInputStream();
    }

    public static void writeString(OutputStream out, String string) throws IOException {
        out.write(string.getBytes());
        out.flush();
    }

    public static void writeBytes(OutputStream out, byte[] data) throws IOException {
        out.write(data);
        out.flush();
    }

    public static void writeObject(OutputStream out, Object object) throws IOException {
        ObjectOutputStream oi = new ObjectOutputStream(out);
        oi.writeUnshared(object);
        out.flush();
    }

    public static String readString(InputStream in) {
        try {
            byte[] bytes = new byte[1024];
            int r = in.read(bytes);
            if (r >= 0) {
                return new String(bytes, 0, r);
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Object readObject(InputStream in) {
        try {
            ObjectInputStream ois = new ObjectInputStream(in);
            return ois.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
