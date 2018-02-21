package io.progsets.proc.impl;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import io.progsets.proc.Procedure;
import io.progsets.proc.Procontext;

/**
 * 
 * Returns one or more views as list of maps or rows & columns
 * 
 * return?view=myviewname1,myviewname2&as=map|row|tree/treemap|treelist
 * 
 */
public class ReturnView extends Procedure {

	private static final long serialVersionUID = 5462017043671728400L;
	public static String NEW_LINE = System.getProperty("line.separator");
	
	public Procontext run(Procontext pc) {
		assertNotNullParams("view");

		//1. route based on 'return.as' overridden parameter first if overridden
		//2. route based on 'as' parameter if not overridden
		
		//route based on override parameter 'return.as'
		if (isparam("return.as", "map", "maps")) {
			return returnmap(pc);
		} if (isparam("return.as", "vert", "vertical", "verticals", "column", "columns")) { 
			return returnvert(pc);
		} else if (isparam("return.as", "tree", "treemap", "treelist")) {
			return returntree(pc);
		} else if (isparam("return.as", "row", "rows")) {
			return returnrow(pc);
		} else if (isparam("return.as", "rowcol")) {
			return returnrowcol(pc);
		} else if (isparam("return.as", "sheet")) {
				return returnsheet(pc);
			}
		//route base on function parameter 'as'
		else if (isparam("as", "map", "maps")) { 
			return returnmap(pc);
		} else if (isparam("as", "vert", "vertical", "verticals","column", "columns")) { 
			return returnvert(pc);
		} else if (isparam("as", "tree", "treemap", "treelist")) { 
			return returntree(pc);
		} else if (isparam("as", "row", "rows" )) { 
			return returnrow(pc);
		} else if(isparam("as", "rowcol")) {
			return returnrowcol(pc);
		} else if (isparam("as", "sheet")) {
			return returnsheet(pc);
		} else { //else default
			return returnrow(pc);
		}
	}
		
		
	/**
	 * 
	 * Return views as list of maps
	 * 
	 * @param pc
	 * @return
	 */
	public Procontext returnmap(Procontext pc) {
		try {
			Map<String, Object> resultset = null;
			Dataset<Row> dataset = null;
			List<Map<String, Object>> records = null;
			Map<String, Object> record = null;
		
			for(String v : param("view").split(",")) {
				try {
					resultset = new HashMap<String, Object>();
					dataset = pc.ss().table(v.trim());
					records = new ArrayList<Map<String, Object>>();
					for(Row row : dataset.collectAsList()) {
						record = new HashMap<String, Object>();
						for (String f : row.schema().fieldNames()) {
							record.put(f, row.getAs(f));
						}
						records.add(record);
					}
					resultset.put("name", v);
					resultset.put("type", "map");
					resultset.put("status", "success");
					resultset.put("columns", dataset.columns());
					resultset.put("data", records);
				} catch(Exception e) {
					resultset.put(v + "-status", "failure");
					if (isparam("ignore")) {
						resultset.put(v + "-message", e.getMessage());
					} else {
						pc.pushProcStack(false, this, (isDebug() ? ExceptionUtils.getStackTrace(e) : e.getMessage()));
					}
					LOG().info("proc-returnviewmap-view-iter-exception", e);
				}
				pc.addResult(resultset);
			}
			//pc.result(result);
			pc.pushProcStack(true, this, "DONE");
		} catch (Exception e) {
			LOG().error("proc-returnviewmap-run-exception", e);
			pc.pushProcStack(false, this, (isDebug() ? ExceptionUtils.getStackTrace(e) : e.getMessage()));
		}
		return pc;	
	}
	
