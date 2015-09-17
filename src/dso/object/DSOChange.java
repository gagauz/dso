package dso.object;

public class DSOChange {
    private final DSOManager object;
    private final String fieldName;
    private final Object fieldValue;

    public DSOChange(DSOManager object, String fieldName, String fieldValue) {
        this.object = object;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

}
