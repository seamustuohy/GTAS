package gov.gtas.model.lookup;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.cache.annotation.Cacheable;

import gov.gtas.model.BaseEntity;

@Cacheable
@Entity
@Table(name = "app_configuration")
public class AppConfiguration extends BaseEntity {
    private static final long serialVersionUID = 1L;
    public AppConfiguration() { }
    private String option;
    private String value;
    private String description;
    public String getOption() {
        return option;
    }
    public void setOption(String option) {
        this.option = option;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
