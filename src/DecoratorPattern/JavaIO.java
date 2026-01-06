package DecoratorPattern;

import java.io.*;

class LowerCaseInputStream extends FilterInputStream {

    public LowerCaseInputStream(InputStream in) {
        super(in);
    }

    public int read() throws IOException {
        int c = in.read();
        return (c == -1 ? c : Character.toLowerCase((char)c));
    }

    public int read(byte[] b, int offset, int len) throws IOException {
        int result = in.read(b, offset, len);
        System.out.print("Entered buffer read");
        for (int i = offset; i < offset+result; i++) {
            b[i] = (byte)Character.toLowerCase((char)b[i]);
        }
        return result;
    }
}

public class JavaIO {
    public static void main(String[] args) throws IOException {
        int c;
        InputStream in = null;
        try {
//            in =
//                    new LowerCaseInputStream(
//                            new BufferedInputStream(
//                                    new FileInputStream("/Users/lakshman.thirumurthi/Documents/Design-patterns/src/DecoratorPattern/test.txt")));

            in = new FileInputStream("/Users/lakshman.thirumurthi/Documents/Design-patterns/src/DecoratorPattern/test.txt");
            in = new BufferedInputStream(in);
            in = new LowerCaseInputStream(in);

            while((c = in.read()) >= 0) {
                System.out.print((char)c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) { in.close(); }
        }
        System.out.println();
        try (InputStream in2 =
                     new LowerCaseInputStream(
                             new BufferedInputStream(
                                     new FileInputStream("/Users/lakshman.thirumurthi/Documents/Design-patterns/src/DecoratorPattern/test.txt"))))
        {
            while((c = in2.read()) >= 0) {
                System.out.print((char)c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
