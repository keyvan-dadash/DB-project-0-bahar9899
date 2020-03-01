package IOHelper;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class ReadLineHelper {

    public static String readLine(long lineNumber, String path) throws IOException {
        Stream<String> alllines = Files.lines(Paths.get(path));
        return alllines.skip(lineNumber - 1).findFirst().get();
    }
}
