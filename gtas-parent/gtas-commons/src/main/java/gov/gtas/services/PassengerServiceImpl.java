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
import gov.gtas.model.Document;
import gov.gtas.model.Flight;
import gov.gtas.model.HitsSummary;
import gov.gtas.model.Passenger;
import gov.gtas.repository.HitsSummaryRepository;
import gov.gtas.repository.PassengerRepository;
import gov.gtas.services.dto.PassengersPageDto;
import gov.gtas.services.dto.PassengersRequestDto;
import gov.gtas.vo.passenger.DocumentVo;
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
    public PassengersPageDto getPassengersByFlightId(Long flightId, PassengersRequestDto request) {
        List<Passenger> passengerList = passengerRespository.getPassengersByFlightId(flightId, request);
        List<PassengerVo> vos = new ArrayList<>();
        
        for (Passenger p : passengerList) {
            PassengerVo vo = new PassengerVo();
            BeanUtils.copyProperties(p, vo);
            vos.add(vo);
            
            for (Document d : p.getDocuments()) {
                DocumentVo docVo = new DocumentVo();
                BeanUtils.copyProperties(d, docVo);
                vo.addDocument(docVo);
            }
            
            fillWithHitsInfo(vo,flightId, p.getId());
        }
        
        return new PassengersPageDto(vos, -1);
    }

    @Override
    @Transactional
    public PassengersPageDto findAllWithFlightInfo(PassengersRequestDto request) {
        long total = -1;
        List<Object[]> results = passengerRespository.getAllPassengersAndFlights(request);
        List<PassengerVo> rv = new ArrayList<>();

        for (Object[] objs : results) {
            Passenger p = (Passenger)objs[0];
            Flight f = (Flight)objs[1];
            PassengerVo vo = new PassengerVo();
            BeanUtils.copyProperties(p, vo);
            rv.add(vo);

            // grab hits information
            fillWithHitsInfo(vo, f.getId(), p.getId());
            
            // grab flight info
            vo.setFlightId(f.getId().toString());
            vo.setFlightNumber(f.getFlightNumber());
            vo.setCarrier(f.getCarrier());
            vo.setEtd(f.getEtd());
            vo.setEta(f.getEta());
        }
        
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