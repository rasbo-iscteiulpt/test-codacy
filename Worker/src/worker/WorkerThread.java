package worker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import common.Resposta;
import common.Tarefa;

public class WorkerThread extends Thread {

	private ObjectOutputStream out;
	private ObjectInputStream in;
	private boolean running = true;

	public WorkerThread(ObjectInputStream in, ObjectOutputStream out) {
		this.out = out;
		this.in = in;
	}

	@Override
	public void run() {
		while (running) {
			try {
				Tarefa t = (Tarefa) in.readObject();
				ArrayList<Integer> indices = t.getNoticia().procurar(t.getExpressao());
				Resposta resposta = new Resposta(indices.size(), t.getNoticia().getNomeFicheiro(), indices,
						t.getNoticia().getTitulo());
				out.writeObject(resposta);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				try {
					out.close();
					running  = false;
				} catch (IOException e1) {
					System.exit(-1);
				}
			}
		}

	}
}
