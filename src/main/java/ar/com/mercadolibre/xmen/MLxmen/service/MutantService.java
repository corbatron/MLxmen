package ar.com.mercadolibre.xmen.MLxmen.service;

import ar.com.mercadolibre.xmen.MLxmen.model.DNA;
import ar.com.mercadolibre.xmen.MLxmen.model.Stats;

public interface MutantService {
	public boolean isMutant(String[] dna);	
	public Stats getStats();
	public void save(DNA dna);
}
