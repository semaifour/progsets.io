package io.progsets.proc.impl;

import io.progsets.proc.Procedure;
import io.progsets.proc.Procontext;

public class Noname extends Procedure {

	private static final long serialVersionUID = 1756619078576069135L;

	public Procontext run(Procontext pc) {

		pc.pushProcStack(true, this, "DONE");

		return pc;
		
	}
}
