package gov.gtas.services;

import gov.gtas.model.PnrData;
import gov.gtas.repository.PnrDataRepository;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class PnrDataServiceImpl implements PnrDataService {

	@Resource
	private PnrDataRepository pnrRespository;
	
	@Override
	@Transactional
	public PnrData create(PnrData pnrData) {
		return pnrRespository.save(pnrData);
	}

	@Override
	@Transactional
	public PnrData delete(Long id) {
		PnrData pnr = this.findById(id);
		if(pnr != null){
			pnrRespository.delete(pnr);
		}
		return pnr;
	}

	@Override
	@Transactional
	public PnrData update(PnrData pnrData) {
		PnrData pnr = this.findById(pnrData.getId());
		if(pnr != null){
			mapPnrData(pnrData,pnr);
		}
		return pnr;
	}

	@Override
	@Transactional
	public PnrData findById(Long id) {
		return pnrRespository.findOne(id);
	}

	@Override
	@Transactional
	public List<PnrData> findAll() {
		return (List<PnrData>) pnrRespository.findAll();
	}
	
	private void mapPnrData(PnrData source,PnrData target){
		target.setBagCount(source.getBagCount());
		target.setBooked(source.getBooked());
		target.setCarrier(source.getCarrier());
		target.setChangeDate();
		target.setDaysBookedBeforeTravel(source.getDaysBookedBeforeTravel());
		target.setDepartureDate(source.getDepartureDate());
		target.setEmail(source.getEmail());
		target.setFormOfPayment(source.getFormOfPayment());
		target.setOrigin(source.getOrigin());
		target.setOriginCountry(source.getOriginCountry());
		//target.setPassenger(source.getPassenger());
		
		target.setPassengerCount(source.getPassengerCount());
		target.setReceived(source.getReceived());
		target.setTotalDwellTime(source.getTotalDwellTime());
		target.setUpdatedAt(new Date());
		target.setUpdatedBy(source.getUpdatedBy());
	}
}
