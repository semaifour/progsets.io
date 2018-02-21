package io.progsets.test;

import org.junit.Test;

import io.progsets.util.FSGrok;
import oi.thekraken.grok.api.Grok;
import oi.thekraken.grok.api.Match;

public class GrokTest {

	@Test
	public void test() {
		Grok grok = FSGrok.newGrok("%{DATA:nodeapp}-%{DATA:nodeenv}-%{GREEDYDATA:nodenum}");
		Match m = grok.match("clip-prod-100");
		m.captures();
		String s = m.toJson();
		System.out.println(s);
	}

}
