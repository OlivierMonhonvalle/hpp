package fr.tse.fi2.hpp.labs.queries.impl.lab4;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.css.Rect;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class RouteMembershipProcessor extends AbstractQueryProcessor {
	
	private static List<DebsRecord> recs =  new ArrayList<>();
	
	public RouteMembershipProcessor(QueryProcessorMeasure measure) {
		super(measure);
		// TODO Auto-generated constructor stub
		
	}

	@Override
	protected void process(DebsRecord record) {
		// TODO Auto-generated method stub
		recs.add(record);
	}
	
	public int checkroute(float d, float g, float e, float f, String license){
		int nb = 0;
		for (DebsRecord debsRecord : recs) {
			if (d == debsRecord.getPickup_longitude() && g == debsRecord.getPickup_latitude() && 
					e == debsRecord.getDropoff_longitude() && f == debsRecord.getDropoff_latitude() &&
				    debsRecord.getHack_license().equals(license))
			{
				nb++;
			}
		}
		return nb;
	}
	
	public static void afficher(){
		System.out.println("Départ : " + recs.get(100).getPickup_longitude()+ " " + recs.get(100).getPickup_latitude());
		System.out.println("Arrivé : " + recs.get(100).getDropoff_longitude()+ " " + recs.get(100).getDropoff_latitude());
		System.out.println("License : " + recs.get(100).getHack_license());
	}
}
