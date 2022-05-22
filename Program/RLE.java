package Program;

public class RLE {

    public static String encode(String str) {
        var sb = new StringBuilder();
        char last = str.charAt(0);
        int lastCnt = 1;
        for (int index = 1; index <= str.length(); index++) {
            if (index == str.length()) {
                sb.append(last).append(lastCnt);
                break;
            }
            char current = str.charAt(index);
            if (last == current) lastCnt++;
            else {
                sb.append(last).append(lastCnt);
                last = current;
                lastCnt = 1;
            }
        }
        return sb.toString();
    }

    public static String decode(String enc) {
        var sb = new StringBuilder();
        var repeats = new StringBuilder();
        char last = enc.charAt(0);
        for (int index = 1; index <= enc.length(); index++) {
            if (index == enc.length()) {
                for (int i = 0; i < Integer.parseInt(repeats.toString()); i++) sb.append(last);
                break;
            }
            char currentCharacter = enc.charAt(index);
            if (Character.isDigit(currentCharacter)) repeats.append(currentCharacter);
            else {
                for (int i = 0; i < Integer.parseInt(repeats.toString()); i++) sb.append(last);
                last = currentCharacter;
                repeats = new StringBuilder();
            }

        }
        return sb.toString();
    }
}