	/**
	 * 
	 * Returns views as rows and columns
	 * 
	 * @param pc
	 * @return
	 */
	public Procontext returnrow(Procontext pc) {
		try {
			List<Object> record = null;
			List<List<Object>> records = null;
			Dataset<Row> dataset = null;
			Map<String, Object> resultset = null;
			List<String> columns = null;
			
			for(String v : param("view").split(",")) {
				try {
					resultset = new HashMap<String, Object>();
					dataset = pc.ss().table(v.trim());
					records = new ArrayList<List<Object>>();
					columns = Arrays.asList(dataset.columns());
					for(Row row : dataset.collectAsList()) {
						record = new ArrayList<Object>();
						for(String c : columns) {
							record.add(row.getAs(c));
						}
						records.add(record);
					}
					resultset.put("name", v);
					resultset.put("type", "row");
					resultset.put("status", "success");
					resultset.put("columns", columns);
					resultset.put("data", records);
				} catch(Exception e) {
					resultset.put(v + "-status", "failure");
					if (isparam("ignore")) {
						resultset.put(v + "-message", e.getMessage());
					} else {
						pc.pushProcStack(false, this, (isDebug() ? ExceptionUtils.getStackTrace(e) : e.getMessage()));
					}
					LOG().info("proc-returnviewrow-view-iter-exception", e);
				}
				pc.addResult(resultset);
			}
			//pc.result(result);
			pc.pushProcStack(true, this, "DONE");
		} catch (Exception e) {
			LOG().error("proc-returnviewrow-run-exception", e);
			pc.pushProcStack(false, this, (isDebug() ? ExceptionUtils.getStackTrace(e) : e.getMessage()));
		}
		return pc;	
	}
	
	/**
	 * Returns one or more views's verticals; i.e values of a column as a row including the column name at index 0.
	 * 
	 * <pre>
	 * ['col1', $row1col1, $row1col1, $row3col1, ...],
	 * ['col2', $row1col2, $row1col2, $row3col2, ...]
	 * ['col...',...................................]
	 * <pre>
	 * 
	 */
	public Procontext returnvert(Procontext pc) {
		try {
			assertNotNullParams("view");
			Map<String, Object> resultset = null;
			Map<String, List<Object>> verticals = null;
			Dataset<Row> dataset = null;
			List<Object> vertical = null;
			List<String> columns = null;
			List<List<Object>> overticals = null;
			
			for(String v : param("view").split(",")) {
				try {
					resultset = new HashMap<String, Object>();
					dataset = pc.ss().table(v.trim());
					verticals = new HashMap<String, List<Object>>();
					overticals = new ArrayList<List<Object>>();
					columns = Arrays.asList(dataset.columns());
					for(Row row : dataset.collectAsList()) {
						for(String c : dataset.columns()) {
							vertical = verticals.get(c);
							if (vertical == null) {
								vertical = new ArrayList<Object>();
								vertical.add(c);
								verticals.put(c, vertical);
								overticals.add(vertical);
							}
							vertical.add(row.getAs(c));
						}
					}
					resultset.put("name", v);
					resultset.put("type", "vertical");
					resultset.put("status", "success");
					resultset.put("columns", columns);
					resultset.put("data", overticals);
				} catch(Exception e) {
					resultset.put(v + "-status", "failure");
					if (isparam("ignore")) {
						resultset.put(v + "-message", e.getMessage());
					} else {
						pc.pushProcStack(false, this, (isDebug() ? ExceptionUtils.getStackTrace(e) : e.getMessage()));
					}
					LOG().info("proc-returnviewvert-view-iter-exception", e);
				}
				pc.addResult(resultset);
			}
			//pc.result(resultset);
			pc.pushProcStack(true, this, "DONE");
		} catch (Exception e) {
			LOG().error("proc-returnviewvert-run-exception", e);
			pc.pushProcStack(false, this, (isDebug() ? ExceptionUtils.getStackTrace(e) : e.getMessage()));
		}
		return pc;	
	}
	
