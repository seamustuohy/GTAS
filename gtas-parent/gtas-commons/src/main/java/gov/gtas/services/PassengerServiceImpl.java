package gov.gtas.services;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import gov.gtas.enumtype.HitTypeEnum;
import gov.gtas.model.Flight;
import gov.gtas.model.HitsSummary;
import gov.gtas.model.Passenger;
import gov.gtas.repository.HitsSummaryRepository;
import gov.gtas.repository.PassengerRepository;
import gov.gtas.services.dto.PassengersPageDto;
import gov.gtas.services.dto.PassengersRequestDto;
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
    public PassengersPageDto getPassengersByCriteria(Long flightId, PassengersRequestDto request) {
        List<Object[]> results = passengerRespository.getPassengersByCriteria(flightId, request);
        List<PassengerVo> rv = new ArrayList<>();

        for (Object[] objs : results) {
            Passenger p = (Passenger)objs[0];
            Flight f = (Flight)objs[1];
            Long currentFlightId = f.getId();
            HitsSummary hits = (HitsSummary)objs[2];
            
            // hack: until we figure out how to properly left join in query
            if (hits != null && currentFlightId != hits.getFlight().getId()) {
                continue;
            }

            PassengerVo vo = new PassengerVo();
            BeanUtils.copyProperties(p, vo);
            rv.add(vo);

            if (hits != null) {
                String hitType = hits.getHitType();
                if (hitType.contains(HitTypeEnum.R.toString())) {
                    vo.setOnRuleHitList(true);
                }
                if (hitType.contains(HitTypeEnum.P.toString())) {
                    vo.setOnWatchList(true);
                }
                if (hitType.contains(HitTypeEnum.D.toString())) {
                    vo.setOnWatchListDoc(true);
                }
            }

            // grab flight info
            vo.setFlightId(f.getId().toString());
            vo.setFlightNumber(f.getFlightNumber());
            vo.setFullFlightNumber(f.getFullFlightNumber());
            vo.setCarrier(f.getCarrier());
            vo.setEtd(f.getEtd());
            vo.setEta(f.getEta());
            
            // documents?
//            for (Document d : p.getDocuments()) {
//                DocumentVo docVo = new DocumentVo();
//                BeanUtils.copyProperties(d, docVo);
//                vo.addDocument(docVo);
//            }            
        }
        
        /*
         * we're not currently caluclating total # of results, which is
         * expensive.  Return -1 because ui-grid on the front-end will
         * interpret this by not showing total # results.
         */
        long total = -1;
        return new PassengersPageDto(rv, total);
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
    public void fillWithHitsInfo(PassengerVo vo, Long flightId, Long passengerId) {
        List<HitsSummary> hitsSummary = hitsSummaryRepository.findByFlightIdAndPassengerId(flightId, passengerId);
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
    }
}