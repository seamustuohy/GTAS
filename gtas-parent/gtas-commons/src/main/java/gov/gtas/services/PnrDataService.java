package gov.gtas.services;

import java.util.List;

import gov.gtas.model.PnrData;

public interface PnrDataService {
	
	public PnrData create(PnrData pnrData);
	public PnrData delete(Long id);
	public PnrData update(PnrData pnrData);
	public PnrData findById(Long id);
	public List<PnrData> findAll();

}
