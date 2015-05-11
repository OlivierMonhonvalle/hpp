package fr.tse.fi2.hpp.labs.queries.impl.lab2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class WriteQuery implements Runnable  {

	private static BlockingQueue<Float> listsum;
	final static Logger logger = LoggerFactory
			.getLogger(AbstractQueryProcessor.class);
	private BufferedWriter outputWriter;
	int id;
	static float poison;

	public WriteQuery(BlockingQueue<Float> listsum,  int id) {
		// TODO Auto-generated constructor stub
		this.listsum = listsum;
		this.id = id;
		try {
			outputWriter = new BufferedWriter(new FileWriter(new File(
					"result/query" + id + ".txt")));
		} catch (IOException e) {
			logger.error("Cannot open output file for " + id, e);
			System.exit(-1);
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		//smeasure.notifyStart(this.id);
		poison(0);
		try {
			while (this.listsum.take() != -1.0f) {
				try {
					writeLine("Somme " + this.listsum.take());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finish();
	}
	
	public void writeLine(String line) {
		try {
			outputWriter.write(line);
			outputWriter.newLine();
		} catch (IOException e) {
			logger.error("Could not write new line for query processor " + id
					+ ", line content " + line, e);
		}

	}
	
	public void finish() {
		// Close writer
		try {
			outputWriter.flush();
			outputWriter.close();
		} catch (IOException e) {
			logger.error("Cannot property close the output file for query "
					+ id, e);
		}
	}
	
	public static void poison(float p) {
	    poison = p;
	    listsum.add(poison);
	}

}

