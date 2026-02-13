import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileHandler {
    protected Path directory;
    protected Path ciphers;

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

    /**
     * Loads cipher mapping from a key file that contains:
     * line 1: actual letters
     * line 2: cipher match letters
     * Returns null if file missing/invalid.
     */
    public Cipher readCipherKey(String path) {
        try {
                java.nio.file.Path p = java.nio.file.Paths.get(path);
                if (!java.nio.file.Files.exists(p)) return null;

                String raw = java.nio.file.Files.readString(p);

                String[] lines = raw.replace("\r\n", "\n").replace("\r", "\n").split("\n");
                if (lines.length < 2) return null;

                String actualLetters = lines[0];
                String cipherMatch = lines[1];

                if (actualLetters.length() != cipherMatch.length()) return null;

                return new Cipher(actualLetters, cipherMatch);

            } catch (Exception e) {
                return null;
            }
    }
}
