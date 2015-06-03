package gov.cbp.taspd.gtas.util;

public class ParseUtils {
	public static String stripHeaderAndFooter(String text) {
		String rv = null;
		final int STX_CODEPOINT = 2;
		final int ETX_CODEPOINT = 3;
		
		int stxIndex = text.indexOf(STX_CODEPOINT);
		if (stxIndex != -1) {
			rv = text.substring(stxIndex + 1);
		}
		int etxIndex = text.indexOf(ETX_CODEPOINT);
		if (etxIndex != -1) {
			rv = text.substring(0, etxIndex);
		}		
		
		return rv;
	}
}