	/**
	 * 
	 * Return views as tree of maps (values are maps)
	 * 
	 * @param pc
	 * @return
	 */
	public Procontext returntree(Procontext pc) {
		try {
			this.assertNotNullParams("by");
			Map<String, Object> resultset = null;
			Dataset<Row> dataset = null;
			Map<String, Object> record = null;
			String[] treeby = param("by").split(",");
			Tree tree = null;
			String resulttype = null;
			Constructor<?> treeclass = null;
			if (isparam("return.as", "treelist") || isparam("as", "treelist")) {
				treeclass = TreeList.class.getConstructor(String[].class);
				resulttype = "treelist";
			} else {
				resulttype = "treemap";
				treeclass = Tree.class.getConstructor(String[].class);
			}
			
			for(String v : param("view").split(",")) {
				try {
					resultset = new HashMap<String, Object>();
					dataset = pc.ss().table(v.trim());
					tree = (Tree) treeclass.newInstance(new Object[] {treeby});
					for(Row row : dataset.collectAsList()) {
						record = new HashMap<String, Object>();
						for (String f : row.schema().fieldNames()) {
							record.put(f, row.getAs(f));
						}
						tree.add(record);
					}
					resultset.put("name", v);
					resultset.put("type", resulttype);
					resultset.put("status", "success");
					resultset.put("columns", dataset.columns());
					resultset.put("data", tree.root());
					resultset.put("treeby", param("by"));

				} catch(Exception e) {
					resultset.put(v + "-status", "failure");
					if (isparam("ignore")) {
						resultset.put(v + "-message", e.getMessage());
					} else {
						pc.pushProcStack(false, this, (isDebug() ? ExceptionUtils.getStackTrace(e) : e.getMessage()));
					}
					LOG().info("proc-returnviewtree-view-iter-exception", e);
				}
				pc.addResult(resultset);
			}
			//pc.result(resultset);
			pc.pushProcStack(true, this, "DONE");
		} catch (Exception e) {
			LOG().error("proc-returnviewtree-run-exception", e);
			pc.pushProcStack(false, this, (isDebug() ? ExceptionUtils.getStackTrace(e) : e.getMessage()));
		}
		return pc;	
	}

	/**
	 * 
	 * Returns views as rows as columns (Rotates the given grid 90 degree clockwise and considers first column as header and rest all data 
	 * 
	 * @param pc
	 * @return
	 */
	public Procontext returnrowcol(Procontext pc) {
		try {
			List<Object> record = null;
			List<List<Object>> records = null;
			Dataset<Row> dataset = null;
			Map<String, Object> resultset = null;
			List<String> columns = null;
			
			for(String v : param("view").split(",")) {
				try {
					resultset = new HashMap<String, Object>();
					dataset = pc.ss().table(v.trim());
					records = new ArrayList<List<Object>>();
					columns = Arrays.asList(dataset.columns());
					ArrayList<String> rowcolumns = new ArrayList<String>();
					for(Row row : dataset.collectAsList()) {
						record = new ArrayList<Object>();
						rowcolumns.add(row.getString(0));
						for(String c : columns) {
							record.add(row.getAs(c));
						}
						records.add(record);
					}
					resultset.put("name", v);
					resultset.put("type", "rowcol");
					resultset.put("status", "success");
					resultset.put("srccolumns", columns);
					resultset.put("columns", rowcolumns);
					resultset.put("data", records);
				} catch(Exception e) {
					resultset.put(v + "-status", "failure");
					if (isparam("ignore")) {
						resultset.put(v + "-message", e.getMessage());
					} else {
						pc.pushProcStack(false, this, (isDebug() ? ExceptionUtils.getStackTrace(e) : e.getMessage()));
					}
					LOG().info("proc-returnview-rowcol-view-iter-exception", e);
				}
				pc.addResult(resultset);
			}
			//pc.result(result);
			pc.pushProcStack(true, this, "DONE");
		} catch (Exception e) {
			LOG().error("proc-returnview-rowcol-run-exception", e);
			pc.pushProcStack(false, this, (isDebug() ? ExceptionUtils.getStackTrace(e) : e.getMessage()));
		}
		return pc;	
	}
	
