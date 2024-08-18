package ghops.gprint.tools;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

public class DoubleText {

    Pattern validEditingState = Pattern.compile("-?(([1-9][0-9]*)|0)?(\\.[0-9]*)?");

    UnaryOperator<TextFormatter.Change> filter = c -> {
        String text = c.getControlNewText();
        if (validEditingState.matcher(text).matches()) {
            return c;
        } else {
            return null;
        }
    };

    StringConverter<Double> converter = new StringConverter<Double>() {

        @Override
        public Double fromString(String s) {
            if (s.isEmpty() || "-".equals(s) || ".".equals(s) || "-.".equals(s)) {
                return 0.0;
            } else {
                return Double.valueOf(s);
            }
        }

        @Override
        public String toString(Double d) {
            return d.toString();
        }
    };

    TextFormatter<Double> textFormatter;

    public DoubleText(double d) {
        this.textFormatter = new TextFormatter<>(converter, d, filter);
 
    }
 
 
    
    public TextFormatter<Double> getformat(){
        return this.textFormatter;
    }
 

}
