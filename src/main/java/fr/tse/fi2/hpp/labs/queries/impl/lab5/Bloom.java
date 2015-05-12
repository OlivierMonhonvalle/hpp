package fr.tse.fi2.hpp.labs.queries.impl.lab5;

import java.math.BigInteger;
import java.util.BitSet;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;


public class Bloom  {

	//public static BitSet listeHashRoute;
	static BigInteger modulo;
	private static String res = null;

	public Bloom(int nb_octet) {
		String nb_octer_s = ""+ nb_octet;
		modulo = new BigInteger(nb_octer_s);
		
	}

	public static String getStringConca (DebsRecord record){
		res = record.getPickup_longitude() + record.getPickup_latitude() + record.getDropoff_longitude()
				  + record.getDropoff_latitude() + record.getHack_license();
		return res;
	}
	
	public int getIndex(int i, String result){
		String resultHash = result + i;
		BigInteger bigIndex = new BigInteger(SHA3Util.digest(resultHash),16);
		int index = bigIndex.mod(modulo).intValue();
		return index;
	}
	
	



}