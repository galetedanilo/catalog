package com.devsuperior.dscatalog.resources;

import java.util.List;

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

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import com.devsuperior.dscatalog.tests.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductResourceTests {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ProductService service;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private TokenUtil tokenUtil;
	
	private ProductDTO productDTO;
	
	private PageImpl<ProductDTO> page;
	
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
		
		productDTO = Factory.createProductDTO();
		page = new PageImpl<>(List.of(productDTO));
		
		Mockito.when(service.findAll(ArgumentMatchers.any(), 
				ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(page);
		
		Mockito.when(service.findById(existingId)).thenReturn(productDTO);
		Mockito.when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		
		Mockito.when(service.update(ArgumentMatchers.eq(existingId), ArgumentMatchers.any())).thenReturn(productDTO);
		Mockito.when(service.update(ArgumentMatchers.eq(nonExistingId), ArgumentMatchers.any())).thenThrow(ResourceNotFoundException.class);
		
		Mockito.when(service.insert(ArgumentMatchers.any())).thenReturn(productDTO);
		
		Mockito.doNothing().when(service).delete(existingId);
		Mockito.doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
		Mockito.doThrow(DatabaseException.class).when(service).delete(dependentId);
	}
	
	@Test
	public void insertShouldReturnProuctDTO() throws Exception {
		
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);
		
		ResultActions result =mockMvc.perform(MockMvcRequestBuilders.post("/products/")
				.header("Authorization", "Bearer" + accessToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		//Código 201
		result.andExpect(MockMvcResultMatchers.status().isCreated());
		result.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
	}
	
	@Test
	public void deleteShouldReturnNothingWhenIdExists() throws Exception {
		
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);
		
		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/products/{id}", existingId)
				.header("Authorization", "Bearer" + accessToken)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(MockMvcResultMatchers.status().isNoContent());
	}
	
	@Test
	public void deleteShouldThrowsResourceNotFoundExceptionWhenIdDoesNotExisting() throws Exception {
		
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);
		
		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/products/{id}", nonExistingId)
				.header("Authorization", "Bearer" + accessToken)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
	
	@Test
	public void deleteShouldThrowsDatabaseExceptionWhenPedenpentId() throws Exception {
		
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);
		
		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/products/{id}", dependentId)
				.header("Authorization", "Bearer" + accessToken)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
	
	@Test
	public void updateShouldReturnProductDTOWhenIdExists() throws Exception {
		
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);
		
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}", existingId)
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
		
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
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
