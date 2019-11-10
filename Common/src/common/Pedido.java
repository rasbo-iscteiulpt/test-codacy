package common;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Pedido implements Serializable{
	
	private String someExpression;
	private int id;
	
	public Pedido(String someExpression, int id) {
		this.someExpression = someExpression;
		this.id = id;
	}

	public String getExpression() {
		return someExpression;
	}
	
	public int getId() {
		return id;
	}
}
