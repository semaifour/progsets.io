package io.progsets.proc.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
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
import io.progsets.util.FSGrok;
import oi.thekraken.grok.api.Grok;
import oi.thekraken.grok.api.Match;

/**
 * 
 * To extract values using GROK pattern
 *
 * <Pre>
 * mynewview = grok?pattern={...}&view=mysourceview&column=name-of-column
 * 
 * </pre>
 * @author mjs
 *
 */
public class GrokProc extends Procedure {
	
	private static final long serialVersionUID = 4262207398833836899L;

	@Override
	public Procontext run(Procontext pc) {

		try {
			assertNotNullParams("saveas", "pattern", "view", "column");
			List<Object> rowvalues = null;
			StructType newschema = null;
			List<String> newfields = new ArrayList<String>();
			List<Row> resultrows = new ArrayList<Row>();
			Dataset<Row>  dataset = pc.ss().table(StringUtils.trim(param("view")));
			String pattern = param("pattern");
			Grok grok = FSGrok.newGrok(pattern);
			if (dataset != null) {
				for(Row row : dataset.collectAsList()) {
					rowvalues = new ArrayList<Object>();
					for (String f : row.schema().fieldNames()) {
						rowvalues.add(row.getAs(f));
					}
					String recstr = row.getAs(param("column"));
					Match grokmatch = grok.match(recstr);
					grokmatch.captures();
					Map<String, Object> grokrecord = grokmatch.toMap();
					if (newschema == null) {
						ArrayList<StructField> tfields = new ArrayList<StructField>();
						for(StructField f : row.schema().fields()) {
							tfields.add(f);
						}
						for(Entry<String, Object> entry : grokrecord.entrySet()) {
							tfields.add(DataTypes.createStructField(entry.getKey(), SchemaUtil.getDatatypeByValue(entry.getValue()), true)); 
							newfields.add(entry.getKey());
						}
						newschema = DataTypes.createStructType(tfields.toArray(new StructField[tfields.size()]));
					}
					for(String newfield : newfields) {
						rowvalues.add(grokrecord.get(newfield));
					}
					resultrows.add(RowFactory.create(rowvalues.toArray()));
				}
				dataset = pc.ss().createDataFrame(pc.jsc().parallelize(resultrows), newschema);
			}
			createOrReplaceView(dataset);
			pc.pushProcStack(true, this, "DONE");
		} catch (Exception e) {
			LOG().error("proc-grok-run exception", e);
			pc.pushProcStack(false, this, (isDebug() ? ExceptionUtils.getStackTrace(e) : e.getMessage()));
		}
		return pc;
	}
}