package gov.gtas.querybuilder.model;

import gov.gtas.model.udr.json.QueryObject;

public interface IUserQueryResult {
    public int getId();
    public void setId(int id);
    public String getTitle();
    public void setTitle(String title);
    public String getDescription();
    public void setDescription(String description);
    public QueryObject getQuery();
    public void setQuery(QueryObject query);
}
