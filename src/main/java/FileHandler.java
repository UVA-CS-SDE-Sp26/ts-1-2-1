import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileHandler {
    protected Path directory;

    /**
     * Constructs a `FileHandler` using the expected folder location.
     */
    public FileHandler() {
        this.directory = Paths.get("./data").normalize();
    }

    /**
     * @return A list of the files in the `data` directory
     */
    public List<String> getFileList() {
        try(var files = Files.list(this.directory)) {
            return files.map((path) -> path.getFileName().toString()).toList();
        } catch (IOException err) {
            return null;
        }
    }

    /**
     * Gets a file's contents from its name
     * @param file The name of a file in the
     * @return The ciphered contents of the file
     */
    public String getFileContents(String file) {
        try {
            return Files.readString(this.directory.resolve(file));
        } catch (IOException err) {
            return null;
        }
    }
}
