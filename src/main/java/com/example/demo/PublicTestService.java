package com.example.demo;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/public")
public class PublicTestService {
	
	@RequestMapping(value = "/test", produces = "application/json")
	public String test() {
		return "{ \"value\" : \"Pagina publica funcionando\"}";
	}
}
