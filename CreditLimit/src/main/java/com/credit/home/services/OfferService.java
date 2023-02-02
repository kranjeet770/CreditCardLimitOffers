package com.credit.home.services;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.credit.home.dao.AccountRepo;
import com.credit.home.dao.OfferRepo;
import com.credit.home.entities.Account;
import com.credit.home.entities.Offer;
import com.credit.home.enums.LimitType;
import com.credit.home.enums.Status;

@Service
public class OfferService {
	private final AccountRepo accountRepo;
	private final OfferRepo offerRepo;
	
	public OfferService(AccountRepo accountRepo, OfferRepo offerRepo) {
		super();
		this.accountRepo = accountRepo;
		this.offerRepo = offerRepo;
	}
	
	public Offer createComment( Long accountId, Offer offerRequest) 
	{
		Account account = accountRepo.findById(accountId).orElse(null);
		offerRequest.setAccount(account);
	    return offerRepo.save(offerRequest);
	}
	
	public List<Offer> getLimitOffer( Long accountId) 
	{
	    return offerRepo.findActiveOffers(accountId);
	}
	
	public Offer actLimitOffer(  Long limitId, Status status) 
	{
		Offer offer = offerRepo.findById(limitId).orElse(null);
		offer.setStatus(status);
		if(status==Status.ACCEPTED)
		{
			Account account = offer.getAccount();
			if(offer.getLimitType()==LimitType.ACCOUNT_LIMIT)
			{
				account.setLastAccountLimit(account.getAccountLimit());
				account.setAccountLimit(offer.getNewLimit());
				account.setAccountLimitUpdateTime(new Date());
			}
			else if(offer.getLimitType()==LimitType.PER_TRANSACTION_LIMIT)
			{
				account.setLastPerTransactionLimit(account.getPerTransactionLimit());
				account.setPerTransactionLimit(offer.getNewLimit());
				account.setPerTransactionLimitUpdateTime(new Date());
			}
		}
	    return offerRepo.save(offer);
	}
}
