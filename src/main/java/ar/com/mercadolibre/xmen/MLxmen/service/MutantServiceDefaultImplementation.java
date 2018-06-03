package ar.com.mercadolibre.xmen.MLxmen.service;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import ar.com.mercadolibre.xmen.MLxmen.exception.XMENException;
import ar.com.mercadolibre.xmen.MLxmen.model.DNA;
import ar.com.mercadolibre.xmen.MLxmen.model.Stats;
import ar.com.mercadolibre.xmen.MLxmen.repository.DNARepository;

@Service
public class MutantServiceDefaultImplementation implements MutantService {

	@Autowired
	DNARepository DNADao;
	
	public boolean isMutant(String[] dna) {	
		validate(dna);
		Stream<String> horizontalMatch = buildStreamForHorizontalMatch(dna);
		Stream<String> verticalMatch = buildStreamForVerticalMatch(dna);
		Stream<String> diagonalMatch = buildStreamForDiagonalMatch(dna);
		long matches = Stream.of(horizontalMatch, verticalMatch, diagonalMatch).
			flatMap(Function.identity()).
			filter(DNA::matchMutantPattern).
			map(DNA::numberOfMatches).
			limit(2).
			count();	
		return matches==0 ? false : true ;
	}

	private Stream<String> buildStreamForDiagonalMatch(String[] dna) {
		return IntStream.
				range(3, (dna.length * 2) - 4).
				boxed().
				parallel().
				map(mapHorizontalWordsToDiagonals(dna)).
				flatMap(Arrays::stream);
	}

	private Function<Integer, String[]> mapHorizontalWordsToDiagonals(String[] dna) {
		return k -> {	
			StringBuffer bf1 = new StringBuffer();
			StringBuffer bf2 = new StringBuffer();
			for (int j = 0; j <= k; j++) {
				int i = k - j;		
				if (i < dna.length && j < dna.length) {
					bf1.append(dna[i].charAt(j));
					bf2.append(dna[dna.length - 1 - i].charAt(j));
				}
			}
			String[] s = { bf1.toString(), bf2.toString() };
			return s;
		};
	}

	private Stream<String> buildStreamForVerticalMatch(String[] dna) {
		return IntStream.
				range(0, dna.length).
				boxed().
				parallel().
				map(mapHorizontalWordsToVertical(dna));
	}

	private Function<Integer,String> mapHorizontalWordsToVertical(String[] dna) {
		return t -> {
			StringBuffer sb = new StringBuffer();
			for (int j = 0; j < dna.length; j++) {
				sb.append(dna[j].charAt(t));
			}
			return sb.toString();
		};
	}

	private void validate(String[] dna) {
		Optional<String> dnaWithoutSameLenght= Stream.of(dna).
				parallel().filter(p->p.length()!=dna.length).findAny();
		if(dnaWithoutSameLenght.isPresent()) {
			throw new XMENException("DNA dimension is not NxN");
		}
		
	}
	
	
	private Stream<String> buildStreamForHorizontalMatch(String[] dna) {
		return Stream.of(dna).
				parallel();
	}

	@Override
	@Cacheable(sync=true,value="stats")
	public Stats getStats() {
		long totalRecords = DNADao.count();
		DNA dna = new DNA();
		dna.setMutant(Boolean.TRUE);
		Example<DNA> example = Example.of(dna); 
		long totalMutantRecords = DNADao.count(example);
		return new Stats(totalMutantRecords, totalRecords);
	}

	@Override
	public void save(DNA dna) {
		DNADao.save(dna);
	}



}
