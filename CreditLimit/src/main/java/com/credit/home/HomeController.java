package com.credit.home;


import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import com.credit.home.entities.Account;
import com.credit.home.entities.Offer;
import com.credit.home.enums.Status;
import com.credit.home.services.AccountService;
import com.credit.home.services.OfferService;

@RestController
public class HomeController {
	
	private final AccountService accountService;
	private final OfferService offerService;
	
	public HomeController(AccountService accountService, OfferService offerService) {
		super();
		this.accountService = accountService;
		this.offerService = offerService;
	}


	@PostMapping("/addAccount")
	public Account addAccount(@RequestBody Account account)
	{
		return accountService.addAccount(account);
	}
	
	@GetMapping("/getAccount/{accountId}")
	public Account getAccount(@PathVariable Long accountId)
	{
		return accountService.getAccount(accountId);
	}
	
	@PostMapping("/createLimitOffer/{accountId}")
	public Offer createLimitOffer( @PathVariable(value = "accountId") Long accountId, @RequestBody Offer offerRequest) 
	{
	    return offerService.createLimitOffer(accountId,offerRequest);
	}
	
	@GetMapping("/getLimitOffer/{accountId}")
	public List<Offer> getLimitOffer( @PathVariable(value = "accountId") Long accountId) 
	{
	    return offerService.getLimitOffer(accountId);
	}
	
	@PostMapping("/actLimitOffer/{limitId}/{status}")
	public Offer actLimitOffer( @PathVariable(value = "limitId") Long limitId,
								@PathVariable(value = "status") Status status) 
	{
		return offerService.actLimitOffer(limitId,status);
	}
}
