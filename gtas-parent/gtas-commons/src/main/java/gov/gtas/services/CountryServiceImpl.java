/*
 * All GTAS code is Copyright 2016, Unisys Corporation.
 * 
 * Please see LICENSE.txt for details.
 */
package gov.gtas.services;

import gov.gtas.model.lookup.Country;
import gov.gtas.repository.CountryRepository;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CountryServiceImpl implements CountryService {

    @Resource
    private CountryRepository countryRespository;
    
    @Override
    @Transactional
    public Country create(Country country) {
        return countryRespository.save(country);
    }

    @Override
    @Transactional
    public Country delete(Long id) {
        Country country = this.findById(id);
        if(country != null){
            countryRespository.delete(country);
        }
        return country;
    }

    @Override
    @Transactional
    public List<Country> findAll() {
        return (List<Country>) countryRespository.findAll();
    }

    @Override
    @Transactional
    public Country update(Country country) {
        // not implemented
        return null;
    }

    @Override
    @Transactional
    public Country findById(Long id) {
        Country country = null;
        List<Country> countries = (List<Country>)countryRespository.findOne(id);
        if(countries != null && countries.size() >0)
            country=countries.get(0);
        return country;
    }

    @Override
    @Transactional
    public Country getCountryByTwoLetterCode(String country) {
        Country ctry = null;
        List<Country> countries = (List<Country>)countryRespository.getCountryByTwoLetterCode(country);
        if(countries != null && countries.size() >0)
            ctry=countries.get(0);
        return ctry;
    }

    @Override
    @Transactional
    public Country getCountryByThreeLetterCode(String country) {
        Country ctry = null;
        List<Country> countries = (List<Country>)countryRespository.getCountryByThreeLetterCode(country);
        if(countries != null && countries.size() >0)
            ctry=countries.get(0);
        return ctry;
    }

}
