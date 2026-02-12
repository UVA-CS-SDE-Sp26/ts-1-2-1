import java.util.HashMap;

public class Cipher {
    private HashMap<Character, Character> key;
    private String actualLetters;
    private String cipherMatch;

    public Cipher(String actualLetters, String cipherMatch){
        this.actualLetters = actualLetters;
        this.cipherMatch = cipherMatch;
        this.key = new HashMap<>();
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

    public String decipher(String toDecrypt){
        String output = "";
        String cipherMatch = getCipherMatch();
        String actualLetters = getActualLetters();
        for(int i = 0; i < cipherMatch.length(); i++){
            key.put(cipherMatch.charAt(i), actualLetters.charAt(i));
        }
        for(int i = 0; i < toDecrypt.length(); i++){
            char currChar = toDecrypt.charAt(i);
            if(key.containsKey(currChar)){
                output += key.get(currChar);
            }
            else{
                output += currChar;
            }
        }
        return output;
    }
}
