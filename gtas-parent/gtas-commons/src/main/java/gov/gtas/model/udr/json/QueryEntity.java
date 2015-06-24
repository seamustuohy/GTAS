package gov.gtas.model.udr.json;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonSubTypes({@JsonSubTypes.Type(QueryTerm.class), @JsonSubTypes.Type(QueryObject.class)})
public interface QueryEntity extends Serializable{

}
