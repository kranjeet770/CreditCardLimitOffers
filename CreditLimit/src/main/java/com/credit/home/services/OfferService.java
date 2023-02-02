package com.credit.home.services;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

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
	
	public OfferService(AccountRepo accountRepo, OfferRepo offerRepo) 
	{
		super();
		this.accountRepo = accountRepo;
		this.offerRepo = offerRepo;
	}
	
	public Offer createLimitOffer( Long accountId,@Validated Offer offerRequest) 
	{
		Account account = accountRepo.findById( accountId ).orElse( null );
		
		if(account == null )
		{
			System.out.println( "Account not found" );
			return null;
		}
		
		if( offerRepo.existsById( offerRequest.getLimitId() ))
		{
			System.out.println( "Limit Offer Id already exists" );
			return null;			
		}
		
		if( offerRequest.getOfferActivationTime().compareTo( offerRequest.getOfferExpiryTime() ) > 0
				|| new Date().compareTo( offerRequest.getOfferExpiryTime() ) > 0 )
		{
			System.out.println("Expiry Date not valid");
			return null;
		}
		
		if( offerRequest.getLimitType() == LimitType.ACCOUNT_LIMIT )
		{
			if( offerRequest.getNewLimit() > account.getAccountLimit() )
			{
				offerRequest.setAccount( account );
			    return offerRepo.save( offerRequest );				
			}
			
			else
			{
				System.out.print("New Account Limit is less than current Account Limit");
			}
		}
		
		else if(offerRequest.getLimitType()==LimitType.PER_TRANSACTION_LIMIT)
		{
			if( offerRequest.getNewLimit() > account.getPerTransactionLimit() )
			{
				offerRequest.setAccount( account );
			    return offerRepo.save( offerRequest );				
			}
			else
			{
				System.out.print("New Per-Transaction Limit is less than current Per-Transaction Limit");
			}
		}
		
		return null;
	}
	
	public List<Offer> getLimitOffer( Long accountId) 
	{
		Account account = accountRepo.findById( accountId ).orElse( null );
		
		if( account == null )
		{
			System.out.println( "Account not found" );
			return null;
		}
		
	    return offerRepo.findActiveOffers( accountId );
	}
	
	public Offer actLimitOffer(  Long limitId, Status status) 
	{
		Offer offer = offerRepo.findById(limitId).orElse(null);
		
		if( offer == null )
		{
			System.out.println("Offer not found");
			return null;
		}
		
		if( new Date().compareTo( offer.getOfferActivationTime() ) < 0 
				|| new Date().compareTo(offer.getOfferExpiryTime()) > 0 
				|| offer.getStatus() != null )
		{
			System.out.println("Offer no longer valid");
			return null;			
		}
		
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
