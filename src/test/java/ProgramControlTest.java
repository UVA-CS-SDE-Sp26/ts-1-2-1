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

    @Test
    void oneFile() {
        when(mockFileHandler.getFileList()).thenReturn(List.of("a.txt"));

        String result = programControl.handleArguments(new String[]{});

        assertTrue(result.contains("01 a.txt"));
    }

    @Test
    void twoFiles() {
        when(mockFileHandler.getFileList()).thenReturn(List.of("a.txt", "b.txt"));

        String result = programControl.handleArguments(new String[]{});

        assertTrue(result.contains("01 a.txt"));
        assertTrue(result.contains("02 b.txt"));
        verify(mockFileHandler).getFileList();
    }

    @Test
    void emptyList() {
        when(mockFileHandler.getFileList()).thenReturn(List.of());

        String result = programControl.handleArguments(new String[]{});

        assertTrue(result.toLowerCase().contains("no files"));
        verify(mockFileHandler).getFileList();
    }

    @Test
    void ManyFiles() {
        when(mockFileHandler.getFileList()).thenReturn(List.of("a.txt","b.txt","c.txt","d.txt","e.txt","f.txt","g.txt","h.txt","i.txt","j.txt"));

        String result = programControl.handleArguments(new String[]{});

        //check first and last
        assertTrue(result.contains("01 a.txt"));
        assertTrue(result.contains("10 j.txt"));
        verify(mockFileHandler).getFileList();
    }

    @Test
    void fileListNull() {
        when(mockFileHandler.getFileList()).thenReturn(null);

        String result = programControl.handleArguments(new String[]{});

        assertTrue(result.toLowerCase().contains("no files"));
        verify(mockFileHandler).getFileList();
    }

    @Test
    void selectionValid01() {
        when(mockFileHandler.getFileList()).thenReturn(List.of("a.txt", "b.txt"));
        when(mockFileHandler.getFileContents("a.txt")).thenReturn("Content A");

        String result = programControl.handleArguments(new String[]{"01"});

        assertEquals("Content A", result);
        verify(mockFileHandler).getFileContents("a.txt");
    }

    @Test
    void selectionValid02() {
        when(mockFileHandler.getFileList()).thenReturn(List.of("a.txt", "b.txt"));
        when(mockFileHandler.getFileContents("b.txt")).thenReturn("Content B");

        String result = programControl.handleArguments(new String[]{"02"});

        assertEquals("Content B", result);
        verify(mockFileHandler).getFileContents("b.txt");
    }

    @Test
    void selectionInvalidRange() {
        when(mockFileHandler.getFileList()).thenReturn(List.of("a.txt", "b.txt"));

        String result = programControl.handleArguments(new String[]{"03"});

        assertTrue(result.toLowerCase().contains("error"));
        verify(mockFileHandler, never()).getFileContents(any());
    }

    @Test
    void selectionInvalidInput() {
        when(mockFileHandler.getFileList()).thenReturn(List.of("a.txt", "b.txt"));

        String result = programControl.handleArguments(new String[]{"HI"});

        assertTrue(result.toLowerCase().contains("error"));
        verify(mockFileHandler, never()).getFileContents(any());
    }

    @Test
    void selectionZeroInvalid() {
        when(mockFileHandler.getFileList()).thenReturn(List.of("a.txt"));

        String result = programControl.handleArguments(new String[]{"00"});

        assertTrue(result.toLowerCase().contains("error"));
        verify(mockFileHandler).getFileList();
    }

    @Test
    void fileContentValidFile() {
        when(mockFileHandler.getFileList()).thenReturn(List.of("a.txt"));
        when(mockFileHandler.getFileContents("a.txt")).thenReturn("Text from file 01");

        String result = programControl.handleArguments(new String[]{"01"});

        assertEquals("Text from file 01", result);
        verify(mockFileHandler).getFileContents("a.txt");
    }

    @Test
    void fileContentMissingFile() {
        when(mockFileHandler.getFileList()).thenReturn(List.of("a.txt"));
        when(mockFileHandler.getFileContents("a.txt")).thenReturn(null);

        String result = programControl.handleArguments(new String[]{"01"});

        assertTrue(result.toLowerCase().contains("error"));
        verify(mockFileHandler).getFileContents("a.txt");
    }

    @Test
    void alternateKeyValid() {
        when(mockFileHandler.getFileList()).thenReturn(List.of("a.txt"));
        when(mockFileHandler.getFileContents("a.txt")).thenReturn("Decoded Text");

        String result = programControl.handleArguments(new String[]{"01", "key1"});

        assertEquals("Decoded Text", result);
        verify(mockFileHandler).getFileContents("a.txt");
    }

    @Test
    void alternateKeyInvalid() {
        when(mockFileHandler.getFileList()).thenReturn(List.of("a.txt"));
        when(mockFileHandler.getFileContents("a.txt")).thenReturn(null);

        String result = programControl.handleArguments(new String[]{"01", "not a key"});

        assertTrue(result.toLowerCase().contains("error"));
        verify(mockFileHandler).getFileContents("a.txt");
    }

    @Test
    void alternateKeyEmpty() {
        when(mockFileHandler.getFileList()).thenReturn(List.of("a.txt"));
        when(mockFileHandler.getFileContents("a.txt")).thenReturn("Text");

        String result = programControl.handleArguments(new String[]{"01", ""});

        assertEquals("Text", result);
    }
}