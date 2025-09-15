package com.fintrack.entity;


public class Balance {

	private double amount;

	public Balance(double amount) {
		this.amount = amount;
	}

	public Balance() { }

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getAmount() {
		return amount;
	}
}