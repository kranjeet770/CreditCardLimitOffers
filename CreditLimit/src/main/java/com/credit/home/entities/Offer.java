package com.credit.home.entities;

import java.util.Date;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.credit.home.enums.LimitType;
import com.credit.home.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;

@Entity
@Transactional
public class Offer {
	
	@Id
	private Long limitId;
	private double newLimit;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date offerActivationTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date offerExpiryTime;
	
	@Enumerated(EnumType.STRING)
	private LimitType limitType;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "account_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private Account account;

	@Enumerated(EnumType.STRING)
	private Status status;

	public LimitType getLimitType() {
		return limitType;
	}

	public void setLimitType(LimitType limitType) {
		this.limitType = limitType;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Long getLimitId() {
		return limitId;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public void setLimitId(Long limitId) {
		this.limitId = limitId;
	}

	public double getNewLimit() {
		return newLimit;
	}

	public void setNewLimit(double newLimit) {
		this.newLimit = newLimit;
	}

	public Date getOfferActivationTime() {
		return offerActivationTime;
	}

	public void setOfferActivationTime(Date offerActivationTime) {
		this.offerActivationTime = offerActivationTime;
	}

	public Date getOfferExpiryTime() {
		return offerExpiryTime;
	}

	public void setOfferExpiryTime(Date offerExpiryTime) {
		this.offerExpiryTime = offerExpiryTime;
	}

	@Override
	public String toString() {
		return "Limit [limitId=" + limitId + ", newLimit=" + newLimit + ", offerActivationTime=" + offerActivationTime
				+ ", offerExpiryTime=" + offerExpiryTime + "]";
	}

	
}
