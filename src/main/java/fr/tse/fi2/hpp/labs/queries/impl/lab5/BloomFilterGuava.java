package fr.tse.fi2.hpp.labs.queries.impl.lab5;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.PrimitiveSink;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class BloomFilterGuava extends AbstractQueryProcessor {
	
	private BloomFilter<DebsRecord> rec;
	
	public BloomFilterGuava(QueryProcessorMeasure measure) {
		super(measure);
		// TODO Auto-generated constructor stub
	}
	
	Funnel<DebsRecord> personFunnel = new Funnel<DebsRecord>() {
		@Override
	     public void funnel(DebsRecord person, PrimitiveSink into) {
	       into.putFloat(person.getPickup_latitude())
	           .putFloat(person.getPickup_longitude())
	           .putFloat(person.getDropoff_latitude())
	           .putFloat(person.getDropoff_longitude())
	           .putString(person.getHack_license(), Charsets.UTF_8);
	     }
	   };
	
	@Override
	protected void process(DebsRecord record) {
		// TODO Auto-generated method stub
		rec = BloomFilter.create(personFunnel, 1000, 0.001);
		for (DebsRecord debsRecord : eventqueue) {
			rec.put(debsRecord);
		}
	}
	
	public int check(DebsRecord recherche)
	{
		int nb = 0;
		if (rec.mightContain(recherche)) {
			nb ++;
			}
		return nb;
	}

}
