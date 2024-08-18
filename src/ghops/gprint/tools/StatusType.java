package ghops.gprint.tools;

import java.util.ArrayList;
import java.util.List;

public class StatusType {

    private static final int WAITING = 0;
    private static final int COMPLATE = 1;
    private static final int ABORT = 2;

    private int value;
    private String title;

    public static List<StatusType> getStatusList() {
        List<StatusType> list = new ArrayList<>();
        list.add(new StatusType(WAITING));
        list.add(new StatusType(COMPLATE));
        list.add(new StatusType(ABORT));
        return list;
    }

    public void StatusType(int value) {
        this.setValue(value);
    }

    
    
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        this.setTitle(value);
    }

    private void setTitle(int value) {
        switch (value) {
            case WAITING ->
                this.title = "BEKLEMEDE";

            case COMPLATE ->
                this.title = "TAMAMLANDI";

            case ABORT ->
                this.title = "İPTAL EDİLDİ";

        }
    }

    public String getTitle() {
        return title;
    }

    public StatusType(int value) {
        this.value = value;
        this.setTitle(value);
    }

    @Override
    public String toString() {
        return this.getTitle();
    }
}
