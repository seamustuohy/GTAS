package gov.gtas.services;

import gov.gtas.model.Traveler;
import gov.gtas.repository.PassengerRepository;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
public class PassengerServiceImpl implements PassengerService {

	@Resource
	private PassengerRepository passengerRespository;
	
	@Override
	@Transactional
	public Traveler create(Traveler passenger) {
		return passengerRespository.save(passenger);
	}

	@Override
	@Transactional
	public Traveler delete(Long id) {
		Traveler passenger = this.findById(id);
		if(passenger != null)
			passengerRespository.delete(passenger);
		return passenger;
	}

	@Override
	@Transactional
	public List<Traveler> findAll() {
		return (List<Traveler>)passengerRespository.findAll();
	}

	@Override
	@Transactional
	public Traveler update(Traveler passenger) {
		Traveler passengerToUpdate = this.findById(passenger.getId());
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
	public Traveler findById(Long id) {
		return passengerRespository.findOne(id);
	}

	@Override
	@Transactional
	public Traveler getPassengerByName(String firstName, String lastName) {
		Traveler passenger = null;
		List<Traveler> passengerList = passengerRespository.getPassengerByName(firstName, lastName);
		if(passengerList != null && passengerList.size() > 0)
			passenger = passengerList.get(0);
		return passenger;
	}

	@Override
	@Transactional
	public List<Traveler> getPassengersByLastName(String lastName) {
		List<Traveler> passengerList = passengerRespository.getPassengersByLastName(lastName);
		return passengerList;
	}

    @Override
    @Transactional
    public List<Traveler> getPassengersByFlightId(Long flightId) {
        List<Traveler> passengerList = passengerRespository.getPassengersByFlightId(flightId);
        return passengerList;
    }
}
