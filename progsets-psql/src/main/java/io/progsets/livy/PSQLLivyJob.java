package io.progsets.livy;


import java.util.HashMap;
import java.util.Map;

import com.cloudera.livy.Job;
import com.cloudera.livy.JobContext;

import io.progsets.common.Appproperties;
import io.progsets.common.PSQL;
import io.progsets.common.Restponse;
import io.progsets.proc.Procedure;
import io.progsets.proc.Procontext;

public class PSQLLivyJob implements Job<Restponse<Object>> {

	private static final long serialVersionUID = -1479371823327703813L;

	private Appproperties appproperties = null;
	private String psql = null;
	private String name = null;
	
	public PSQLLivyJob(Appproperties appproperties, String name, String psql) {
		this.appproperties = appproperties;
		this.psql = psql;
		this.name = name;
	}
	
	@Override
	public Restponse<Object> call(JobContext jc) throws Exception {
		Restponse<Object> result = null;
		Procontext pc = null;
		try {	
			PSQL psqlo = PSQL.parse(psql);
			pc = new Procontext(name, appproperties.master(), jc, appproperties);
			for(Procedure proc : psqlo.getProcedures()) {
				proc.run(pc);
				if (!pc.wasLastProcSuccess()) {
					Map<String, Object> map = new HashMap<String, Object>();
					result = new Restponse<Object>(false, 300).setMessage(pc.procStack().peek().c()).setBody(proc);
					break;
				}
			}
			if (result == null) result = new Restponse<Object>(pc.getResultlist());
		} catch(Exception e) {
			result = new Restponse<Object>(false, 300).setMessage(e.getMessage());
		}
		return result;
	}

}
