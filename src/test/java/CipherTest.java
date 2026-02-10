import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CipherTest {
    private Cipher cipher;

    @BeforeEach
    void setup() {
        cipher = new Cipher("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890", "bcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890a");
    }

    @Test
    void test1_dog() {

        String result = cipher.decipher("eph");
        assertEquals("dog", result);
    }

    @Test
    void test2_dog_with_punctuation() {
        String result = cipher.decipher("eph.");
        assertEquals("dog.", result);
    }

    @Test
    void test3_empty() {
        String result = cipher.decipher("");
        assertEquals("", result);
    }

    @Test
    void test4_large_input(){
        String result = cipher.decipher("bcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890a");
        assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890", result);
    }
}