package com.training.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.training.config.MyConfiguration;
import com.training.model.OrderedBook;
import com.training.proxy.BookProviderProxy;

@RequestMapping("/order")
@RestController
public class OrderController {

	@Autowired
	private MyConfiguration config;
	
	//call  http://localhost:8082/bookstore/book/isbn/1234
	//private String providerUrl = "http://localhost:8082/bookstore/book/isbn/1234";
	
	private String providerUrl="";
	
	@Autowired
	private RestTemplate restTemplate;
	
	@GetMapping("/getorderedbook/isbn/{isbn}/qty/{qty}")
	public OrderedBook getOrderedBook(@PathVariable("isbn") String isbn,@PathVariable("qty") int quantity)
	{
		providerUrl=config.getUrl();
		System.out.println(providerUrl);
		Map<String,String> map = new HashMap<>();
		
		map.put("isbn", isbn);
		
		OrderedBook orderedBook = restTemplate.getForObject(providerUrl, OrderedBook.class, map);
		
		//orderedBook.setQty(quantity);
		//orderedBook.setAmount(quantity * orderedBook.getPrice());
		return orderedBook; 
	}
	
	// using feign client
	@Autowired
	private BookProviderProxy bookProviderProxy;
	
	@GetMapping("/getorderedbookfeign/isbn/{isbn}/qty/{qty}")
	public OrderedBook getOrderedBookFeign(@PathVariable("isbn") String isbn,@PathVariable("qty") int quantity)
	{
		OrderedBook orderedBook = bookProviderProxy.getOrderedBook(isbn);
		
		//orderedBook.setQty(quantity);
		//orderedBook.setAmount(quantity * orderedBook.getPrice());
		
		return orderedBook; 
	}
	
}
