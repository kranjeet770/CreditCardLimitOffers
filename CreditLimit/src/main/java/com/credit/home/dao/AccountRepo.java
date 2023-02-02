package com.credit.home.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.credit.home.entities.Account;

public interface AccountRepo extends JpaRepository<Account, Long> 
{
	
}
