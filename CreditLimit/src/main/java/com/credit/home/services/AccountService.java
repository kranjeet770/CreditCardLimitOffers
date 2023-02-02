package com.credit.home.services;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.credit.home.dao.AccountRepo;
import com.credit.home.entities.Account;
import com.credit.home.exceptions.ResourceAlreadyExistsException;
import com.credit.home.exceptions.ResourceNotFoundException;

@Service
public class AccountService {
	private final AccountRepo accountRepo;
	
	public AccountService( AccountRepo accountRepo ) {
		super();
		this.accountRepo = accountRepo;
	}

	public ResponseEntity<Account> addAccount( @RequestBody Account account )
	{
		if( accountRepo.existsById( account.getAccountId() ) )
		{
			throw new ResourceAlreadyExistsException( "Account Id: " + account.getAccountId() + " already exists" );		
		}
		
		accountRepo.save( account );
		return new ResponseEntity<>(account, HttpStatus.OK);
	}
	
	public ResponseEntity<Account> getAccount(@PathVariable Long accountId)
	{
		Account account = accountRepo.findById( accountId )
				.orElseThrow( () -> new ResourceNotFoundException( "Account Id: " + accountId + " Not found" ) );
		return new ResponseEntity<>(account, HttpStatus.OK);
	}
}
