package gov.cbp.taspd.gtas.parsers.unedifact;

public class Element {
    private String value;
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
