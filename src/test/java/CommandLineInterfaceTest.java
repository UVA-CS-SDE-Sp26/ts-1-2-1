import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommandLineInterfaceTest {

    private final CommandLineInterface cli = new CommandLineInterface();

    @Test
    void parse_noArgs_returnsListRequest() {
        var result = cli.parse(new String[]{});
        assertTrue(result.isOk());
        assertEquals(CommandLineInterface.CliRequest.Mode.LIST, result.getRequest().getMode());
        assertNull(result.getRequest().getSelection());
        assertNull(result.getRequest().getKey());
    }

    @Test
    void parse_oneArg_twoDigits_returnsDisplayRequest_withoutKey() {
        var result = cli.parse(new String[]{"01"});
        assertTrue(result.isOk());
        assertEquals(CommandLineInterface.CliRequest.Mode.DISPLAY, result.getRequest().getMode());
        assertEquals("01", result.getRequest().getSelection());
        assertNull(result.getRequest().getKey());
    }

    @Test
    void parse_twoArgs_twoDigits_returnsDisplayRequest_withKey() {
        var result = cli.parse(new String[]{"02", "altKey.txt"});
        assertTrue(result.isOk());
        assertEquals(CommandLineInterface.CliRequest.Mode.DISPLAY, result.getRequest().getMode());
        assertEquals("02", result.getRequest().getSelection());
        assertEquals("altKey.txt", result.getRequest().getKey());
    }

    @Test
    void parse_invalidSelection_notTwoDigits_returnsError() {
        var result = cli.parse(new String[]{"2"});
        assertFalse(result.isOk());
        assertNotNull(result.getErrorMessage());
        assertTrue(result.getErrorMessage().contains("two digits"));
    }

    @Test
    void parse_tooManyArgs_returnsError() {
        var result = cli.parse(new String[]{"01", "k", "extra"});
        assertFalse(result.isOk());
        assertNotNull(result.getErrorMessage());
        assertTrue(result.getErrorMessage().contains("Usage"));
    }

    @Test
    void parse_nullArgs_returnsError() {
        var result = cli.parse(null);
        assertFalse(result.isOk());
        assertNotNull(result.getErrorMessage());
    }
}
