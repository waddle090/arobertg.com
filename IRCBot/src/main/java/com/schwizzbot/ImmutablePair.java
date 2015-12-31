package com.schwizzbot;

public class ImmutablePair<T1, T2> {
	private T1 t1;
	private T2 t2;
	
	public ImmutablePair(T1 t1, T2 t2){
		this.t1 = t1;
		this.t2 = t2;
	}
	
	public T1 getLeft(){
		return t1;
	}
	public T2 getRight(){
		return t2;
	}
}
