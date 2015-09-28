package gov.gtas.services;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import gov.gtas.enumtype.HitTypeEnum;
import gov.gtas.model.Flight;
import gov.gtas.model.HitsSummary;
import gov.gtas.model.Passenger;
import gov.gtas.repository.HitsSummaryRepository;
import gov.gtas.repository.PassengerRepository;
import gov.gtas.vo.passenger.PassengerVo;

@Service
public class PassengerServiceImpl implements PassengerService {

	@Resource
	private PassengerRepository passengerRespository;

    @Resource
    private HitsSummaryRepository hitsSummaryRepository;

	@Override
	@Transactional
	public Passenger create(Passenger passenger) {
		return passengerRespository.save(passenger);
	}

	@Override
	@Transactional
	public Passenger delete(Long id) {
		Passenger passenger = this.findById(id);
		if(passenger != null)
			passengerRespository.delete(passenger);
		return passenger;
	}

	@Override
	@Transactional
	public List<Passenger> findAll() {
		return (List<Passenger>)passengerRespository.findAll();
	}
	
    @Override
    @Transactional
    public Page<Passenger> findAll(int pageNumber, int pageSize) {
        int pn = pageNumber > 0 ? pageNumber - 1 : 0;
        return passengerRespository.findAll(new PageRequest(pn, pageSize));
    }

    @Override
    @Transactional
    public List<PassengerVo> findAllWithFlightInfo(int pageNumber, int pageSize) {
        int pn = pageNumber > 0 ? pageNumber - 1 : 0;
        List<Object[]> results = passengerRespository.getAllPassengersAndFlights(new PageRequest(pn, pageSize));
        List<PassengerVo> rv = new ArrayList<>();
        for (Object[] objs : results) {
            Passenger p = (Passenger)objs[0];
            Flight f = (Flight)objs[1];
            PassengerVo vo = new PassengerVo();
            BeanUtils.copyProperties(p, vo);
            rv.add(vo);

            // grab hits information
            List<HitsSummary> hitsSummary = hitsSummaryRepository.findByFlightIdAndPassengerId(f.getId(), p.getId());
            if (!CollectionUtils.isEmpty(hitsSummary)) {
                for (HitsSummary hs : hitsSummary) {
                    if (vo.getOnRuleHitList() && vo.getOnWatchList()) {
                        break;
                    }
                    
                    String hitType = hs.getHitType();
                    if (hitType.equals(HitTypeEnum.R.toString())) {
                        vo.setOnRuleHitList(true);
                    } else {
                        vo.setOnWatchList(true);
                    }
                }
            }
            
            // grab flight info
            vo.setFlightId(f.getId());
            vo.setFlightNumber(f.getFlightNumber());
            vo.setCarrier(f.getCarrier());
            vo.setEtd(f.getEtd());
            vo.setEta(f.getEta());
        }
        return rv;
    }

	@Override
	@Transactional
	public Passenger update(Passenger passenger) {
		Passenger passengerToUpdate = this.findById(passenger.getId());
		if(passengerToUpdate != null){
			passengerToUpdate.setAge(passenger.getAge());
			passengerToUpdate.setCitizenshipCountry(passenger.getCitizenshipCountry());
			passengerToUpdate.setDebarkation(passenger.getDebarkation());
			passengerToUpdate.setDebarkCountry(passenger.getDebarkCountry());
			passengerToUpdate.setDob(passenger.getDob());
			passengerToUpdate.setEmbarkation(passenger.getEmbarkation());
			passengerToUpdate.setEmbarkCountry(passenger.getEmbarkCountry());
			passengerToUpdate.setFirstName(passenger.getFirstName());
			passengerToUpdate.setFlights(passenger.getFlights());
			passengerToUpdate.setGender(passenger.getGender());
			passengerToUpdate.setLastName(passenger.getLastName());
			passengerToUpdate.setMiddleName(passenger.getMiddleName());
			passengerToUpdate.setResidencyCountry(passenger.getResidencyCountry());
			passengerToUpdate.setDocuments(passenger.getDocuments());
			passengerToUpdate.setSuffix(passenger.getSuffix());
			passengerToUpdate.setTitle(passenger.getTitle());
//			passengerToUpdate.setType(passenger.getType());
		}
		return passengerToUpdate;
	}

	@Override
	@Transactional
	public Passenger findById(Long id) {
		return passengerRespository.findOne(id);
	}

	@Override
	@Transactional
	public List<Passenger> getPassengersByLastName(String lastName) {
		List<Passenger> passengerList = passengerRespository.getPassengersByLastName(lastName);
		return passengerList;
	}

    @Override
    @Transactional
    public Page<Passenger> getPassengersByFlightId(Long flightId, Integer pageNumber, Integer pageSize) {
        int pn = pageNumber > 0 ? pageNumber - 1 : 0;
        Page<Passenger> passengerList = passengerRespository.getPassengersByFlightId(flightId, new PageRequest(pn, pageSize));
        return passengerList;
    }
}
