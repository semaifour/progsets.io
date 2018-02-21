package io.procsets.proc.impl;

import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;

import io.procsets.proc.Procedure;
import io.procsets.proc.Procontext;

/**
 * 
 * Base class for all machine learnig procs/algorithms 
 * 
 * @author msathaia
 *
 */
public class MachineLeaningProc extends Procedure {
	
	private static final long serialVersionUID = 4262207398833836899L;

	@Override
	public Procontext run(Procontext pc) {

		try {
			assertNotNullParams("saveas", "view");
			Map<String, String> viewparams = settings(pc);
			
			pc.pushProcStack(true, this, "DONE");
		} catch (Exception e) {
			LOG().error("proc-machinel-run exception", e);
			pc.pushProcStack(false, this, (isDebug() ? ExceptionUtils.getStackTrace(e) : e.getMessage()));
		}
		return pc;
	}
}
