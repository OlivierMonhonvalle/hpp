package fr.tse.fi2.hpp.labs.queries.impl.projet;

import java.util.ArrayList;
import java.util.LinkedList;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class Query2 extends AbstractQueryProcessor {

	// recs15 stock tous les DebsRecord ayant eu lieu dans les 15 dernières minutes
	private static LinkedList<DebsRecord> recs15 = new LinkedList<>();
	// recs30 stock tous les debsRecord ayant eu lieu dans les 30 dernières minutes
	private static LinkedList<DebsRecord> recs30 = new LinkedList<>();
	private static LinkedList<DebsRecord> deletedRecs15 = new LinkedList<>();
	private static LinkedList<DebsRecord> deletedRecs30 = new LinkedList<>();
	
	// elements de recsRentable au format (longitudeCell, latitudeCell, taxiVides, Mediane, profit)
	// va permettre de stocker toutes les cellules rencontrées dans les 30 dernière minutes
	private static LinkedList<RecRentable> recsRentable = new LinkedList<>();
	
	private static long lastTime;
	private static long firstTime15;
	private static int DepX, DepY,ArrX, ArrY;
	

	public Query2(QueryProcessorMeasure measure) {
		super(measure);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void process(DebsRecord record) {
		// TODO Auto-generated method stub

		recs15.add(record);
		recs30.add(record);
		lastTime = record.getDropoff_datetime();
		//deletedRecs15.clear();
		//deletedRecs30.clear();
		firstTime15 = recs15.getFirst().getDropoff_datetime();
		while ((lastTime - recs15.getFirst().getDropoff_datetime()) / 60000 > 15) {
			//deletedRecs15.add(recs15.getFirst());
			recs15.removeFirst();
		}
		
		while ((lastTime - recs30.getFirst().getDropoff_datetime()) / 60000 > 30) {
			//deletedRecs30.add(recs30.getFirst());
			recs30.removeFirst();
		}
		
		majRecsRentable();
		System.out.println(recsRentable.toString());
	}
	
	

	private void majRecsRentable() {
		// TODO Auto-generated method stub
		//modification due à l'ajout du dernier record
		getCell(recs30.getLast());
		boolean celluleArriv_existe = false;
		boolean celluleDep_existe = false;
		boolean jeSuisLePlusGrand = true;
		
		
		for(int i =0; i<recsRentable.size();i++){
			// Pour toutes les cellules déjà connues
			if (recsRentable.get(i).getX()==ArrX &&recsRentable.get(i).getY()==ArrY){
				// Si un taxi est déjà arriver dans cette cellule
				recsRentable.get(i).setTaxiVide(recsRentable.get(i).getTaxiVide()+1); // On incrémente le nombre de taxi vide
				recsRentable.get(i).setProfit(recsRentable.get(i).getMediane()/recsRentable.get(i).getTaxiVide()); // On calcule le profit 
				celluleArriv_existe=true;
			}
			if (recsRentable.get(i).getX()==DepX &&recsRentable.get(i).getY()==DepY){
				// Si un taxi est déjà partie de cette cellule
				for (int j=0 ; j<recsRentable.get(i).getFares().size();j++){
					// Pour tous les tarifs payé dans cette cellule
					// On les trie au fur et à mesure
					if(recs30.getLast().getFare_amount()+recs30.getLast().getTip_amount()<recsRentable.get(i).getFares().get(j)){
						// Si le tarifs est plus petit qu'un tarif déjà existant on l'insert juste avant lui
						recsRentable.get(i).getFares().add(j,(double) (recs30.getLast().getFare_amount()+recs30.getLast().getTip_amount()) );
						celluleDep_existe=true;
						jeSuisLePlusGrand=false;
						break;
					}
					
				}
				if(jeSuisLePlusGrand == true){
					// Si le tarif est le plus grand trouvé jusqu'à présent on le met à la fin
					recsRentable.get(i).getFares().add((double) (recs30.getLast().getFare_amount()+recs30.getLast().getTip_amount()));
					celluleDep_existe=true;
				}
			}
		}
		
		
		if(celluleArriv_existe==false){
			// Si c'est la première fois qu'un taxi arrive dans une cellule on l'a créer
			RecRentable recRentable= new RecRentable();
			recRentable.setX(ArrX);
			recRentable.setY(ArrY);
			recRentable.setTaxiVide(1);
			recsRentable.add(recRentable);
		}
		
		
		if(celluleDep_existe==false){
			// Si c'est la première fois qu'un taxi part d'une cellule on l'a créer
			RecRentable recRentable = new RecRentable();
			recRentable.setX(DepX);
			recRentable.setY(DepY);
			recRentable.getFares().add( (double) (recs30.getLast().getFare_amount()+recs30.getLast().getTip_amount()));
			recRentable.setMediane(recRentable.getFares().get(recRentable.getFares().size()/2));
			recRentable.setProfit(recRentable.getMediane()/recRentable.getTaxiVide());
			recsRentable.add(recRentable);
		}
		
		//modification due aux supppressions des taxi -30 min 
		// modification dues aux supppressions des taxi -30 min à 15min
	}

	public static void getCell(DebsRecord record) {

		/*
		 * Le grille fait 600 case x 600 cases. Chaque case fait 250m x 250m
		 * Coordonnées de la 1ère case : 20.7374685, -37.4567925 250 m to South
		 * = 0.002245778 (lattitude) 250 m to East = 0.002993 (longitude)
		 */
		
		
		double pickup_longitude = record.getPickup_longitude();
		double pickup_latitude = record.getPickup_latitude();
		double dropoff_longitude = record.getDropoff_longitude();
		double dropoff_latitude = record.getDropoff_latitude();
		
		double a = pickup_longitude + 37.4567925 + 0.005986 / 2;
		double xDepDouble = a / 0.002993;
		DepX = (int) xDepDouble + 1;

		a = -(pickup_latitude - 20.7374685 - 0.004491556 / 2);
		double yDepDouble = a / 0.002245778;
		DepY = (int) yDepDouble + 1;

		a = dropoff_longitude + 37.4567925 + 0.005986 / 2;
		double xArrDouble = a / 0.002993;
		ArrX = (int) xArrDouble + 1;

		a = -(dropoff_latitude - 20.7374685 - 0.004491556 / 2);
		double yArrDouble = a / 0.002245778;
		ArrY = (int) yArrDouble + 1;
	}

	public static int getProfit(LinkedList<DebsRecord> allRecs,int x, int y) {
		LinkedList<DebsRecord> recsLast15 = new LinkedList<>();
		while (lastTime - recsLast15.getFirst().getDropoff_datetime() / 60000 > 15) {
			recsLast15.removeFirst();
		}
		for (int i=0;i<recsLast15.size();i++){
			if(!(x==DepX && y==DepY)){
				recsLast15.remove(i);
				i--;
			}
		}
		return 0;
	}
}