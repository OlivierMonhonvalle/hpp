package fr.tse.fi2.hpp.labs.queries.impl.lab5;

import java.util.BitSet;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;


public class BloomFilterHash extends AbstractQueryProcessor {

	private static Bloom bloom;
	public static BitSet listeHashRoute;
	private int k_hash;

	public BloomFilterHash(QueryProcessorMeasure measure, int nb_octet, int k) {
		super(measure);
		bloom = new Bloom(nb_octet);
		listeHashRoute = new BitSet(nb_octet);
		k_hash = k;
	}

	@Override
	protected void process(DebsRecord record) {
		String result = Bloom.getStringConca(record);
		for(int i =0 ; i<k_hash ; i++){
			listeHashRoute.set(bloom.getIndex(i, result));
		}
	}

	
	public boolean checkroute(DebsRecord rec){
		String result = Bloom.getStringConca(rec);;
		for(int i =0 ; i< k_hash ; i++){
			if(!listeHashRoute.get(bloom.getIndex(i, result))){
				return false;	
			}
		}
			
		return true;		
	}
}