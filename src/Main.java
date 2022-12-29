public class Main {
    /**
     * Program entry point
     * @param args Command line args
     */
    public static void main(String[] args) {
        Console console = new Console(System.in);
        Resolver res = new Resolver(console);
        res.resolve();
    }
}