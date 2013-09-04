package com.eventfabric.example.products;

import java.util.LinkedHashMap;
import java.io.IOException;

import com.eventfabric.api.client.Response;
import com.eventfabric.api.client.EventClient;
import com.eventfabric.api.model.Event;
import com.eventfabric.example.products.ui.FrameProducts;

public class App 
{
	public static void main(String[] args) {
		FrameProducts frameProducts = new FrameProducts();
		frameProducts.setVisible(true);
	}
}
