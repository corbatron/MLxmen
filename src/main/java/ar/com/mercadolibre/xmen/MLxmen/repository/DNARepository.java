package ar.com.mercadolibre.xmen.MLxmen.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import ar.com.mercadolibre.xmen.MLxmen.model.DNA;

public interface DNARepository extends MongoRepository<DNA, String> { 

	
}
