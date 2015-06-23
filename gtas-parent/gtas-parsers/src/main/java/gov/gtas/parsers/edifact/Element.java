package gov.gtas.parsers.edifact;

public final class Element {
    private String value;

    @SuppressWarnings("unused")
    private Element() { }
    
    public Element(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
    @Override
    public String toString() {
        return "Element [value=" + value + "]";
    }
}
