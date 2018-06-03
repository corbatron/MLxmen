package ar.com.mercadolibre.xmen.MLxmen.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

import ar.com.mercadolibre.xmen.MLxmen.model.DNA;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class XMENTRestTest {

	@Autowired
	private TestRestTemplate template;

	@Rule
	public ExpectedException thrown = ExpectedException.none();


	@Test
	public void nonNxNDNAShouldReturn500() throws Throwable {
		String[] dnaString = { 
				"AAAAAAAAAAAAAAAAAAAA", 
				"CAGTGC", 
				"TTATGT", 
				"AGAAGG", 
				"CCCCTA", 
				"TCACTG" };			
		ResponseEntity<String> result = doPost(dnaString);
		assertEquals(result.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@Test
	public void mutantShouldReturn200() throws Throwable {
		String[] dnaString = { 
				"ATGCGA", 
				"CAGTGC", 
				"TTATGT", 
				"AGAAGG", 
				"CCCCTA", 
				"TCACTG" };			
		ResponseEntity<String> result = doPost(dnaString);
		assertNotNull(result);
		assertEquals(result.getStatusCode(), HttpStatus.OK);
	}

	@Test
	public void mutant8x8DnaShouldReturn200() throws Throwable {
		String[] dnaString = {
				"AXXXXXXX",
				"XAXXXXXX",
				"XXAXXXXX",
				"XXXAXXXX",
				"XXXXTXXX",
				"XXXXXTXX",
				"XXXXXXTX",
				"FFFFFFFT" };			
		ResponseEntity<String> result = doPost(dnaString);
		assertNotNull(result);
		assertEquals(result.getStatusCode(), HttpStatus.OK);
	}

	
	
	@Test
	public void nonMutantShouldReturn403() throws Throwable {
		String[] dnaString = { 
				"AAACCC", 
				"TTTGGG", 
				"AAACCC", 
				"TTTGGG", 
				"AAACCC", 
				"TTTGGG" };
		ResponseEntity<String> result = doPost(dnaString);
		assertNotNull(result);
		assertEquals(result.getStatusCode(), HttpStatus.FORBIDDEN);
	}

	@Test
	public void notExposedURIshouldReturn404() {
		ResponseEntity<String> result = this.template.postForEntity("/notExposed", "", String.class);
		assertNotNull(result);
		assertEquals(result.getStatusCode(), HttpStatus.NOT_FOUND);
	}

	@Test
	public void queryStatsShouldReturn200() {
		ResponseEntity<String> result = this.template.getForEntity("/stats", String.class);
		assertNotNull(result.getBody());
		assertEquals(result.getStatusCode(), HttpStatus.OK);
	}

	@Test
	public void queryStatsByPOSTShouldReturn405() {
		ResponseEntity<String> result = this.template.postForEntity("/stats", "", String.class);
		assertNotNull(result);
		assertEquals(result.getStatusCode(), HttpStatus.METHOD_NOT_ALLOWED);
	}

	private ResponseEntity<String> doPost(String[] dnaString) throws Throwable {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		DNA dna = new DNA(dnaString);
		ObjectMapper mapper = new ObjectMapper();
		String requestBody = mapper.writeValueAsString(dna);
		HttpEntity<String> httpEntity = new HttpEntity<String>(requestBody, httpHeaders);
		ResponseEntity<String> result = this.template.postForEntity("/mutant/", httpEntity, String.class);
		return result;
	}

}
