import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Resolver {
    private final Console console;
    private final String basePath;
    private final boolean isBoringMethod;
    private final String resultFile;
    private final String regex = "require ‘(.*)’";
    private final DirectedGraph graph;
    private final ArrayList<String> loops;

    /**
     * Main solution constructor
     * @param c Console instance
     */
    public Resolver(Console c) {
        console = c;
        console.write("Enter base path: ");
        basePath = console.readLine();
        console.write("Enter result file name: ");
        resultFile = concat(basePath, console.readLine());
        console.write("Enter number of method (See README for more details): ");
        var readValue = console.tryReadInt();
        while (!readValue.success() || readValue.value() != 0 && readValue.value() != 1) {
            console.writeLine("Incorrect option!");
            console.write("Enter number of method (See README for more details): ");
            readValue = console.tryReadInt();
        }
        isBoringMethod = readValue.value() == 0;
        loops = new ArrayList<>();
        graph = new DirectedGraph();
    }

    /**
     * Function to generate graph and check for loops + start writing result
     */
    public void resolve() {
        File baseFile = new File(basePath);
        if (!baseFile.exists()) {
            console.writeLine("Base path does not exist!");
            return;
        }
        if (baseFile.isFile()) {
            console.writeLine("Input path to dir, not a file!");
            return;
        }
        File[] files = baseFile.listFiles();
        if (files == null) {
            console.writeLine("Empty directory!");
            return;
        }
        traverseFiles(files);
        for (var file : graph.getVertices()) {
            parseFile(file);
        }
        if (!checkForLoops()) {
            console.writeLine("Found loops: ");
            for (var s : loops) {
                console.writeLine(s);
            }
            return;
        }
        for (var v: graph.getVertices()) {
            console.write(v.getPath() + " => [");
            for (var n: v.getAdjacencyList()) {
                console.write(n.getPath() + " [" + n.getAdjacencyList().size() + "], ");
            }
            console.writeLine("]");
        }
        HashSet<String> allDependencies = new HashSet<>();
        for (var v: graph.getVertices()) {
            for (var n: v.getAdjacencyList()) {
                allDependencies.add(n.getPath());
            }
        }
        if (isBoringMethod) {
            var l = graph.sort();
            console.writeLine("Sorted list:");
            for (var v : l) {
                console.writeLine(v.getPath());
            }
            console.writeLine("------------");
            createBoringResultFile(l);
        } else {
            for (var v: graph.getVertices()) {
                if (allDependencies.contains(v.getPath())) {
                    continue;
                }
                createResultFile(v);
            }
        }
    }

    /**
     * Creates file in a boring way - as said in task
     * @param list Sorted list of vertices
     */
    private void createBoringResultFile(List<Vertex> list) {
        try {
            File file = new File(resultFile);
            if (file.createNewFile()) {
                console.writeLine("Creating file: " + resultFile);
            } else {
                console.writeLine("File already exists: " + resultFile);
            }
            try (FileWriter writer = new FileWriter(resultFile)) {
                for (var v: list) {
                    File readFile = new File(v.getPath());
                    try (Scanner reader = new Scanner(readFile)) {
                        while (reader.hasNextLine()) {
                            String data = reader.nextLine();
                            writer.write(data + "\n");
                        }
                    }
                }
            }
        } catch (IOException e) {
            console.writeLine("Failed to create/open file!");
        }
    }

    /**
     * Creates result file based on top-independent vertex
     * @param v Base vertex
     */
    private void createResultFile(Vertex v) {
        try {
            File file = new File(resultFile);
            if (file.createNewFile()) {
                console.writeLine("Creating file: " + resultFile);
            } else {
                console.writeLine("File already exists: " + resultFile);
            }

            File mainFile = new File(v.getPath());
            try (Scanner reader = new Scanner(mainFile); FileWriter writer = new FileWriter(resultFile)) {
                int index = 0;
                while (reader.hasNextLine()) {
                    String data = reader.nextLine();
                    Pattern p = Pattern.compile(regex);
                    Matcher m = p.matcher(data);
                    if (m.matches()) {
                        writeOtherFileContent(v, writer, index);
                        index++;
                    } else {
                        writer.write(data + "\n");
                    }
                }
            }
        } catch (IOException e) {
            console.writeLine("Failed to create/open file!");
        }
    }

    /**
     * Wrapper to check for loops and save conflicts to array
     * @return False if found loops
     */
    private boolean checkForLoops() {
        var result = graph.hasCycle();
        boolean flag = !result.first();
        if (result.first()) {
            loops.add(result.second().first().getPath() + " with " + result.second().second().getPath());
        }
        return flag;
    }

    /**
     * Recursive function to replace 'require' string with file content
     * @param v Parent vertex
     * @param writer Writer object of result file
     * @param idx Index for regex matches
     */
    private void writeOtherFileContent(Vertex v, FileWriter writer, int idx) {
        Vertex dep = v.getAdjacencyList().get(idx);
        try (Scanner reader = new Scanner(new File(dep.getPath()))) {
            int index = 0;
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(data);
                if (m.matches()) {
                    writeOtherFileContent(dep, writer, index);
                    index++;
                } else {
                    writer.write(data + "\n");
                }
            }
        } catch (IOException e) {
            console.writeLine("Failed to create/open file!");
        }
    }

    /**
     * Traverse function to get all files in dirs and sub-dirs
     * @param files Array of entries in base directory
     */
    private void traverseFiles(File[] files) {
        for (var file : files) {
            if (file.isDirectory()) {
                traverseFiles(file.listFiles());
            } else {
                Vertex v = new Vertex(file.getPath());
                graph.addVertex(v);
            }
        }
    }

    /**
     * Combines to string with File separator
     * @param path String 1
     * @param file String 2
     * @return Combined string
     */
    private String concat(String path, String file) {
        return path + File.separator + file;
    }

    /**
     * Parses file and creates edges in graph based on dependencies
     * @param file File to parse
     */
    private void parseFile(Vertex file) {
        File openFile = new File(file.getPath());
        if (!openFile.canRead())
            return;
        try {
            try(Scanner fs = new Scanner(openFile)) {
                while (fs.hasNextLine()) {
                    String data = fs.nextLine();
                    Pattern p = Pattern.compile(regex);
                    Matcher m = p.matcher(data);
                    if (m.matches()) {
                        String path = m.group(1);
                        File testFile = new File(concat(basePath, path));
                        if (testFile.exists() && testFile.isFile()) {
                            var fromQ = graph.getVertices().stream().filter(v -> Objects.equals(v.getPath(), file.getPath())).findFirst();
                            var toQ = graph.getVertices().stream().filter(v -> Objects.equals(v.getPath(), testFile.getPath())).findFirst();
                            if (fromQ.isPresent() && toQ.isPresent()) {
                                Vertex from = fromQ.get();
                                Vertex to = toQ.get();
                                graph.addEdge(from, to);
                            }
                        }
                    }
                }
            }
        } catch (FileNotFoundException ignored) {}
    }
}
