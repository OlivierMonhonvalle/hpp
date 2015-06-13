package fr.tse.fi2.hpp.labs.queries.impl.projet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class Query1 extends AbstractQueryProcessor {

	// Tableau contenant tous les debsRecord des 30 dernières minutes
	private static LinkedList<DebsRecord> recs = new LinkedList<>();
	// Tableau contenant tous les cellules des debsRecord des 30 dernières
	// minutes
	private static HashMap<ArrayList<Integer>, Integer> recsCell = new HashMap<ArrayList<Integer>, Integer>();
	// Première date des DebsRecord présent dans recs
	private static long firstTime;
	// Dernière date des DebsRecord présent dans recs
	private static long lastTime;
	// Valeur ecrite dans le fichier
	private String sortie;

	public Query1(QueryProcessorMeasure measure) {
		super(measure);
		// TODO Auto-generated constructor stub
	}

	// Fonction permettant de recuperer toutes les DebsRecord ayant eu lieu les
	// 30 dernieres minutes

	@Override
	protected void process(DebsRecord record) {
		// TODO Auto-generated method stub
		long start = System.nanoTime();
		recs.add(record);
		lastTime = record.getDropoff_datetime();
		addCell(record);
		while ((lastTime - recs.getFirst().getDropoff_datetime()) / 60000 > 30) {
			lowerCell(recs.getFirst());
			recs.removeFirst();
		}
		firstTime = recs.getFirst().getDropoff_datetime();
		prepareSortie(start, count());
		// écriture de la sortie dans un Thread
		this.listsum.add(sortie);
	}

	// Fonction permettant de préparer l'écriture de la sortie

	public void prepareSortie(long start, ArrayList<ArrayList<Integer>> list) {
		Date dd = new Date(firstTime);
		Date df = new Date(lastTime);
		String listnull = "";
		int taille = list.size();
		if (taille > 10) {
			for (int j = 10; j < taille; j++) {
				list.remove(10);
			}
		}
		for (int k = 0; k < 10 - taille; k++) {
			listnull += ",NULL";
		}
		String formatedString = list.toString()
			    .replace(", ", ",") 
			    .replace("[", "") 
			    .replace("]", "") 
			    .trim();      
		long delay = System.nanoTime() - start;
		sortie = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dd) + "," + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(df) + "," + formatedString + listnull + "," + delay;
	}

	public static void addCell(DebsRecord record) {
		ArrayList<Integer> array = toArray(record);
		if (recsCell.containsKey(array)) {
			recsCell.put(array, recsCell.get(array) + 1);
		} else {
			recsCell.put(array, 1);
		}
	}

	// Fonction permettant de compter le nombre de Taxis faisant le même
	// parcours

	public static ArrayList<ArrayList<Integer>> count() {

		ValueComparator bvc = new ValueComparator(recsCell);
		TreeMap<ArrayList<Integer>, Integer> sorted_map = new TreeMap<ArrayList<Integer>, Integer>(bvc);
		sorted_map.putAll(recsCell);

		ArrayList<ArrayList<Integer>> sorted_List = new ArrayList<>();
		for (ArrayList<Integer> key : sorted_map.keySet()) {
			sorted_List.add(key);
		}
		return sorted_List;
	}

	static ArrayList<Integer> toArray(DebsRecord last) {
		/*
		 * Le grille fait 300 case x 300 cases. Chaque case fait 500m x 500m
		 * Coordonnées de la 1ère case : 41.474937, -74.913585 500 m to South =
		 * 0.004491556 (lattitude) 500 m to East = 0.005986 (longitude)
		 */
		ArrayList<Integer> array = new ArrayList<Integer>();
		array.add((int) ((last.getPickup_longitude() + 74.913585 + 0.005986 / 2) / 0.005986 + 1));
		array.add((int) ((-(last.getPickup_latitude() - 41.474937 - 0.004491556 / 2)) / 0.004491556 + 1));
		array.add((int) ((last.getDropoff_longitude() + 74.913585 + 0.005986 / 2) / 0.005986 + 1));
		array.add((int) ((-(last.getDropoff_latitude() - 41.474937 - 0.004491556 / 2)) / 0.004491556 + 1));
		return array;
	}

	// Fonction permettant de classer les routes de la plus fréquentée à la
	// moins fréquentée

	static class ValueComparator implements Comparator<ArrayList<Integer>> {

		Map<ArrayList<Integer>, Integer> base;

		public ValueComparator(HashMap<ArrayList<Integer>, Integer> recsCellCount) {
			this.base = recsCellCount;
		}

		@Override
		public int compare(ArrayList o1, ArrayList o2) {
			// TODO Auto-generated method stub
			if (base.get(o1) > base.get(o2)) {
				return -1;
			} else {
				return 1;
			}
		}
	}

	private void lowerCell(DebsRecord last) {
		// TODO Auto-generated method stub
		ArrayList<Integer> arrayLast = toArray(last);
		recsCell.put(arrayLast, recsCell.get(arrayLast) - 1);

		if (recsCell.get(arrayLast) == 0) {
			recsCell.remove(arrayLast);
		}

	}
}
