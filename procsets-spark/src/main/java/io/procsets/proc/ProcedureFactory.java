package io.procsets.proc;

import java.util.HashMap;
import java.util.Map;

import io.procsets.proc.impl.CassandraInputProc;
import io.procsets.proc.impl.CloseProcontext;
import io.procsets.proc.impl.DatasourceProc;
import io.procsets.proc.impl.DescribeView;
import io.procsets.proc.impl.ElasticInputProc;
import io.procsets.proc.impl.GrokProc;
import io.procsets.proc.impl.JdbcInputProc;
import io.procsets.proc.impl.JsonifyProc;
import io.procsets.proc.impl.JsonPathProc;
import io.procsets.proc.impl.MLClusterKMeans;
import io.procsets.proc.impl.MongoInputProc;
import io.procsets.proc.impl.Noname;
import io.procsets.proc.impl.ReturnView;
import io.procsets.proc.impl.SparkSQLProc;
import io.procsets.proc.impl.SolrInputProc;

public class ProcedureFactory {

	private static Map<String, Class<? extends Procedure>> proclassmap = new HashMap<String, Class<? extends Procedure>>();
	
	static {
		proclassmap.put("datasource", DatasourceProc.class);
		proclassmap.put("genview", SparkSQLProc.class);
		proclassmap.put("icassandra", CassandraInputProc.class);
		proclassmap.put("ssql", SparkSQLProc.class);
		proclassmap.put("imongo", MongoInputProc.class);
		proclassmap.put("mongo", MongoInputProc.class);
		proclassmap.put("ielastic", ElasticInputProc.class);
		proclassmap.put("isolr", SolrInputProc.class);
		proclassmap.put("ijdbc", JdbcInputProc.class);
		proclassmap.put("jsonify", JsonifyProc.class);
		proclassmap.put("jsonpath", JsonPathProc.class);
		proclassmap.put("grok", GrokProc.class);
		proclassmap.put("describe", DescribeView.class);
		proclassmap.put("mlkmeans", MLClusterKMeans.class);
		proclassmap.put("return", ReturnView.class);
		proclassmap.put("close", CloseProcontext.class);
	}

	public static Procedure procedure(String name, Map<String, String> args) {
		Class<? extends Procedure> procclass = proclassmap.get(name);
		Procedure proc = null;
		if (procclass != null) {
			try {
				proc = procclass.newInstance();
			} catch (Exception e) {
				proc = new Noname().setName(e.toString());
			}
		} else {
			proc =  new Noname().setName(name);
		}	
		return proc.setName(name).setParameters(args);
	}
}
