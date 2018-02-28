package io.progsets.proc.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import io.progsets.proc.Procedure;
import io.progsets.proc.Procontext;
import io.progsets.proc.util.SchemaUtil;
import io.progsets.util.Converter;

/**
 * 
 * myviewname = convert?view=myviewname&columns=f1:int,f2:string,f2:boolean
 * 
 * @author mjs
 *
 */
public class ConvertProc extends Procedure {
	
	private static final long serialVersionUID = 4262207398833836899L;

	@Override
	public Procontext run(Procontext pc) {

		try {
			assertNotNullParams("saveas");
			StructType newschema = null;
			ArrayList<StructField> tfields = new ArrayList<StructField>();
			List<Object> rowvalues = null;
			List<Row> resultrows = new ArrayList<Row>();
			String[] nfd = null;
			Dataset<Row> resultset = pc.ss().table(param("view"));
			String[] fields = param("columns").split(",");
			if (isparam("columns")) {
				for(String entry : fields) {
					nfd = entry.split(":");
					tfields.add(DataTypes.createStructField(nfd[0], SchemaUtil.getDatatypeByName(nfd.length > 1 ? nfd[1] : "string") , true)); 
				}
				newschema = DataTypes.createStructType(tfields.toArray(new StructField[tfields.size()]));
			}
			for (Row row : resultset.collectAsList()) {
				rowvalues = new ArrayList<Object>();
				for(String entry : fields) {
					rowvalues.add(Converter.convert(entry, row.getAs(entry.split(":")[0])).getValue());
				}
				resultrows.add(RowFactory.create(rowvalues.toArray()));
			}
			resultset = pc.ss().createDataFrame(pc.jsc().parallelize(resultrows), newschema);
			createOrReplaceView(resultset);
			pc.pushProcStack(true, this, "DONE");
		} catch (Exception e) {
			LOG().error("proc-convert-run exception", e);
			pc.pushProcStack(false, this, (isDebug() ? ExceptionUtils.getStackTrace(e) : e.getMessage()));
		}
		return pc;
	}
	
	/**
	 * Returns default settings for the procedures + resolved datasource settings
	 */
	@Override
	public Map<String, String> settings(Procontext pc) {
		Map<String, String> settings = (HashMap<String, String>) super.settings(pc);
		String ds = param("datasource");
		if (ds == null) {
			ds = pc.appproperties().prop("progsets.proc.convert.default.datasource", "default");
		}
		//load default settings for the connector
		settings.putAll(pc.appproperties().propsWithPrefix("progsets.proc.convert.default.", true));
		//load default settings for the data source
		settings.putAll(pc.appproperties().propsWithPrefix("progsets.spark.datasource." + ds + ".", true));
		return settings;
	}

}
