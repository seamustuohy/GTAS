package gov.gtas.services.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonFormat;

import gov.gtas.vo.passenger.FlightVo;

public class FlightsRequestDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private int pageNumber;
    private int pageSize;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = FlightVo.DATE_FORMAT)        
    private Date etaStart;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = FlightVo.DATE_FORMAT)        
    private Date etaEnd;
    
    private List<SortOptionsDto> sort;
    
    public FlightsRequestDto() { }
    
    public int getPageNumber() {
        return pageNumber;
    }
    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    public Date getEtaStart() {
        return etaStart;
    }
    public void setEtaStart(Date etaStart) {
        this.etaStart = etaStart;
    }
    public Date getEtaEnd() {
        return etaEnd;
    }
    public void setEtaEnd(Date etaEnd) {
        this.etaEnd = etaEnd;
    }        
    public List<SortOptionsDto> getSort() {
        return sort;
    }
    public void setSort(List<SortOptionsDto> sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE); 
    }
}

