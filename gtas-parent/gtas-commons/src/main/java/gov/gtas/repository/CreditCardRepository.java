package gov.gtas.repository;

import java.util.Date;

import org.springframework.data.repository.CrudRepository;

import gov.gtas.model.CreditCard;

public interface CreditCardRepository extends CrudRepository<CreditCard, Long> {
    CreditCard findByCardTypeAndNumberAndExpiration(String cardType, String number, Date expiration);
}
