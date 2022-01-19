package com.devsuperior.dscatalog.controllers;

import java.util.List;

import com.devsuperior.dscatalog.responses.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import com.devsuperior.dscatalog.tests.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTests {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ProductService productService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private TokenUtil tokenUtil;
	
	private ProductResponse productResponse;
	
	private PageImpl<ProductResponse> page;
	
	private Long existingId;
	private Long nonExistingId;
	private Long dependentId;
	
	private String adminUsername;
	private String adminPassword;
	
	@BeforeEach
	public void setUp() throws Exception {
		
		existingId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;
		
		adminUsername = "maria@gmail.com";
		adminPassword = "123456";
		
		productResponse = Factory.createProductResponse();
		page = new PageImpl<>(List.of(productResponse));
		
		Mockito.when(productService.findAllProducts(ArgumentMatchers.any(),
				ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(page);
		
		Mockito.when(productService.findProductByPrimaryKey(existingId)).thenReturn(productResponse);
		Mockito.when(productService.findProductByPrimaryKey(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		
		Mockito.when(productService.updateProduct(ArgumentMatchers.eq(existingId), ArgumentMatchers.any())).thenReturn(productResponse);
		Mockito.when(productService.updateProduct(ArgumentMatchers.eq(nonExistingId), ArgumentMatchers.any())).thenThrow(ResourceNotFoundException.class);
		
		Mockito.when(productService.saveNewProduct(ArgumentMatchers.any())).thenReturn(productResponse);
		
		Mockito.doNothing().when(productService).deleteProductByPrimaryKey(existingId);
		Mockito.doThrow(ResourceNotFoundException.class).when(productService).deleteProductByPrimaryKey(nonExistingId);
		Mockito.doThrow(DatabaseException.class).when(productService).deleteProductByPrimaryKey(dependentId);
	}
	
	@Test
	public void saveNewProductShouldReturnProductResponse() throws Exception {
		
		String jsonBody = objectMapper.writeValueAsString(productResponse);
		
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);
		
		ResultActions resultActions =mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/")
				.header("Authorization", "Bearer" + accessToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		//Código 201
		resultActions.andExpect(MockMvcResultMatchers.status().isCreated());
		resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
	}
	
	@Test
	public void deleteProductShouldReturnNothingWhenIdExists() throws Exception {
		
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);
		
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/products/{id}", existingId)
				.header("Authorization", "Bearer" + accessToken)
				.accept(MediaType.APPLICATION_JSON));
		
		resultActions.andExpect(MockMvcResultMatchers.status().isNoContent());
	}
	
	@Test
	public void deleteProductShouldThrowsResourceNotFoundExceptionWhenIdDoesNotExisting() throws Exception {
		
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);
		
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/products/{id}", nonExistingId)
				.header("Authorization", "Bearer" + accessToken)
				.accept(MediaType.APPLICATION_JSON));
		
		resultActions.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
	
	@Test
	public void deleteProductShouldThrowsDatabaseExceptionWhenPedenpentId() throws Exception {
		
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);
		
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/products/{id}", dependentId)
				.header("Authorization", "Bearer" + accessToken)
				.accept(MediaType.APPLICATION_JSON));
		
		resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
	
	@Test
	public void updateProductShouldReturnProductDTOWhenIdExists() throws Exception {
		
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);
		
		String jsonBody = objectMapper.writeValueAsString(productResponse);
		
		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/products/{id}", existingId)
				.header("Authorization", "Bearer" + accessToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
		result.andExpect(MockMvcResultMatchers.jsonPath("$.name").exists());
		result.andExpect(MockMvcResultMatchers.jsonPath("$.description").exists());
	}
	
	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExisting() throws Exception {
		
		String jsonBody = objectMapper.writeValueAsString(productResponse);
		
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);
		
		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}", nonExistingId)
				.header("Authorization", "Bearer" + accessToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
	
	@Test
	public void findByIdShouldReturnProductDTOWhenIdExists() throws Exception {
		
		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}", existingId)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(MockMvcResultMatchers.status().isOk());
		
		result.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
		result.andExpect(MockMvcResultMatchers.jsonPath("$.name").exists());
		result.andExpect(MockMvcResultMatchers.jsonPath("$.description").exists());
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExisting() throws Exception {
		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}", nonExistingId)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
	
	@Test
	public void findAllShouldReturnPage() throws Exception {
		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/products")
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(MockMvcResultMatchers.status().isOk());
	}
}
