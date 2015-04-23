package fr.tse.fi2.hpp.labs.queries.impl.lab2;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class SumQueryThread1 extends AbstractQueryProcessor {

	
	private float sum2 = 0;
	
	public SumQueryThread1(QueryProcessorMeasure measure) {
		super(measure);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void process(DebsRecord record) {
		// TODO Auto-generated method stub
		sum2 += record.getFare_amount();
		this.listsum.add(sum2);
	}
	

}
