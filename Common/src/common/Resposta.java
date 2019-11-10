package common;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Resposta implements Serializable{

	private int nVezes;
	private String nomeFicheiro;
	private ArrayList<Integer> indices;
	private String titulo;
	
	public Resposta(int nVezes, String nomeFicheiro, ArrayList<Integer> indices, String titulo) {
		this.nVezes = nVezes;
		this.nomeFicheiro = nomeFicheiro;
		this.indices = indices;
		this.titulo = titulo;
	}

	public int getnVezes() {
		return nVezes;
	}

	public String getNomeFicheiro() {
		return nomeFicheiro;
	}

	public ArrayList<Integer> getIndices() {
		return indices;
	}

	public String getTitulo() {
		return titulo;
	}
	
	@Override
	public String toString() {
		return nVezes + " - " + titulo;
	}
}
