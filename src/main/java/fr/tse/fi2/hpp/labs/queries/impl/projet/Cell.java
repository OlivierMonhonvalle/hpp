package fr.tse.fi2.hpp.labs.queries.impl.projet;

public class Cell {
	int depX = 0;
	int depY = 0;
	int arrX = 0;
	int arrY = 0;
	int occurence = 0;

	public int getDepX() {
		return depX;
	}

	public void setDepX(int depX) {
		this.depX = depX;
	}

	public int getDepY() {
		return depY;
	}

	public void setDepY(int depY) {
		this.depY = depY;
	}

	public int getArrX() {
		return arrX;
	}

	public void setArrX(int arrX) {
		this.arrX = arrX;
	}

	public int getArrY() {
		return arrY;
	}

	public void setArrY(int arrY) {
		this.arrY = arrY;
	}

	public int getOccurence() {
		return occurence;
	}

	public void setOccurence(int occurence) {
		this.occurence = occurence;
	}

	public boolean equals(Cell cell) {
		if (this.depX == cell.getDepX() && this.depY == cell.getDepY() && this.arrX == cell.getArrX() && this.arrY == cell.getarrY()) {
			return true;
		}
		else{
			return true;
		}
	}
}
