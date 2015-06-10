package fr.tse.fi2.hpp.labs.queries.impl.projet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.dispatcher.LoadFirstDispatcher;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;
import fr.tse.fi2.hpp.labs.queries.impl.projet.Query1;

/**
 * Main class of the program. Register your new queries here
 * 
 * Design choice: no thread pool to show the students explicit
 * {@link CountDownLatch} based synchronization.
 * 
 * @author Julien
 * 
 */

@State(Scope.Thread)
public class ProjetJHM {

	final static Logger logger = LoggerFactory
			.getLogger(ProjetJHM.class);
    //private static BloomFilterHash q;
	private static Query2 q;
    private DebsRecord r;
	/**
	 * @param args
	 * @throws IOException
	 */
	
	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.SECONDS)
	@Fork(1)
	@Warmup(iterations=1)
	@Measurement(iterations=1)
	public static void main() throws IOException {
		// Init query time measure
		QueryProcessorMeasure measure = new QueryProcessorMeasure();
		// Init dispatcher and load everything
		LoadFirstDispatcher dispatch = new LoadFirstDispatcher(
				"src/main/resources/data/100k.csv");
		logger.info("Finished parsing");
		// Query processors
		List<AbstractQueryProcessor> processors = new ArrayList<>();
		// Add you query processor here
		q = new Query2(measure);
		processors.add(q);
		//processors.add(new SumQuery(measure));
		// Register query processors
		for (AbstractQueryProcessor queryProcessor : processors) {
			dispatch.registerQueryProcessor(queryProcessor);
		}
		// Initialize the latch with the number of query processors
		CountDownLatch latch = new CountDownLatch(processors.size());
		// Set the latch for every processor
		for (AbstractQueryProcessor queryProcessor : processors) {
			queryProcessor.setLatch(latch);
		}
		for (AbstractQueryProcessor queryProcessor : processors) {
			Thread t = new Thread(queryProcessor);
			t.setName("QP" + queryProcessor.getId());
			t.start();
		}
		// Start everything dispatcher first, not as a thread
		dispatch.run();
		logger.info("Finished Dispatching");
		// Wait for the latch
		try {
			latch.await();
		} catch (InterruptedException e) {
			logger.error("Error while waiting for the program to end", e);
		}
	}
	


}