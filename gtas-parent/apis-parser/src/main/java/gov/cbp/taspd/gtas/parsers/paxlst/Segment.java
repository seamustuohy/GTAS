package gov.cbp.taspd.gtas.parsers.paxlst;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Segment {
    private String name;
    private Composite[] composites;
    
    public Segment(String txt, UNA serviceStrings) {        
        String regex = String.format(
                "[^\\%c]*(\\%c\\%c)+[^\\%c]*|[^\\%c]+",
                serviceStrings.dataElementSeparator,
                serviceStrings.releaseCharacter,
                serviceStrings.dataElementSeparator,
                serviceStrings.dataElementSeparator,
                serviceStrings.dataElementSeparator);
        Pattern tokenPattern = Pattern.compile(regex);
        Matcher matcher = tokenPattern.matcher(txt);
        List<String> tokens = new LinkedList<>();
        while (matcher.find()) {
            tokens.add(matcher.group());
        }

        int numTokens = tokens.size();
        if (numTokens == 0) {
            // error?
            return;
        }
        
        this.name = tokens.get(0);
        tokens.remove(0);
        
        if (numTokens >= 1) {
            this.composites = new Composite[tokens.size()];
            for (int i=0; i<tokens.size(); i++) {
                this.composites[i] = new Composite(tokens.get(i), serviceStrings);
            }
        }
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
