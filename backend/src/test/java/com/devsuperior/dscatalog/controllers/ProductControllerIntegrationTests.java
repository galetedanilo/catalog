package com.devsuperior.dscatalog.controllers;

import com.devsuperior.dscatalog.requests.ProductRequest;
import com.devsuperior.dscatalog.tests.Factory;
import com.devsuperior.dscatalog.tests.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductControllerIntegrationTests {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private TokenUtil tokenUtil;
	
	private Long existingId;
	private Long nonExistingId;
	private Long countTotalProducts;
	
	private String adminUsername;
	private String adminPassword;
	
	@BeforeEach
	public void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalProducts = 25L;
		
		adminUsername = "maria@gmail.com";
		adminPassword = "123456";
	}
	
	@Test
	public void updateProductShouldReturnProductDTOWhenIdExists() throws Exception {
		
		ProductRequest productRequest = Factory.createProductRequest();
		
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);
		
		String jsonBody = objectMapper.writeValueAsString(productRequest);
		String expectedName = productRequest.getName();
	
		ResultActions result = mockMvc.perform(put("/api/v1/products/{id}", existingId)
				.header("Authorization", "Bearer" + accessToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").value(existingId));
		result.andExpect(jsonPath("$.name").value(expectedName));		
	}
	
	@Test
	public void updateProductShouldThrowNotFoundWhenIdDoesNotExisting() throws Exception {
		
		ProductRequest productRequest = Factory.createProductRequest();
		
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);
		
		String jsonBody = objectMapper.writeValueAsString(productRequest);
	
		ResultActions result = mockMvc.perform(put("/api/v1/products/{id}", nonExistingId)
				.header("Authorization", "Bearer" + accessToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());		
	}
	
	@Test
	public void findAllProductsShouldReturnSortedPageWhenSortById()  throws Exception {
		
		ResultActions result = mockMvc.perform(get("/api/v1/products?page=0&size=12&sort=name,asc")
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.totalElements").value(countTotalProducts));
		result.andExpect(jsonPath("$.content").exists());
		result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
	}

}
