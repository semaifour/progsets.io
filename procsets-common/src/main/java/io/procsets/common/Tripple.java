package io.procsets.common;

public class Tripple<A,B,C> {
	private A a;
	private B b;
	private C c;
	
	public Tripple(A a, B b, C c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}

	public A a() {
		return this.a;
	}
	
	public B b() {
		return this.b;
	}
	
	public C c() {
		return this.c;
	}
	
}
