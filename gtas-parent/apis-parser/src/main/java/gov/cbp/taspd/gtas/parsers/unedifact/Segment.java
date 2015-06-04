package gov.cbp.taspd.gtas.parsers.unedifact;


public class Segment {
    private String name;
    protected Composite[] composites;
    
    public Segment(String name, Composite[] composites) {
        this.name = name;
        this.composites = composites;
    }
    
    public String getName() {
        return name;
    }
    public Composite[] getComposites() {
        return composites;
    }

    @Override
    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append(this.name + " ");
        for (Composite x : this.composites) {
            b.append(x + " ");
        }
        return b.toString();
    }
    
    
}
