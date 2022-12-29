import java.io.InputStream;
import java.util.Scanner;

/**
 * Wrapper class to System.out or reading from console
 */
public final class Console {
    Scanner input;

    /**
     * Constructor
     * @param iStream Stream to read from
     */
    public Console(InputStream iStream) {
        input = new Scanner(iStream);
    }

    /**
     * Writes message to console
     * @param message Message to write
     */
    public void write(String message) {
        System.out.print(message);
    }

    /**
     * Writes message to console and adds new line
     * @param message Message to write
     */
    public void writeLine(String message) {
        System.out.println(message);
    }

    /**
     * Reads int value if it can, otherwise returns false
     * @return SafeInt type
     */
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

    /**
     * Reads line from console
     * @return Line as string
     */
    public String readLine() {
        return input.nextLine();
    }
}
