import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Resolver {
    Console console;
    String basePath;
    String resultFile;
    final String regex = "require ‘(.*)’";
    DirectedGraph graph;
    ArrayList<String> loops;

    public Resolver(Console c) {
        console = c;
        console.write("Enter base path: ");
        basePath = console.readLine();
        console.write("Enter result file name: ");
        resultFile = concat(basePath, console.readLine());
        loops = new ArrayList<>();
        graph = new DirectedGraph();
    }

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
        for (var v: graph.getVertices()) {
            if (allDependencies.contains(v.getPath())) {
                continue;
            }
            createResultFile(v);
        }
    }

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

    private boolean checkForLoops() {
        var result = graph.hasCycle();
        boolean flag = !result.first();
        if (result.first()) {
            loops.add(result.second().first().getPath() + " with " + result.second().second().getPath());
        }
        return flag;
    }

    private void writeOtherFileContent(Vertex v, FileWriter writer, int idx) {
        console.writeLine("Recursive call from: " + v.getPath());
        Vertex dep = v.getAdjacencyList().get(idx);
        console.writeLine("Dep file: " + dep.getPath());
        if (dep.getAdjacencyList().isEmpty()) {
            console.writeLine("Empty dep file" + dep.getPath());
            try (Scanner reader = new Scanner(new File(dep.getPath()))) {
                while (reader.hasNextLine()) {
                    writer.write(reader.nextLine() + "\n");
                }
            } catch (IOException e) {
                console.writeLine("Failed to create/open file!");
            }
        } else {
            try (Scanner reader = new Scanner(new File(dep.getPath()))) {
                int index = 0;
                while (reader.hasNextLine()) {
                    String data = reader.nextLine();
                    Pattern p = Pattern.compile(regex);
                    Matcher m = p.matcher(data);
                    if (m.matches()) {
                        console.writeLine("Match! => " + dep.getPath());
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
    }

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

    private String concat(String path, String file) {
        return path + File.separator + file;
    }

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
        } catch (FileNotFoundException ignored) {

        }
    }
}
