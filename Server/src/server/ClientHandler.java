package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import common.Pedido;
import common.Resposta;

public class ClientHandler extends Thread {

	private Socket client;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Server server;
	private int id;
	private ArrayList<Resposta> respostas = new ArrayList<Resposta>();
	private Barrier barrier;
	private boolean running = true;

	public ClientHandler(Socket client, Server server, ObjectInputStream in, ObjectOutputStream out, int id)
			throws IOException {
		this.client = client;
		this.server = server;
		this.in = in;
		this.out = out;
		this.id = id;
		barrier = new Barrier(server.numNoticias());
	}

	public int getIdentifier() {
		return id;
	}

	public void enviarRespostas() throws IOException {
		ArrayList<Resposta> aux = new ArrayList<Resposta>();
		for (Resposta r : respostas) {
			aux.add(r);
		}
		out.writeObject(aux);
		respostas.clear();
	}

	public void adicionarResposta(Resposta resposta) {
		respostas.add(resposta);
	}

	@Override
	public void run() {
		while (running) {
			try {
				try {
					Object o = in.readObject();
					if (o instanceof Pedido) {
						if (((Pedido) o).getId() == 0) {
							server.criarTarefa((Pedido) o, this.id);
							barrier.barrierWait();
							enviarRespostas();
						} else if (((Pedido) o).getId() == 1) {
							out.writeObject(server.identificarNoticia(((Pedido) o).getExpression()));
						}
					}
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (IOException e) {
				try {
					server.fechar(id);
					in.close();
					out.close();
					client.close();
					running = false;
				} catch (IOException e1) {
				}
			}
		}
	}

	public Barrier getBarrier() {
		return barrier;
	}
}
