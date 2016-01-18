package gov.gtas.util;

import com.google.api.GoogleAPI;
import com.google.api.translate.Language;
import com.google.api.translate.Translate;
/**
 * This is a sample program to test the GOOGLE Translate API.
 * It uses the java api that can be downloaded from github.
 * To run this program put google-api-translate-java-0.97.jar
 * and org.json-20120521.jar in your java classpath.
 * 
 * Before you can run this program you have to register for a Google API key.
 * There is a free trial period of 60 days, with max usage of $300.00
 * 
 *
 */
public class GoogleTranslateUtil {
     /*
      * Test use:
      * https://www.googleapis.com/language/translate/v2?key=<insert API key here>&q=hello%20world&source=en&target=de
      */

	  public static void main(String[] args) throws Exception {
		if(args.length < 2){
			System.out.println("please specify api key (arg1) and referrer address URL (arg2)");
		}
		
	    // Set the Google Translate API key
	    // See: http://code.google.com/apis/language/translate/v2/getting_started.html
	    GoogleAPI.setKey(args[0]);
		 
	    // Set the HTTP referrer to your website address.
	    GoogleAPI.setHttpReferrer(args[1]);

	    //String translatedText = Translate.DEFAULT.execute("Bonjour le monde", Language.FRENCH, Language.TELUGU);
	    String translatedText = Translate.DEFAULT.execute("or", Language.ENGLISH, Language.TELUGU);

	    System.out.println(translatedText);
	  }
	
}
