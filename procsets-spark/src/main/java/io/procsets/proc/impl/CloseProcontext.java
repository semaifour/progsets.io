package io.procsets.proc.impl;

import io.procsets.proc.Procedure;
import io.procsets.proc.Procontext;

/**
 * 
 * Closes the procontext
 * 
 * @author mjs
 *
 */
public class CloseProcontext extends Procedure {

	private static final long serialVersionUID = -8798708000015343155L;

	@Override
	public Procontext run(Procontext pc) {
		try {
			if (pc != null) pc.close();
		} catch (Exception e) {
			LOG().error("Error closing procontext", e);
		}
		return null;
	}
	
}
