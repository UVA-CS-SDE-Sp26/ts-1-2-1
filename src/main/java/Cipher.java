import java.util.HashMap;

public class Cipher {
    HashMap<Character, Character> key;
    String actualLetters;
    String cipherMatch;

    public Cipher(String actualLetters, String cipherMatch){
        this.actualLetters = actualLetters;
        this.cipherMatch = cipherMatch;
    }

    public String getActualLetters() {
        return actualLetters;
    }

    public void setActualLetters(String actualLetters) {
        this.actualLetters = actualLetters;
    }

    public String getCipherMatch() {
        return cipherMatch;
    }

    public void setCipherMatch(String cipherMatch) {
        this.cipherMatch = cipherMatch;
    }

    private HashMap<Character,Character> createKeySet(){
        HashMap<Character, Character> keySet = new HashMap<>();
        for(int i = 0; i < actualLetters.length(); i++){

        }
    }
}
