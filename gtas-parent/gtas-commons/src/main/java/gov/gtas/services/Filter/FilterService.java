package gov.gtas.services.Filter;

import java.util.List;

public interface FilterService {
    public FilterData create(FilterData filterData);

    public void delete(String userId);

    public List<FilterData> findAll();

    public FilterData update(FilterData filterData);

    public FilterData findById(String userId);

}
