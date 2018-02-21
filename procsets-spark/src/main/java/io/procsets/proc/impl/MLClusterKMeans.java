package io.procsets.proc.impl;

import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.spark.ml.clustering.KMeans;
import org.apache.spark.ml.clustering.KMeansModel;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import io.procsets.proc.Procontext;

/**
 * 
 * mlkmeans?view=myview&saveas=myresult&
 * 
 * OR 
 * 
 * myresutl = mlkmeans?view=myview&
 * 
 * 
 * @author mjs
 *
 */
public class MLClusterKMeans extends MachineLeaningProc {
	
	private static final long serialVersionUID = 4262207398833836899L;

	@Override
	public Procontext run(Procontext pc) {

		try {
			assertNotNullParams("saveas", "view");
			Map<String, String> viewparams = settings(pc);
			
			Dataset<Row> dataset = pc.ss().table(param("view"));

			// Trains a k-means model.
			KMeans kmeans = new KMeans().setK(param("k", 2)).setSeed(param("seed", 1L));
			KMeansModel model = kmeans.fit(dataset);
			// Evaluate clustering by computing Within Set Sum of Squared Errors.
			double WSSSE = model.computeCost(dataset);
			System.out.println("Within Set Sum of Squared Errors = " + WSSSE);

			// Shows the result.
			Vector[] centers = model.clusterCenters();
			System.out.println("Cluster Centers: ");
			for (Vector center: centers) {
			  System.out.println(center);
			}
			
			
			pc.pushProcStack(true, this, "DONE");
		} catch (Exception e) {
			LOG().error("proc-machinel-run exception", e);
			pc.pushProcStack(false, this, (isDebug() ? ExceptionUtils.getStackTrace(e) : e.getMessage()));
		}
		return pc;
	}
}
