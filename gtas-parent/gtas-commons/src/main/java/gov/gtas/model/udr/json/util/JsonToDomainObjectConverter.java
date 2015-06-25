package gov.gtas.model.udr.json.util;

import gov.gtas.model.udr.RuleMeta;
import gov.gtas.model.udr.UdrRule;
import gov.gtas.model.udr.YesNoEnum;
import gov.gtas.model.udr.json.UdrSpecification;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Utility functions to convert JSON objects to domain objects.
 * @author GTAS3 (AB)
 *
 */
public class JsonToDomainObjectConverter {
	/**
	 * 
	 * @param rule
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
    public static UdrSpecification getJsonFromUdrRule(UdrRule rule) throws IOException, ClassNotFoundException{
    	UdrSpecification ret = null;
	    	if(rule.getUdrConditionObject() != null){
			final ByteArrayInputStream bis = new ByteArrayInputStream(rule.getUdrConditionObject());
			final GZIPInputStream gzipInStream = new GZIPInputStream(bis);
			final ObjectInputStream in = new ObjectInputStream(gzipInStream);
			ret = (UdrSpecification)in.readObject();
			in.close();	
    	}
		return ret;
    }
	
    /**
     * 
     * @param inputJson
     * @return
     * @throws IOException
     */
    public static UdrRule createUdrRuleFromJson(UdrSpecification inputJson) throws IOException{
    	final String title = inputJson.getSummary().getTitle();
    	final String descr = inputJson.getSummary().getDescription();
    	final boolean enabled = inputJson.getSummary().isEnabled();
    	
    	UdrRule rule = createUdrRule(title, descr, enabled?YesNoEnum.Y:YesNoEnum.N);
    	setJsonObjectInUdrRule(rule, inputJson);
    	
    	return rule;
    	
    }
    /**
     * Converts a UdrSpecification JSON object to compressed binary data for saving in the database as a BLOB.
     * @param rule the rule domain object.
     * @param json the json object to serialize.
     */
    private static void setJsonObjectInUdrRule(UdrRule rule, UdrSpecification json) throws IOException{
		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		final GZIPOutputStream gzipOutStream = new GZIPOutputStream(bos);
	    final ObjectOutputStream out = new ObjectOutputStream(gzipOutStream);
	    out.writeObject( json );
	    out.close();
    	
	    byte[] bytes = bos.toByteArray();
	    rule.setUdrConditionObject(bytes);
    }
    /**
     * 
     * @param title
     * @param descr
     * @param enabled
     * @return
     */
	public static UdrRule createUdrRule(String title, String descr, YesNoEnum enabled) {
		UdrRule rule = new UdrRule();
		rule.setDeleted(YesNoEnum.N);
		rule.setEditDt(new Date());
		RuleMeta meta = createRuleMeta(title, descr, enabled);
		rule.setMetaData(meta);
		return rule;
	}
	/**
	 * 
	 * @param title
	 * @param descr
	 * @param enabled
	 * @return
	 */
	private static RuleMeta createRuleMeta(String title, String descr,
			YesNoEnum enabled) {
		RuleMeta meta = new RuleMeta();
		meta.setDescription(descr);
		meta.setEnabled(enabled);
		meta.setHitSharing(YesNoEnum.N);
		meta.setPriorityHigh(YesNoEnum.N);
		meta.setStartDt(new Date());
		meta.setTitle(title);
		return meta;
	}

}
