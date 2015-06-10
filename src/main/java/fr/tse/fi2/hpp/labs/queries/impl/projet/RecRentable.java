package fr.tse.fi2.hpp.labs.queries.impl.projet;

import java.util.ArrayList;
import java.util.LinkedList;

public class RecRentable implements Comparable<RecRentable> {
	int x = 0;
	int y = 0;
	int taxiVide = 0;
	LinkedList<Double> fares = new LinkedList<>();
	LinkedList<String> iDs = new LinkedList<>();
	Double mediane = (double) 0;
	Double profit = 0.0;

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getTaxiVide() {
		return taxiVide;
	}

	public void setTaxiVide(int taxiVide) {
		this.taxiVide = taxiVide;
	}

	public Double getMediane() {
		return mediane;
	}

	public void setMediane(Double mediane) {
		this.mediane = mediane;
	}

	public Double getProfit() {
		return profit;
	}

	public void setProfit(Double profit) {
		this.profit = profit;
	}

	public LinkedList<Double> getFares() {
		return fares;
	}

	public void setFares(LinkedList<Double> fares) {
		this.fares = fares;
	}

	public LinkedList<String> getiDs() {
		return iDs;
	}

	public void setiDs(LinkedList<String> iDs) {
		this.iDs = iDs;
	}

	public String toString() {
		return "x=" + x + ", y=" + y + ", taxiVide=" + taxiVide + ", mediane=" + mediane + ", profit=" + profit;
	}

	@Override
	public int compareTo(RecRentable a) {
		final int BEFORE = -1;
	    final int EQUAL = 0;
	    final int AFTER = 1;
	    
	    if (this == a) return EQUAL;
	    if (this.getProfit() < a.getProfit()) return AFTER;
	    if (this.getProfit() > a.getProfit()) return BEFORE;
		return 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fares == null) ? 0 : fares.hashCode());
		result = prime * result + ((iDs == null) ? 0 : iDs.hashCode());
		result = prime * result + ((mediane == null) ? 0 : mediane.hashCode());
		result = prime * result + ((profit == null) ? 0 : profit.hashCode());
		result = prime * result + taxiVide;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RecRentable other = (RecRentable) obj;
		if (fares == null) {
			if (other.fares != null)
				return false;
		} else if (!fares.equals(other.fares))
			return false;
		if (iDs == null) {
			if (other.iDs != null)
				return false;
		} else if (!iDs.equals(other.iDs))
			return false;
		if (mediane == null) {
			if (other.mediane != null)
				return false;
		} else if (!mediane.equals(other.mediane))
			return false;
		if (profit == null) {
			if (other.profit != null)
				return false;
		} else if (!profit.equals(other.profit))
			return false;
		if (taxiVide != other.taxiVide)
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
}