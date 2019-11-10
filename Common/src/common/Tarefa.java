package common;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Tarefa implements Serializable{

	private String expressao;
	private Noticia noticia;
	private int handlerID;
	
	public Tarefa(String expressao, Noticia noticia, int clientID) {
		this.expressao = expressao;
		this.noticia = noticia;
		this.handlerID = clientID;
	}
	
	public Noticia getNoticia() {
		return noticia;
	}
	
	public String getExpressao() {
		return expressao;
	}

	public int getClientID() {
		return handlerID;
	}
	
	@Override
	public String toString() {
		return expressao + " " + noticia.getTitulo();
	}
}
