package common;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Noticia implements Serializable{

	private String titulo;
	private String texto;
	private String nomeFicheiro;
	
	public Noticia(String titulo, String texto, String nomeFicheiro) {
		this.titulo = titulo;
		this.texto = texto;
		this.nomeFicheiro = nomeFicheiro;
	}
	
	public String getNomeFicheiro() {
		return nomeFicheiro;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getTexto() {
		return texto;
	}
	
	public ArrayList<Integer> procurar(String word) {
		ArrayList<Integer> indices = new ArrayList<Integer>();
			int ultIndice = 0;
			while (ultIndice != -1) {
				ultIndice = texto.toLowerCase().indexOf(word.toLowerCase(), ultIndice);
				if (ultIndice != -1) {
					indices.add(ultIndice);
					ultIndice += word.length();
				}
			}
		return indices;
	}
}