	/**
	 * 
	 * Returns as csv or tsv or psv based on the delimiter parameter 
	 *  
	 * @param pc
	 * @return
	 */
	public Procontext returnsheet(Procontext pc) {
		try {
			StringBuilder record = null;
			StringBuilder records = null;
			Dataset<Row> dataset = null;
			Map<String, Object> resultset = null;
			List<String> columns = null;
			String delimiter = param("delimiter", "\t");
			for(String v : param("view").split(",")) {
				try {
					resultset = new HashMap<String, Object>();
					dataset = pc.ss().table(v.trim());
					records = new StringBuilder();
					columns = Arrays.asList(dataset.columns());
					int rowsize = columns.size();
					int cindex = 0;
					for (String col : columns) {
						records.append(col);
						cindex++;
						if (cindex < rowsize) {
							records.append(delimiter);
						} else {
							records.append(NEW_LINE);
						}
					}
					for(Row row : dataset.collectAsList()) {
						record = new StringBuilder();
						int tlimit = rowsize - 1; 
						for(cindex = 0; cindex < rowsize; cindex++) {
							record.append(row.get(cindex));
							if (cindex < tlimit) {
								record.append(delimiter);
							} else {
								record.append(NEW_LINE);
							}
						}
						records.append(record.toString());
					}
					resultset.put("name", v);
					resultset.put("type", "rowcol");
					resultset.put("status", "success");
					resultset.put("columns", columns);
					resultset.put("data", records.toString());
				} catch(Exception e) {
					resultset.put(v + "-status", "failure");
					if (isparam("ignore")) {
						resultset.put(v + "-message", e.getMessage());
					} else {
						pc.pushProcStack(false, this, (isDebug() ? ExceptionUtils.getStackTrace(e) : e.getMessage()));
					}
					LOG().info("proc-returnview-rowcol-view-iter-exception", e);
				}
				pc.addResult(resultset);
			}
			//pc.result(result);
			pc.pushProcStack(true, this, "DONE");
		} catch (Exception e) {
			LOG().error("proc-returnview-rowcol-run-exception", e);
			pc.pushProcStack(false, this, (isDebug() ? ExceptionUtils.getStackTrace(e) : e.getMessage()));
		}
		return pc;	
	}
}

/**
 * 
 * Tree class helps to transform dataset (grid) to tree of maps (values are maps)  
 * 
 * @author mjs
 *
 */
class Tree {
	
	String[] columns = null;
	Map<String, Object> root = null;
	
	public Tree(String[] columns) {
		this.columns = columns;
		this.root = new HashMap<String, Object>();
	}
	
	public void add(Map<String, Object> record) {
		Map<String, Object> tnode, cnode = root;
		Object o = null;
		//drill until the last but one column
		String c = null;
		for(int i = 0; i < columns.length-1; i++) {
			c = columns[i];
			o = cnode.get(record.get(c));
			if (o == null) {
				tnode = new HashMap<String, Object>();
				cnode.put(String.valueOf(record.get(c)), tnode);
				cnode = tnode; 
			} else {
				cnode = (Map<String, Object>) o;
			}
		}

		List<Map<String, Object>> items = (List<Map<String, Object>>) cnode.get(String.valueOf(record.get(columns[columns.length-1])));
		
		if (items == null) {
			items = new ArrayList<Map<String, Object>>();
			cnode.put(String.valueOf(record.get(columns[columns.length-1])), items);
		}
		items.add(record);
	}

	public Map<String, Object> root() {
		return this.root;
	}
	
}
/**
 * TreeList helps to transform a dataset (grid) to tree of array-list (values are arrays of maps)
 * 
 * @author mjs
 *
 */
class TreeList extends Tree {
	
	public TreeList(String[] columns) {
		super(columns);
	}

	@Override
	public void add(Map<String, Object> record) {
		Map<String, Object> tnode = null, cnode = root;
		List<Map<String, Object>> tlist = null;
		Object o = null;
		//drill until the last but one column
		String c = null;
		for(int i = 0; i < columns.length; i++) {
			c = columns[i];
			tlist = (List<Map<String, Object>>)cnode.get(c);
			if (tlist == null) {
				tlist = new ArrayList<Map<String, Object>>();
				cnode.put(c, tlist);
			}
			for(Map<String, Object> entry : tlist) {
				if (StringUtils.equals(String.valueOf(entry.get(c)), 
									   String.valueOf(record.get(c)))) {
					tnode = entry;
					break;
				}
			}
			if (tnode == null) {
				tnode = new HashMap<String, Object>();
				tnode.put(c, record.get(c));
				tlist.add(tnode);
			}
			cnode = tnode;
			tnode = null;
		}
		tlist = (List<Map<String, Object>>)cnode.get("items");
		if (tlist == null) {
			tlist = new ArrayList<Map<String, Object>>();
			cnode.put("items", tlist);
		}
		tlist.add(record);
	}
}