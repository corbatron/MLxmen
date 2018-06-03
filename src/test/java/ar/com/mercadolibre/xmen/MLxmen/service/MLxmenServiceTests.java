package ar.com.mercadolibre.xmen.MLxmen.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ar.com.mercadolibre.xmen.MLxmen.model.Stats;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MLxmenServiceTests {

	@Autowired
	MutantService mutantService;
	
	@Test
	public void contextLoads() {
	}
	

	@Test
	public void postSample() {
		Stats s = mutantService.getStats();
		assertNotNull(s);
	}

}
