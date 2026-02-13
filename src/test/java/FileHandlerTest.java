import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileHandlerTest {
    @Mock
    private Path mockDataDir = Paths.get("./data").normalize();

    @InjectMocks
    private FileHandler fileHandler = new FileHandler();

    static MockedStatic<Files> filesMock = mockStatic(Files.class);

    @Test
    void getFileList() {
        FileHandler fileHandler = new FileHandler();

        Path mockPath1 = mock(Path.class);
        when(mockPath1.getFileName()).thenReturn(Paths.get("fileA.txt"));
        when(mockPath1.toString()).thenReturn("fileA.txt");

        Path mockPath2 = mock(Path.class);
        when(mockPath2.getFileName()).thenReturn(Paths.get("fileB.txt"));
        when(mockPath2.toString()).thenReturn("fileB.txt");

        Path mockPath3 = mock(Path.class);
        when(mockPath3.getFileName()).thenReturn(Paths.get("fileC.txt"));
        when(mockPath3.toString()).thenReturn("fileC.txt");

        Stream<Path> mockStream = Stream.of(mockPath1, mockPath2, mockPath3);

        filesMock.when(() -> Files.list(mockDataDir)).thenReturn(mockStream);

        var fileList = fileHandler.getFileList();
        assertArrayEquals(
                new String[] { "fileA.txt", "fileB.txt", "fileC.txt" },
                fileList.toArray(),
                "Incorrect getFileList() output."
        );

        filesMock.when(() -> Files.list(mockDataDir)).thenReturn(Stream.empty());

        fileList = fileHandler.getFileList();
        assertEquals(
                List.of(),
                fileList,
                "Expected fileList to be empty"
        );

        filesMock.when(() -> Files.list(mockDataDir)).thenThrow(new IOException());
        fileList = fileHandler.getFileList();
        assertNull(
                fileList,
                "Expected fileList to be null because of thrown exception"
        );

    }

    @Test
    void getFileContents() {
        fileHandler = new FileHandler();

        filesMock
                .when(() -> Files.readString(Paths.get("data/fileA.txt")))
                .thenReturn("Contents of data/fileA.txt");

        var contents = fileHandler.getFileContents("fileA.txt");
        assertEquals("Contents of data/fileA.txt", contents);

        filesMock
                .when(() -> Files.readString(Paths.get("data/fileA.txt")))
                .thenThrow(new IOException());

        contents = fileHandler.getFileContents("fileA.txt");
        assertNull(contents, "Expected contents to be null");
    }

    @Test
    void readCipherKey() {
        fileHandler = new FileHandler();
        var path = Paths.get("./ciphers/test.txt");

        filesMock.when(() -> Files.exists(path)).thenReturn(true);

        filesMock
                .when(() -> Files.readString(path))
                .thenReturn("ABCDEFGHIJKLMNOPQRSTUVWXYZ\nBCDEFGHIJKLMNOPQRSTUVWXYZA");
        var key = fileHandler.readCipherKey("./ciphers/test.txt");
        var expected = new Cipher("ABCDEFGHIJKLMNOPQRSTUVWXYZ", "BCDEFGHIJKLMNOPQRSTUVWXYZA");
        assertEquals(
                expected.getActualLetters(),
                key.getActualLetters(),
                "Actual cipher letters did not match"
        );
        assertEquals(
                expected.getCipherMatch(),
                key.getCipherMatch(),
                "Cipher matches were not equal"
        );
    }
}