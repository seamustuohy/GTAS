package gov.gtas.services;

import gov.gtas.model.Pnr;
import gov.gtas.repository.PnrRepository;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class PnrServiceImpl implements PnrService {

	@Resource
	private PnrRepository pnrRespository;
	
	@Override
	@Transactional
	public Pnr create(Pnr pnr) {
		return pnrRespository.save(pnr);
	}

	@Override
	@Transactional
	public Pnr delete(Long id) {
		Pnr pnr = this.findById(id);
		if(pnr != null){
			pnrRespository.delete(pnr);
		}
		return pnr;
	}

	@Override
	@Transactional
	public Pnr update(Pnr pnr) {
		Pnr rv = this.findById(pnr.getId());
		if(rv != null){
			mapPnr(pnr,rv);
		}
		return rv;
	}

	@Override
	@Transactional
	public Pnr findById(Long id) {
		return pnrRespository.findOne(id);
	}

	@Override
	@Transactional
	public List<Pnr> findAll() {
		return (List<Pnr>) pnrRespository.findAll();
	}
	
	private void mapPnr(Pnr source, Pnr target){
		target.setBagCount(source.getBagCount());
		target.setDateBooked(source.getDateBooked());
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
		target.setDateReceived(source.getDateReceived());
		target.setTotalDwellTime(source.getTotalDwellTime());
		target.setUpdatedAt(new Date());
		target.setUpdatedBy(source.getUpdatedBy());
	}
}
