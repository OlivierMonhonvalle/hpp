package fr.tse.fi2.hpp.labs.queries.impl.projet;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class Compare extends AbstractQueryProcessor {

	private static LinkedList<DebsRecord> recs = new LinkedList<>();
	private static LinkedList<ArrayList<Integer>> recsCell = new LinkedList<>();
	private static long firstTime;
	private static long lastTime;
	private String sortie;

	public Compare(QueryProcessorMeasure measure) {
		super(measure);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void process(DebsRecord record) {
		// TODO Auto-generated method stub
		long start = System.nanoTime();
		recs.add(record);
		lastTime = record.getDropoff_datetime();
		GetCell(record.getPickup_longitude(),record.getPickup_latitude(),record.getDropoff_longitude(),record.getDropoff_latitude());
		while ((lastTime - recs.getFirst().getDropoff_datetime()) / 60000 > 30) {
			recs.removeFirst();
			recsCell.removeFirst();
		}
		prepareSortie(start);
		//System.out.println(Count(recsCell));
		this.listsum.add(sortie);
	}
	
	
	public void prepareSortie(long start){
		Date dd = new Date(firstTime);
		Date df = new Date(lastTime);
		long delay = System.nanoTime() - start;
		sortie = dd +" , "+ df +" , " + delay ;
	}

	public static void GetCell(double pickup_longitude, double pickup_latitude,
			double dropoff_longitude, double dropoff_latitude) {
		/*
		 * Le grille fait 300 case x 300 cases. Chaque case fait 500m x 500m
		 * Coordonnées de la 1ère case : 41.474937, -74.913585 500 m to South =
		 * 0.004491556 (lattitude) 500 m to East = 0.005986 (longitude)
		 */
		ArrayList<Integer> recCell = new ArrayList<>();
		double a = pickup_longitude + 74.913585 + 0.005986 / 2;
		double xDepDouble = a / 0.005986;
		int xDep = (int) xDepDouble + 1;

		a = -(pickup_latitude - 41.474937 - 0.004491556 / 2);
		double yDepDouble = a / 0.004491556;
		int yDep = (int) yDepDouble + 1;

		a = dropoff_longitude + 74.913585 + 0.005986 / 2;
		double xArrDouble = a / 0.005986;
		int xArr = (int) xArrDouble + 1;

		a = -(dropoff_latitude - 41.474937 - 0.004491556 / 2);
		double yArrDouble = a / 0.004491556;
		int yArr = (int) yArrDouble + 1;
		
		recCell.add(xDep);
		recCell.add(yDep);
		recCell.add(xArr);
		recCell.add(yArr);
		recsCell.add(recCell);
	}
	
	public static Hashtable<ArrayList<Integer>, Integer> Count(
			LinkedList<ArrayList<Integer>> recsCell2) {
		Hashtable<ArrayList<Integer>, Integer> recsCellCount = new Hashtable<ArrayList<Integer>, Integer>();
		for (int i = 0; i < recsCell2.size(); i++) {
			if (recsCellCount.containsKey(recsCell2.get(i))) {
				recsCellCount.put(recsCell2.get(i),recsCellCount.get(recsCell2.get(i)) + 1);
			}
			else{
				recsCellCount.put(recsCell2.get(i), 1);
		}
		}
		return recsCellCount;
	}

}
