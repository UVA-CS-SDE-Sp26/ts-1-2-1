public class TopSecret {

    public static void main(String[] args) {
        CommandLineInterface cli = new CommandLineInterface();
        CommandLineInterface.CliParseResult parsed = cli.parse(args);

        // If CLI parsing fails, print error and exit gracefully
        if (!parsed.isOk()) {
            System.out.println(parsed.getErrorMessage());
            return;
        }

        FileHandler fileHandler = new FileHandler();
        CommandLineInterface.CliRequest request = parsed.getRequest();

        // LIST MODE
        if (request.getMode() == CommandLineInterface.CliRequest.Mode.LIST) {
            ProgramControl control = new ProgramControl(fileHandler, null);
            System.out.print(control.handleArguments(new String[]{}));
            return;
        }

        // DISPLAY MODE
        String selection = request.getSelection();
        String keyArg = request.getKey(); // optional second CLI arg

        // Decide which cipher key file to load
        // - If user supplied a key arg: try that key
        // - Else: try default ./ciphers/key.txt
        String cipherPath = (keyArg != null) ? resolveCipherPath(keyArg) : "./ciphers/key.txt";

        Cipher cipher = loadCipherOrNull(cipherPath);

        // If user explicitly supplied a key but we couldn't load it, error out
        if (keyArg != null && cipher == null) {
            System.out.println("ERROR: Unable to decipher file with provided key.");
            return;
        }

        // If we have a cipher (default or alternate), we want ProgramControl to decipher.
        // Current ProgramControl.decipher logic is triggered only when the 'key' parameter is non-null.
        // So:
        // - If cipher exists AND no keyArg was given, we pass a dummy non-null key to trigger default deciphering.
        String keyForProgramControl = null;
        if (cipher != null) {
            keyForProgramControl = (keyArg != null) ? keyArg : "default";
        }

        ProgramControl control = new ProgramControl(fileHandler, cipher);

        if (keyForProgramControl == null) {
            System.out.print(control.handleArguments(new String[]{selection}));
        } else {
            System.out.print(control.handleArguments(new String[]{selection, keyForProgramControl}));
        }
    }

    /**
     * If the user passes something like "altkey.txt", we interpret it as "./ciphers/altkey.txt".
     * If they pass a path containing "/" or "\" we use it as given.
     */
    private static String resolveCipherPath(String keyArg) {
        if (keyArg.contains("/") || keyArg.contains("\\")) {
            return keyArg;
        }
        return "./ciphers/" + keyArg;
    }

    /**
     * Loads cipher mapping from a key file that contains:
     * line 1: actual letters
     * line 2: cipher match letters
     *
     * Returns null if file missing/invalid.
     *
     */
    private static Cipher loadCipherOrNull(String path) {
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