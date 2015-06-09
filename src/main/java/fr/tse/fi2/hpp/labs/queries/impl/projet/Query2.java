package fr.tse.fi2.hpp.labs.queries.impl.projet;

import java.util.ArrayList;
import java.util.LinkedList;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class Query2 extends AbstractQueryProcessor {
	
	private int emptytaxi;
	private float mediane;
	private LinkedList<DebsRecord> recs = new LinkedList();
	private LinkedList<CreationTabCell> recs2 = new LinkedList();
	private static LinkedList recsCell = new LinkedList();
	// Première date des DebsRecord présent dans recs
	private static long firstTime;
	// Dernière date des DebsRecord présent dans recs
	private static long lastTime;
	

	public Query2(QueryProcessorMeasure measure) {
		super(measure);
		// TODO Auto-generated constructor stub
		
	}

	@Override
	protected void process(DebsRecord record) {
		// TODO Auto-generated method stub
		recs.add(record);
		lastTime = record.getDropoff_datetime();
		GetCell(record.getPickup_longitude(),record.getPickup_latitude(),record.getDropoff_longitude(),record.getDropoff_latitude());
		while ((lastTime - recs.getFirst().getDropoff_datetime()) / 60000 > 30) {
			recs.removeFirst();
			recsCell.removeFirst();
		}
	}
	
	public static void GetCell(double pickup_longitude, double pickup_latitude,
			double dropoff_longitude, double dropoff_latitude) {
				
		/*
		 * Le grille fait 600 case x 600 cases. Chaque case fait 250m x 250m
		 * Coordonnées de la 1ère case : 20.7374685, -37.4567925 250 m to South =
		 * 0.002245778 (lattitude) 250 m to East = 0.002993 (longitude)
		 */
		ArrayList<Integer> recCell = new ArrayList<>();
		double a = pickup_longitude + 37.4567925 + 0.005986 / 2;
		double xDepDouble = a /0.002993;
		int xDep = (int) xDepDouble + 1;

		a = -(pickup_latitude - 20.7374685 - 0.004491556 / 2);
		double yDepDouble = a /0.002245778;
		int yDep = (int) yDepDouble + 1;

		a = dropoff_longitude + 37.4567925 + 0.005986 / 2;
		double xArrDouble = a / 0.002993;
		int xArr = (int) xArrDouble + 1;

		a = -(dropoff_latitude -20.7374685 - 0.004491556 / 2);
		double yArrDouble = a / 0.002245778;
		int yArr = (int) yArrDouble + 1;

		recCell.add(xDep);
		recCell.add(yDep);
		recCell.add(xArr);
		recCell.add(yArr);
		recsCell.add(recCell);
	}
	
	public void taxiVide(){
		
	}

}
