package ar.com.mercadolibre.xmen.MLxmen.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ar.com.mercadolibre.xmen.MLxmen.model.DNA;
import ar.com.mercadolibre.xmen.MLxmen.model.Stats;
import ar.com.mercadolibre.xmen.MLxmen.service.MutantService;

@RestController
public class XMENRest {

	@Autowired
	MutantService mutantService;

	@RequestMapping(value = "/mutant/", 
			method = RequestMethod.POST, 
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<String> mutant(@RequestBody DNA dna) {
		boolean isDnaMutant = mutantService.isMutant(dna.getDna());
		dna.setMutant(isDnaMutant);
		mutantService.save(dna);
		if (isDnaMutant) {
			return ResponseEntity.status(HttpStatus.OK).body("DNA is mutant");
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("DNA is NOT mutant");
		}
	}

	@RequestMapping(value = "/stats", 
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public Stats stats() {
		return mutantService.getStats();
	}
}