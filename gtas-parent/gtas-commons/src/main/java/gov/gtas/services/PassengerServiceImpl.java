package gov.gtas.services;

import gov.gtas.model.Pax;
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
	public Pax create(Pax passenger) {
		return passengerRespository.save(passenger);
	}

	@Override
	@Transactional
	public Pax delete(Long id) {
		Pax passenger = this.findById(id);
		if(passenger != null)
			passengerRespository.delete(passenger);
		return passenger;
	}

	@Override
	@Transactional
	public List<Pax> findAll() {
		return (List<Pax>)passengerRespository.findAll();
	}

	@Override
	@Transactional
	public Pax update(Pax passenger) {
		Pax passengerToUpdate = this.findById(passenger.getId());
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
			passengerToUpdate.setType(passenger.getType());
		}
		return passengerToUpdate;
	}

	@Override
	@Transactional
	public Pax findById(Long id) {
		return passengerRespository.findOne(id);
	}

	@Override
	@Transactional
	public Pax getPassengerByName(String firstName, String lastName) {
		Pax passenger = null;
		List<Pax> passengerList = passengerRespository.getPassengerByName(firstName, lastName);
		if(passengerList != null && passengerList.size() > 0)
			passenger = passengerList.get(0);
		return passenger;
	}

	@Override
	@Transactional
	public List<Pax> getPassengersByLastName(String lastName) {
		List<Pax> passengerList = passengerRespository.getPassengersByLastName(lastName);
		return passengerList;
	}

}
