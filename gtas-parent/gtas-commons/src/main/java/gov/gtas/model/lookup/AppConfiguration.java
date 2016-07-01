/*
 * All GTAS code is Copyright 2016, Unisys Corporation.
 * 
 * Please see LICENSE.txt for details.
 */
package gov.gtas.model.lookup;

import javax.persistence.Column;
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
    
    // mysql (not mariadb) reserves the word 'option'
    @Column(name = "opt")
    private String option;
    @Column(name = "val")
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
