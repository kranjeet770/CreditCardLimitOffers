package com.credit.home.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.credit.home.entities.Offer;

public interface OfferRepo extends JpaRepository<Offer, Long>
{
	@Query("from Offer where status is null and offerExpiryTime >= CURRENT_TIMESTAMP "
			+ "and offerActivationTime<= CURRENT_TIMESTAMP and account.accountId = ?1")
	List<Offer> findActiveOffers(Long accountId);
	
}
