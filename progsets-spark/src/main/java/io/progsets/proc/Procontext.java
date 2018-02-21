package io.progsets.proc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudera.livy.JobContext;

import io.progsets.common.Appproperties;
import io.progsets.common.Tripple;

public class Procontext extends HashMap<String, Object> {

	private static final long serialVersionUID = 3794209450429776732L;
	
	Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	private boolean valid;
	private SparkConf sc = null;
	private SparkSession ss = null;
	private JavaSparkContext jsc = null;
	private Object result = null;
	private List<Map<String, Object>> resultlist = new ArrayList<Map<String, Object>>();
	private Stack<Tripple<Boolean, Procedure, String>> procStack = new Stack<Tripple<Boolean, Procedure, String>>();
	private Appproperties appproperties = null;
	
	public Procontext(String appName, String master, Appproperties appproperties) {
		this.sc = new SparkConf().setAppName(appName).setMaster(master);
		
		for(Entry<String, String> entry : appproperties.propsWithPrefix("progsets.spark.", true, "spark.").entrySet()) {
			this.sc.set(entry.getKey(), entry.getValue());
		}
		
		this.ss = SparkSession
				  .builder()
				  .appName(appName)
				  .master(master)
				  .config(sc)
				  .getOrCreate();

		this.jsc = new JavaSparkContext(ss.sparkContext());
		this.appproperties = appproperties;
		valid = true;
	}
	
	public Procontext(String appName, String master, JobContext jc, Appproperties approperties) {
		this.jsc = jc.sc();
		this.sc = jsc.getConf();
		this.ss = SparkSession
				  .builder()
				  .appName(appName)
				  .master(master)
				  .config(sc)
				  .getOrCreate();
		valid = true;
		this.appproperties = appproperties;
	}
	
	public SparkSession ss() {
		return ss;
	}
	
	public SparkConf sc() {
		return sc;
	}
	
	public JavaSparkContext jsc() {
		return jsc;
	}
	
	@Deprecated
	public Object result() {
		return result;
	}

	@Deprecated
	public void result(Object result) {
		this.result = result;
	}
	
	public Stack<Tripple<Boolean, Procedure, String>> procStack() {
		return procStack;
	}
	
	public Appproperties appproperties() {
		return this.appproperties;
	}
	
	@Override
	public void finalize() {
		this.close();
		this.clear();
	}
	
	public void close() {
		try {
			valid = false;
			ss.close();
			jsc.close();
		} catch (Exception e) {
			LOG.warn("error destroying procontext", e);
		}
	}
	
	public void clear() {
		try {
			procStack.clear();
		} catch(Exception e) {
			LOG.warn("error clearning procontext", e);
		}
	}

	public void pushProcStack(Boolean success, Procedure proc, String message) {
		procStack.push(new Tripple<Boolean, Procedure, String>(success, proc, message));
	}
	
	public boolean wasLastProcSuccess() {
		return procStack.peek().a();
	}

	public Tripple<Boolean, Procedure, String> lastProcStatus() {
		return procStack.peek();
	}

	public boolean isValid() {
		return this.valid;
	}

	public List<Map<String, Object>> getResultlist() {
		return resultlist;
	}

	public void setResultlist(List<Map<String, Object>> resultlist) {
		this.resultlist = resultlist;
	}
	
	public void addResult(Map<String, Object> result) {
		this.resultlist.add(result);
	}
}