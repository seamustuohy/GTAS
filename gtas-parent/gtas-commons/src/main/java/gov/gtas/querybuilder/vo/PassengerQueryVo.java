package gov.gtas.querybuilder.vo;

import java.util.List;

public class PassengerQueryVo {
    List<Object[]> result;
    long totalPassengers;
    public List<Object[]> getResult() {
        return result;
    }
    public void setResult(List<Object[]> result) {
        this.result = result;
    }
    public long getTotalPassengers() {
        return totalPassengers;
    }
    public void setTotalPassengers(long totalPassengers) {
        this.totalPassengers = totalPassengers;
    }
    
}
