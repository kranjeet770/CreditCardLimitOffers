package com.credit.home.services;


import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.credit.home.dao.AccountRepo;
import com.credit.home.entities.Account;

@Service
public class AccountService {
	private final AccountRepo accountRepo;
	
	public AccountService(AccountRepo accountRepo) {
		super();
		this.accountRepo = accountRepo;
	}

	public Account addAccount( @Validated @RequestBody Account account )
	{
		if( accountRepo.existsById( account.getAccountId() ))
		{
			System.out.println( "Account Id already exists" );
			return null;			
		}
		
		return accountRepo.save( account );
	}
	
	public Account getAccount(@PathVariable Long accountId)
	{
		Account account = accountRepo.findById( accountId ).orElse(null);
		
		if( account == null )
		{
			System.out.println( "Account not found" );
			return null;
		}
		
		return account;
	}
}
