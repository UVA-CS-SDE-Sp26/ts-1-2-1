import java.util.List;

public class ProgramControl {
    private FileHandler fileHandler;
    private Cipher cipher;

    public ProgramControl(FileHandler fileHandler, Cipher cipher) {
        this.fileHandler = fileHandler;
        this.cipher = cipher;
    }

    //determine which operation to perform based on number and format of arguments
    public String handleArguments(String[] args) {
        if (args.length == 0) {
            return listFiles();
        }
        if (args.length == 1) {
            return displayFile(args[0], null);
        }
        if (args.length == 2) {
            return displayFile(args[0], args[1]);
        }
        return "ERROR: Invalid argument count.";
    }

    //requests and lists formatted file list
    private String listFiles() {
        List<String> files = fileHandler.getFileList();
        if (files == null || files.isEmpty()) {
            return "No files available.";
        }

        return formatFileList(files);
    }

    //formats file names into numbered output for listing
    private String formatFileList(List<String> files) {
        String output = "";
        for (int i = 1; i <= files.size(); i++) {
            if (i < 10) {
                output += "0";
                output += i;
            }
            else {
                output += i;
            }
            output += " ";
            output += files.get(i-1);
            output += "\n";
        }
        return output;
    }

    //requests and displays contents of files and applies cipher if necessary
    private String displayFile(String selection, String key) {
        if (!selection.matches("\\d{2}")) {
            return "ERROR: Selection must be two digits.";
        }

        List<String> files = fileHandler.getFileList();
        String filename = mapSelectionToFile(selection, files);
        if (filename == null) {
            return "ERROR: No file found.";
        }

        String content = fileHandler.getFileContents(filename);

        if (content == null) {
            return "ERROR: No file content found.";
        }
        if (key != null && cipher != null) {
            content = cipher.decipher(content);
        }
        return content;
    }

    //match two-digit selection number to a filename
    private String mapSelectionToFile(String selection, List<String> files) {
        int index = Integer.parseInt(selection) - 1;
        if (index < 0 || index >= files.size()) {
            return null;
        }
        return files.get(index);
    }
}
