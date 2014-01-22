package com.mycompany.salesapp;

public class Sale {
	private double price;
	private Product product;
	
	public Sale(double price, Product product) {
		super();
		this.price = price;
		this.product = product;
	}
	
	public Sale() {
		// TODO Auto-generated constructor stub
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public Product getProduct() {
		return product;
	}
	
	public void setProduct(Product product) {
		this.product = product;
	}
}
