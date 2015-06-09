package fr.tse.fi2.hpp.labs.queries.impl.projet;

public class CreationTabCell {
	
	private int cellXd;
	private int cellYd;
	private int cellXf;
	private int cellYf;
	private String licence;
	private float prix;
	private float pourB;
	
	public CreationTabCell(String hack_license,
			long pickup_datetime, long dropoff_datetime,
			float pickup_longitude, float pickup_latitude,
			float dropoff_longitude, float dropoff_latitude, 
			float fare_amount, float tip_amount) {
		
		double a = pickup_longitude + 37.4567925 + 0.005986 / 2;
		double xDepDouble = a /0.002993;
		cellXd = (int) xDepDouble + 1;

		a = -(pickup_latitude - 20.7374685 - 0.004491556 / 2);
		double yDepDouble = a /0.002245778;
		cellYd = (int) yDepDouble + 1;

		a = dropoff_longitude + 37.4567925 + 0.005986 / 2;
		double xArrDouble = a / 0.002993;
		cellXf = (int) xArrDouble + 1;

		a = -(dropoff_latitude -20.7374685 - 0.004491556 / 2);
		double yArrDouble = a / 0.002245778;
		cellYf = (int) yArrDouble + 1;
		
		licence = hack_license;
		prix = fare_amount;
		pourB = tip_amount;
	}

	public int getCellXd() {
		return cellXd;
	}

	public int getCellYd() {
		return cellYd;
	}

	public int getCellXf() {
		return cellXf;
	}

	public int getCellYf() {
		return cellYf;
	}

	public String getLicence() {
		return licence;
	}

	public float getPrix() {
		return prix;
	}

	public float getPourB() {
		return pourB;
	}
}
