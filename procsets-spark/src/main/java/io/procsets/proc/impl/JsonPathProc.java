package io.procsets.proc.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;

import io.procsets.proc.Procedure;
import io.procsets.proc.Procontext;
import io.procsets.proc.util.SchemaUtil;
import io.procsets.util.Converter;

/**
 * 
 * To extract values off of a 'json formatted string content' using 'jsonpath'
 *
 * <Pre>
 * mynewview = ijsonpath?&view=mysourceview&column=name-of-column&$.store.book.author=authorName:string&$.store.book.price=price:int
 * 
 * Reference:
 * 
 * https://github.com/json-path/JsonPath
 * 
 * </pre>
 * 
 * @author mjs
 *
 */
public class JsonPathProc extends Procedure {
	
	private static final long serialVersionUID = 4262207398833836899L;

	@Override
	public Procontext run(Procontext pc) {

		try {
			assertNotNullParams("saveas","view", "column");
			List<Object> rowvalues = null;
			StructType newschema = null;
			Set<Entry<String, String>> newfields = null;
			List<Row> resultrows = new ArrayList<Row>();
			Dataset<Row>  dataset = pc.ss().table(StringUtils.trim(param("view")));
			if (dataset != null) {
				for(Row row : dataset.collectAsList()) {
					rowvalues = new ArrayList<Object>();
					for (String f : row.schema().fieldNames()) {
						rowvalues.add(row.getAs(f));
					}
					if (newschema == null) {
						ArrayList<StructField> tfields = new ArrayList<StructField>();
						for(StructField f : row.schema().fields()) {
							tfields.add(f);
						}
						String[] nf = null;
						newfields = paramap("$", false, "").entrySet();
						for(Entry<String, String> entry : newfields) {
							nf = entry.getValue().split(":");
							tfields.add(DataTypes.createStructField(nf[0], SchemaUtil.getDatatypeByName(nf.length > 1 ? nf[1] : "string") , true)); 
						}
						newschema = DataTypes.createStructType(tfields.toArray(new StructField[tfields.size()]));
					}
					String recstr = row.getAs(param("column"));
					ReadContext jsonrec = JsonPath.parse(recstr);
					Object valo = null;
					for(Entry<String, String> entry : newfields) {
						try {
							valo = jsonrec.read(entry.getKey());
						} catch(Exception e) {
							if(isDebug()) {
								valo = e.toString();
							}
						}
						rowvalues.add(Converter.convert(entry.getValue(), valo).getValue());
					}
					resultrows.add(RowFactory.create(rowvalues.toArray()));
				}
				dataset = pc.ss().createDataFrame(pc.jsc().parallelize(resultrows), newschema);
			}
			createOrReplaceView(dataset);
			pc.pushProcStack(true, this, "DONE");
		} catch (Exception e) {
			LOG().error("proc-jsonpath-run exception", e);
			pc.pushProcStack(false, this, (isDebug() ? ExceptionUtils.getStackTrace(e) : e.getMessage()));
		}
		return pc;
	}
}