import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Resolver {
    Console console;
    String basePath;
    HashMap<String, String> graph;
    ArrayList<Pair<String, String>> allFiles;
    ArrayList<String> loops;

    public Resolver(Console c) {
        console = c;
        console.write("Enter base path: ");
        basePath = console.readLine();
        allFiles = new ArrayList<>();
        loops = new ArrayList<>();
        graph = new HashMap<>();
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
        for (var file: allFiles) {
            parseFile(file);
        }
        if (!checkForLoops()) {
            console.writeLine("Found loops: ");
            for (var s: loops) {
                console.writeLine(s);
            }
        }
    }

    private boolean checkForLoops() {
        for(var k: graph.keySet()) {
            console.writeLine(k + " => " + graph.get(k));
        }
        return false;
    }

    private void traverseFiles(File[] files) {
        for (var file: files) {
            if (file.isDirectory()) {
                traverseFiles(file.listFiles());
            } else {
                allFiles.add(new Pair<>(file.getParent(), file.getName()));
            }
        }
    }

    private String concat(String path, String file) {
        return path + File.separator + file;
    }

    private void parseFile(Pair<String, String> file) {
        String openPath = concat(file.first(), file.second());
        File openFile = new File(openPath);
        if (!openFile.canRead())
            return;
        try {
            Scanner fs = new Scanner(openFile);
            while (fs.hasNextLine()) {
                String data = fs.nextLine();
                Pattern p = Pattern.compile("require ‘(.*)’");
                Matcher m = p.matcher(data);
                if (m.matches()) {
                    String path = m.group(1);
                    File testFile = new File(concat(basePath, path));
                    if (testFile.exists() && testFile.isFile()) {
                        graph.put(openPath, testFile.getPath());
                    }
                }
            }
        } catch (FileNotFoundException ignored) {

        }
    }
}
