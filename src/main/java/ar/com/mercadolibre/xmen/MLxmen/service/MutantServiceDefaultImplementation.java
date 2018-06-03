package ar.com.mercadolibre.xmen.MLxmen.service;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
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
			map(mapMultipleMatchesToIndividualElements()).
			flatMap(Function.identity()).
			limit(2).
			count();
		return matches<2 ? false : true ;
	}

	private Function<String, Stream<String>> mapMultipleMatchesToIndividualElements() {
		return k -> {
			return 	IntStream.
					range(0, DNA.numberOfMatches(k)).
					boxed().
					parallel().
					map(String::valueOf);
		};
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
			String diagonalDesc = 
					IntStream.
					rangeClosed(0, k).
					filter(j->(k - j) < dna.length && j < dna.length).
					mapToObj(f->String.valueOf(dna[k - f].charAt(f))).
					collect(Collectors.joining());

			String diagonalAsc = 
					IntStream.
					rangeClosed(0, k).
					filter(j->(k - j) < dna.length && j < dna.length).
					mapToObj(f->String.valueOf(dna[dna.length - 1 - (k - f)].charAt(f))).
					collect(Collectors.joining());

			return new String[] {diagonalDesc,diagonalAsc};

		};
	}
	
	private Stream<String> buildStreamForVerticalMatch(String[] dna) {
		return IntStream.
				range(0, dna.length).
				boxed().
				parallel().
				map(mapHorizontalCharactersToVertical(dna));
			}

	private Function<Integer,String> mapHorizontalCharactersToVertical(String[] dna) {
		return t -> {
			return IntStream.
					range(0, dna.length).
					mapToObj(f->String.valueOf(dna[f].charAt(t))).
					collect(Collectors.joining());
		};
	}

	private void validate(String[] dna) {
		Optional<String> dnaWithoutSameLenght= Stream.of(dna).
				parallel().
				filter(p->p.length()!=dna.length).
				findAny();
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
		Stats stats = new Stats(totalMutantRecords, totalRecords);
		return stats;
	}

	@Override
	public void save(DNA dna) {
		DNADao.save(dna);
	}



}
