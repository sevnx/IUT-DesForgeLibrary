package librairies.protocol;

public class CodeDecode {
    public static String code(String message) {
        return message.replace("\n", "##");
    }

    public static String decode(String message) {
        return message.replace("##", "\n");
    }
}
