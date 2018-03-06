package io.progsets.proc.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import io.progsets.proc.Procedure;
import io.progsets.proc.Procontext;
import io.progsets.proc.util.SchemaUtil;

/**
 * 
 * Loads a text file into a view and optionally splits each line (for ex CSV) and constructs a data-grid as specified.
 * 
 * myviewname = ifile?datasource=myDSname&file.path=http://s3/myfile.csv&file.split=true|yes|false|now&file.delimiter=,|\t&file.hasheader=ture&file.columns=f1:int,f2:string,f2:boolean
 * 
 * OR 
 * 
 * myviewname = ifile?datasource=myDSname
 * 
 * @author mjs
 *
 */
public class FileInputProc extends Procedure {
	
	private static final long serialVersionUID = 4262207398833836899L;

	@Override
	public Procontext run(Procontext pc) {

		try {
			assertNotNullParams("saveas");
			final Map<String, String> viewparams = settings(pc);
			viewparams.putAll(paramap("file.", true, ""));
			Dataset<Row> resultset = null;
			//if file.split=true|false|yes|no
			if (isparam("file.split")) {
				JavaRDD<String> jrdd = pc.ss().read().textFile(viewparams.get("path")).javaRDD();
				StructType newschema = null;
				ArrayList<StructField> tfields = new ArrayList<StructField>();
				String[] fileFields = null;
				List<Object> rowvalues = null;
				List<Row> resultrows = new ArrayList<Row>();
				//build new schema if file.columns provided
				String[] nfd = null;
				if (isparam("file.columns")) {
					fileFields = param("file.columns").split(",");
					for(String entry : fileFields) {
						nfd = entry.split(":");
						tfields.add(DataTypes.createStructField(nfd[0], SchemaUtil.getDatatypeByName(nfd.length > 1 ? nfd[1] : "string") , true)); 
					}
					newschema = DataTypes.createStructType(tfields.toArray(new StructField[tfields.size()]));
				}
				//iterate data row, skip if first row is file.hasheader=true
				boolean shouldskipfirst = false;
				Boolean shouldsplitrec = isparam("file.split", "true", "yes");
				
				for (String srec : jrdd.collect()) {
					if (!shouldskipfirst) {
						shouldskipfirst = true;
						if (isparam("file.hasheader", "true", "yes")) {
							//if first row is header and newschema is not built because file.columns == null
							//build default columns
							if (newschema == null) {
								for(String entry : srec.split(",")) {
									tfields.add(DataTypes.createStructField(entry, SchemaUtil.getDatatypeByName("string") , true)); 
								}
								newschema = DataTypes.createStructType(tfields.toArray(new StructField[tfields.size()]));
							}
							continue;
						}
					}
					if (shouldsplitrec) {
						rowvalues = Arrays.asList(trim(srec.split(param("file.delimiter", ","))));
					} else {
						rowvalues = Arrays.asList(srec);
					}
					resultrows.add(RowFactory.create(rowvalues.toArray()));
				}
				resultset = pc.ss().createDataFrame(pc.jsc().parallelize(resultrows), newschema);
			} else {
				resultset = pc.ss().read().format(viewparams.get("spark.read.format"))
									  .options(viewparams)
									  .load(viewparams.get("path"));
			}
			createOrReplaceView(resultset);
			pc.pushProcStack(true, this, "DONE");
		} catch (Exception e) {
			LOG().error("proc-fileinput-run exception", e);
			pc.pushProcStack(false, this, (isDebug() ? ExceptionUtils.getStackTrace(e) : e.getMessage()));
		}
		return pc;
	}
	
	private Object[] trim(String[] values) {
		Object[] result = new Object[values.length];
		for(int i = 0; i < values.length; i++) {
			result[i] = trim(values[i]);
		}
		return result;
	}

	private Object trim(String val) {
		val = val.trim();
		if (val.startsWith("\"")) {
			val = val.substring(1);
		}
		if (val.endsWith("\"")) {
			val = val.substring(0, val.indexOf("\""));
		}
		return val;
	}

	/**
	 * Returns default settings for the procedures + resolved datasource settings
	 */
	@Override
	public Map<String, String> settings(Procontext pc) {
		Map<String, String> settings = (HashMap<String, String>) super.settings(pc);
		String ds = param("datasource");
		if (ds == null) {
			ds = pc.appproperties().prop("progsets.proc.ifile.default.datasource", "default");
		}
		//load default settings for the connector
		settings.putAll(pc.appproperties().propsWithPrefix("progsets.proc.ifile.default.", true));
		//load default settings for the data source
		settings.putAll(pc.appproperties().propsWithPrefix("progsets.spark.datasource." + ds + ".", true));
		return settings;
	}

}
