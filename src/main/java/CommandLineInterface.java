import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Team Member A (CLI):
 * Parses and validates command-line arguments and returns a request or an error.
 */
public final class CommandLineInterface {

    public static final String USAGE = "Usage: java topsecret [NN] [KEY]";
    private static final Pattern TWO_DIGITS = Pattern.compile("^\\d{2}$");

    public CliParseResult parse(String[] args) {
        if (args == null) {
            return CliParseResult.error("ERROR: Invalid arguments.\n" + USAGE);
        }

        if (args.length == 0) {
            return CliParseResult.ok(CliRequest.list());
        }

        if (args.length == 1 || args.length == 2) {
            String selection = args[0];
            if (!isTwoDigits(selection)) {
                return CliParseResult.error("ERROR: File selection must be two digits (e.g., 01).\n" + USAGE);
            }

            String key = (args.length == 2) ? args[1] : null;
            return CliParseResult.ok(CliRequest.display(selection, key));
        }

        return CliParseResult.error("ERROR: Invalid arguments.\n" + USAGE);
    }

    private boolean isTwoDigits(String s) {
        return s != null && TWO_DIGITS.matcher(s).matches();
    }

    // --- Data types returned to ProgramControl (Team C) ---

    public static final class CliRequest {
        public enum Mode { LIST, DISPLAY }

        private final Mode mode;
        private final String selection; // only for DISPLAY
        private final String key;       // optional, only for DISPLAY

        private CliRequest(Mode mode, String selection, String key) {
            this.mode = Objects.requireNonNull(mode, "mode");
            this.selection = selection;
            this.key = key;
        }

        public static CliRequest list() {
            return new CliRequest(Mode.LIST, null, null);
        }

        public static CliRequest display(String selection, String key) {
            return new CliRequest(Mode.DISPLAY, selection, key);
        }

        public Mode getMode() {
            return mode;
        }

        /** Returns the 2-digit selection (e.g., "01") or null if mode is LIST. */
        public String getSelection() {
            return selection;
        }

        /** Returns the key argument or null if not provided / not applicable. */
        public String getKey() {
            return key;
        }
    }

    public static final class CliParseResult {
        private final CliRequest request;
        private final String errorMessage;

        private CliParseResult(CliRequest request, String errorMessage) {
            this.request = request;
            this.errorMessage = errorMessage;
        }

        public static CliParseResult ok(CliRequest request) {
            return new CliParseResult(Objects.requireNonNull(request, "request"), null);
        }

        public static CliParseResult error(String errorMessage) {
            return new CliParseResult(null, Objects.requireNonNull(errorMessage, "errorMessage"));
        }

        public boolean isOk() {
            return request != null;
        }

        public CliRequest getRequest() {
            return request;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }
}
