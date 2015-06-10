package fr.tse.fi2.hpp.labs.queries.impl.projet;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class Query2 extends AbstractQueryProcessor {

	// recs15 stock tous les DebsRecord ayant eu lieu dans les 15 dernières
	// minutes
	private static LinkedList<DebsRecord> recs15 = new LinkedList<>();
	// recs30 stock tous les debsRecord ayant eu lieu dans les 30 dernières
	// minutes
	private static LinkedList<DebsRecord> recs30 = new LinkedList<>();
	private static LinkedList<DebsRecord> deletedRecs15 = new LinkedList<>();
	private static LinkedList<DebsRecord> deletedRecs30 = new LinkedList<>();

	// elements de recsRentable au format (longitudeCell, latitudeCell,
	// taxiVides, Mediane, profit)
	// va permettre de stocker toutes les cellules rencontrées dans les 30
	// dernière minutes
	private static LinkedList<RecRentable> recsRentable = new LinkedList<>();

	private static long lastTime;
	private static long firstTime15;
	private static int depX, depY, arrX, arrY;
	private String sortie;

	public Query2(QueryProcessorMeasure measure) {
		super(measure);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void process(DebsRecord record) {
		// TODO Auto-generated method stub
		long start = System.nanoTime();
		recs15.add(record);
		recs30.add(record);
		lastTime = record.getDropoff_datetime();
		deletedRecs15.clear();
		deletedRecs30.clear();
		
		while ((lastTime - recs15.getFirst().getDropoff_datetime()) / 60000 > 15) {
			deletedRecs15.add(recs15.getFirst());
			recs15.removeFirst();
		}

		while ((lastTime - recs30.getFirst().getDropoff_datetime()) / 60000 > 30) {
			deletedRecs30.add(recs30.getFirst());
			recs30.removeFirst();
		}
		firstTime15 = recs15.getFirst().getDropoff_datetime();
		majRecsRentable();
		prepareSortie(start, recsRentable);
		// System.out.println(recsRentable.toString());
		this.listsum.add(sortie);
	}

	public void prepareSortie(long start, LinkedList<RecRentable> recsRentablef) {
		Date dd = new Date(firstTime15);
		Date df = new Date(lastTime);
		String listnull = "";
		//System.out.println(recsRentablef.toString());
		Collections.sort(recsRentablef);
		
		int taille = recsRentablef.size();
		if (taille > 10) {
			for (int j = 10; j < taille; j++) {
				recsRentablef.remove(10);
			}
		}
		for (int k = 0; k < 10 - taille; k++) {
			listnull += " , NULL";
		}
		long delay = System.nanoTime() - start;
		sortie = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dd) + " , " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(df) + recsRentablef.toString() + listnull
				+ " , " + delay;
	}

	private void majRecsRentable() {
		// TODO Auto-generated method stub
		// modification due à l'ajout du dernier record
		getCell(recs30.getLast());
		boolean celluleArriv_existe = false;
		boolean celluleDep_existe = false;
		boolean jeSuisLePlusGrand = true;

		// System.out.println(recsRentable.size());
		for (int i = 0; i < recsRentable.size(); i++) {
			// Pour toutes les cellules déjà connues
			// System.out.println("arrX : " + arrX + " arrY : " + arrY);
			if (recsRentable.get(i).getX() == arrX && recsRentable.get(i).getY() == arrY) {
				// Si un taxi est déjà arriver dans cette cellule
				// System.out.println("MAJ cellule arriver");
				if (!(recsRentable.get(i).getiDs().contains(recs30.getLast().getHack_license()))) {
					recsRentable.get(i).setTaxiVide(recsRentable.get(i).getTaxiVide() + 1);
					recsRentable.get(i).getiDs().add(recs30.getLast().getHack_license());
				}
				recsRentable.get(i).setProfit(recsRentable.get(i).getMediane() / recsRentable.get(i).getTaxiVide());
				// System.out.println("Taxi vide : " +
				// recsRentable.get(i).getTaxiVide());
				recsRentable.get(i).setProfit(recsRentable.get(i).getMediane() / recsRentable.get(i).getTaxiVide());
				// On calcule le profit
				celluleArriv_existe = true;
			}
			if (recsRentable.get(i).getX() == depX && recsRentable.get(i).getY() == depY) {
				// Si un taxi est déjà partie de cette cellule
				for (int j = 0; j < recsRentable.get(i).getFares().size(); j++) {
					// Pour tous les tarifs payé dans cette cellule
					// On les trie au fur et à mesure
					if (recs30.getLast().getFare_amount() + recs30.getLast().getTip_amount() < recsRentable.get(i).getFares().get(j)) {
						// Si le tarifs est plus petit qu'un tarif déjà existant
						// on l'insert juste avant lui
						recsRentable.get(i).getFares().add(j, (double) (recs30.getLast().getFare_amount() + recs30.getLast().getTip_amount()));
						celluleDep_existe = true;
						jeSuisLePlusGrand = false;
						break;
					}

				}
				if (jeSuisLePlusGrand == true) {
					// Si le tarif est le plus grand trouvé jusqu'à présent on
					// le met à la fin
					recsRentable.get(i).getFares().add((double) (recs30.getLast().getFare_amount() + recs30.getLast().getTip_amount()));
					celluleDep_existe = true;
				}
				recsRentable.get(i).setMediane(recsRentable.get(i).getFares().get(recsRentable.get(i).getFares().size() / 2));
				//System.out.println(recsRentable.get(i).getX() + " " + recsRentable.get(i).getY() + " Mediane : " + recsRentable.get(i).getMediane());
				if (!(recsRentable.get(i).getTaxiVide() == 0)) {
					recsRentable.get(i).setProfit(recsRentable.get(i).getMediane() / recsRentable.get(i).getTaxiVide());
				}
			}
		}

		if (celluleArriv_existe == false) {
			// Si c'est la première fois qu'un taxi arrive dans une cellule on
			// l'a créer
			RecRentable recRentable = new RecRentable();
			recRentable.setX(arrX);
			recRentable.setY(arrY);
			recRentable.getiDs().add(recs30.getLast().getHack_license());
			recRentable.setTaxiVide(1);
			recsRentable.add(recRentable);
			//System.out.println("creation cellule arrivé : " + recRentable);
		}

		if (celluleDep_existe == false) {
			// Si c'est la première fois qu'un taxi part d'une cellule on l'a
			// créer
			RecRentable recRentable = new RecRentable();
			recRentable.setX(depX);
			recRentable.setY(depY);
			recRentable.getFares().add((double) (recs30.getLast().getFare_amount() + recs30.getLast().getTip_amount()));
			recRentable.setMediane(recRentable.getFares().get(recRentable.getFares().size() / 2));
			recRentable.setProfit(recRentable.getMediane() / recRentable.getTaxiVide());
			recsRentable.add(recRentable);
			//System.out.println("creation cellule départ : " + recRentable);
		}
		// modification due aux supppressions des taxi -30 min
		for (int i = 0; i < deletedRecs30.size(); i++) {
			getCell(deletedRecs30.get(i));
			// comparaison à toute les cellules exisatantes.
			for (int j = 0; j < recsRentable.size(); j++) {
				if (recsRentable.get(j).getX() == arrX && recsRentable.get(j).getY() == arrY) {
					// on supprime un taxi vide si besoin
					if ((recsRentable.get(j).getiDs().contains(recs30.get(i).getHack_license()))) {
						recsRentable.get(j).setTaxiVide(recsRentable.get(j).getTaxiVide() - 1);
						recsRentable.get(i).getiDs().remove(recs30.getLast().getHack_license());
					}
					// suppression de la case si elle est vide
					if (recsRentable.get(j).getMediane() == 0 && recsRentable.get(j).getTaxiVide() == 0) {
						recsRentable.remove(j);
						j--;
						// sinon recalcul du profit (si possible)
					} else if (!(recsRentable.get(j).getTaxiVide() == 0)) {
						recsRentable.get(j).setProfit(recsRentable.get(j).getMediane() / recsRentable.get(j).getTaxiVide());
					}
					// pas de calcul de profit si nm de taxi vide=0
					else {
						recsRentable.get(j).setProfit(0.0);
					}
					break;
				}
			}

		}

		// modification dues aux supppressions des taxi -15min

		for (int i = 0; i < deletedRecs15.size(); i++) {
			getCell(deletedRecs15.get(i));

			// on les compare à chaque élément du tableau
			for (int j = 0; j < recsRentable.size(); j++) {
				if (recsRentable.get(j).getX() == depX && recsRentable.get(j).getY() == depY) {
					// suppression du fare correspondant au taxi
					for (int k = 0; k < recsRentable.get(j).getFares().size(); k++) {
						if (deletedRecs15.get(i).getFare_amount() + deletedRecs15.get(i).getTip_amount() == recsRentable.get(j).getFares().get(k)) {
							recsRentable.get(j).getFares().remove(k);

							break;
						}
					}

					// recalcul de la médiane
					if (recsRentable.get(j).getFares().isEmpty()) {
						recsRentable.get(j).setMediane((double) 0);
					} else {
						recsRentable.get(j).setMediane(recsRentable.get(j).getFares().get(recsRentable.get(j).getFares().size() / 2));
					}
					// suppression de la case si elle est vide
					if (recsRentable.get(j).getMediane() == 0 && recsRentable.get(j).getTaxiVide() == 0) {
						recsRentable.remove(j);
					}
					// Sinon recalcule de profit
					else if (!(recsRentable.get(j).getTaxiVide() == 0)) {
						recsRentable.get(j).setProfit(recsRentable.get(j).getMediane() / recsRentable.get(j).getTaxiVide());
					}
					// pas de calcul de profit si nm de taxi vide=0
					else {
						recsRentable.get(j).setProfit(0.0);
					}
				}
			}
		}
	}

	public static void getCell(DebsRecord record) {

		/*
		 * Le grille fait 600 case x 600 cases. Chaque case fait 250m x 250m
		 * Coordonnées de la 1ère case : ?,? 250 m to South = 0.002245778
		 * (lattitude) 250 m to East = 0.002993 (longitude)
		 */

		double pickup_longitude = record.getPickup_longitude();
		double pickup_latitude = record.getPickup_latitude();
		double dropoff_longitude = record.getDropoff_longitude();
		double dropoff_latitude = record.getDropoff_latitude();
		
		depX = ((int) ((pickup_longitude + 74.913585 + 0.005986 / 4)/ 0.002993 + 1));
		depY = ((int) ((-(pickup_latitude - 41.474937 - 0.004491556 / 4))/ 0.002245778 + 1));
		arrX = ((int) ((dropoff_longitude + 74.913585 + 0.005986 / 4)/ 0.002993 + 1));
		arrY = ((int) ((-(dropoff_latitude - 41.474937 - 0.004491556 / 4))/ 0.002245778 + 1));
	}

	public static int getProfit(LinkedList<DebsRecord> allRecs, int x, int y) {
		LinkedList<DebsRecord> recsLast15 = new LinkedList<>();
		while (lastTime - recsLast15.getFirst().getDropoff_datetime() / 60000 > 15) {
			recsLast15.removeFirst();
		}
		for (int i = 0; i < recsLast15.size(); i++) {
			if (!(x == depX && y == depY)) {
				recsLast15.remove(i);
				i--;
			}
		}
		return 0;
	}
}
