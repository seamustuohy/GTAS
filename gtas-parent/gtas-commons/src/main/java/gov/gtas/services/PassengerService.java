package gov.gtas.services;

import gov.gtas.model.Pax;

import java.util.List;


public interface PassengerService {
	
	public Pax create(Pax passenger);
    public Pax delete(Long id);
    public List<Pax> findAll();
    public Pax update(Pax passenger) ;
    public Pax findById(Long id);
    public Pax getPassengerByName(String firstName,String lastName);
    public List<Pax> getPassengersByLastName(String lastName);


}
