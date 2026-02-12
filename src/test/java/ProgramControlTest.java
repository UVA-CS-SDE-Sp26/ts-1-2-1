import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

class ProgramControlTest {

    @Mock
    private FileHandler mockFileHandler;

    @InjectMocks
    private ProgramControl programControl;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listFilesNoArgs() {

        when(mockFileHandler.getFileList()).thenReturn(List.of("a.txt", "b.txt"));

        String result = programControl.handleArguments(new String[]{});

        assertTrue(result.contains("01"));
        assertTrue(result.contains("a.txt"));
        assertTrue(result.contains("02"));
        assertTrue(result.contains("b.txt"));

        verify(mockFileHandler).getFileList();
    }

    @Test
    void displayFileOneArg() {

        when(mockFileHandler.getFileList()).thenReturn(List.of("a.txt"));

        when(mockFileHandler.getFileContents("a.txt")).thenReturn("File Content");

        String result = programControl.handleArguments(new String[]{"01"});

        assertEquals("File Content", result);
        verify(mockFileHandler).getFileContents("a.txt");
    }

    @Test
    void twoArgsKey() {

        when(mockFileHandler.getFileList()).thenReturn(List.of("a.txt"));

        when(mockFileHandler.getFileContents("a.txt")).thenReturn("Decoded Text");

        String result = programControl.handleArguments(new String[]{"01", "key1"});

        assertEquals("Decoded Text", result);
        verify(mockFileHandler).getFileContents("a.txt");
    }

    @Test
    void tooManyArgs() {

        String result = programControl.handleArguments(new String[]{"01", "key1", "a"});

        assertTrue(result.toLowerCase().contains("error"));

        verifyNoInteractions(mockFileHandler);
    }

    //TODO: add more tests for the other methods later, currently WIP
}
