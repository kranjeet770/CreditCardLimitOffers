package com.credit.home.services;

import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.credit.home.dao.AccountRepo;
import com.credit.home.dao.OfferRepo;
import com.credit.home.entities.Account;
import com.credit.home.entities.Offer;
import com.credit.home.enums.LimitType;
import com.credit.home.enums.Status;
import com.credit.home.exceptions.ForbiddenException;
import com.credit.home.exceptions.ResourceAlreadyExistsException;
import com.credit.home.exceptions.ResourceNotFoundException;

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
	
	public ResponseEntity<Offer> createLimitOffer( Long accountId, Offer offerRequest) 
	{
		Account account = accountRepo.findById( accountId ).
				orElseThrow( () -> new ResourceNotFoundException( "Account Id: " + accountId + " Not found" ) );
		
		if( offerRepo.existsById( offerRequest.getLimitId() ))
		{
			throw new ResourceAlreadyExistsException( "Limit Id: " + offerRequest.getLimitId() + " already exists" );				
		}
		
		if( offerRequest.getOfferActivationTime().compareTo( offerRequest.getOfferExpiryTime() ) > 0
				|| new Date().compareTo( offerRequest.getOfferExpiryTime() ) > 0 )
		{
			throw new ForbiddenException( "Expiry Date not valid" );
		}
		
		if( offerRequest.getLimitType() == LimitType.ACCOUNT_LIMIT )
		{
			if( offerRequest.getNewLimit() > account.getAccountLimit() )
			{
				offerRequest.setAccount( account );
			    offerRepo.save( offerRequest );				
			}
			else
			{
				throw new ForbiddenException( "New Account Limit is less than current Account Limit" );
			}
		}
		
		else if( offerRequest.getLimitType() == LimitType.PER_TRANSACTION_LIMIT )
		{
			if( offerRequest.getNewLimit() > account.getPerTransactionLimit() )
			{
				offerRequest.setAccount( account );
			    offerRepo.save( offerRequest );				
			}
			else
			{
				throw new ForbiddenException( "New Per-Transaction Limit is less than current Per-Transaction Limit" );
			}
		}
		
		return new ResponseEntity<>( offerRequest , HttpStatus.CREATED );
	}
	
	public ResponseEntity<List<Offer>> getLimitOffer( Long accountId ) 
	{	
		if( ! accountRepo.existsById( accountId ) )
		{
			throw new ResourceNotFoundException( "Account Id: " + accountId + " Not found" );
		}
		
		List<Offer> offers = offerRepo.findActiveOffers( accountId );
		
		if( offers.size() == 0 )
		{
			return new ResponseEntity<>( offers , HttpStatus.NO_CONTENT );
		}
		
	    return new ResponseEntity<>( offers , HttpStatus.OK );
	}
	
	public ResponseEntity<Offer> actLimitOffer( Long limitId, Status status ) 
	{
		Offer offer = offerRepo.findById( limitId ).
				orElseThrow( () -> new ResourceNotFoundException( "Limit Id: " + limitId + " Not found" ) );;
		
		if( new Date().compareTo( offer.getOfferActivationTime() ) < 0 
				|| new Date().compareTo(offer.getOfferExpiryTime()) > 0 
				|| offer.getStatus() != null )
		{
			throw new ForbiddenException( "Offer no longer valid" );		
		}
		
		if( status == Status.ACCEPTED )
		{
			offer.setStatus(status);
			Account account = offer.getAccount();
			if(offer.getLimitType()==LimitType.ACCOUNT_LIMIT)
			{
				account.setLastAccountLimit(account.getAccountLimit());
				account.setAccountLimit(offer.getNewLimit());
				account.setAccountLimitUpdateTime(new Date());
			}
			
			else if( offer.getLimitType()==LimitType.PER_TRANSACTION_LIMIT )
			{
				account.setLastPerTransactionLimit( account.getPerTransactionLimit() );
				account.setPerTransactionLimit( offer.getNewLimit() );
				account.setPerTransactionLimitUpdateTime( new Date() );
			}
			offerRepo.save(offer);
		}
		
		return new ResponseEntity<>( offer, HttpStatus.OK );
	}
}
