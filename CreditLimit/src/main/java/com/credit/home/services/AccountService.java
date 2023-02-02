package com.credit.home.services;

import java.util.Optional;

import org.springframework.stereotype.Service;
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

	public Account addAccount(@RequestBody Account account)
	{
		System.out.println(account);
		accountRepo.save(account);
		return account;
	}
	
	public Optional<Account> getAccount(@PathVariable Long accountId)
	{
		System.out.println(accountId);
		Optional<Account> account = accountRepo.findById(accountId);
		return account;
	}
}
