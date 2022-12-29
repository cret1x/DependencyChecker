import java.io.InputStream;
import java.util.Scanner;

public final class Console {
    Scanner input;

    public Console(InputStream iStream) {
        input = new Scanner(iStream);
    }

    public void write(String message) {
        System.out.print(message);
    }

    public void writeLine(String message) {
        System.out.println(message);
    }

    public SafeInt tryReadInt() {
        int value = -1;
        boolean success = true;
        try {
            value = Integer.parseInt(input.nextLine());
        } catch (Exception ignored) {
            success = false;
        }
        return new SafeInt(success, value);
    }

    public String readLine() {
        return input.nextLine();
    }
}
