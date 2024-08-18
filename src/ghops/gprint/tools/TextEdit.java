package ghops.gprint.tools;

public class TextEdit {

    public static String toUpper(String text) {
        if (text == null) {
            return text;
        }
        // return text.replace("ı", "I").replace("İ", "I").toUpperCase();
        return text.replace("i", "İ").toUpperCase();
    }

    public static String toUpperEN(String text) {
        if (text == null) {
            return text;
        }
        return text.replace("İ", "I").replace("i", "I").toUpperCase();
    }
}
