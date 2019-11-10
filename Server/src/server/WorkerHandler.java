package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import common.Resposta;
import common.Tarefa;

public class WorkerHandler extends Thread {
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Server server;

	public WorkerHandler(Server server, ObjectInputStream in, ObjectOutputStream out)
			throws IOException {
		this.server = server;
		this.out = out;
		this.in = in;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Tarefa t = server.getTasks().poll();
				out.writeObject(t);
				Resposta r = (Resposta) in.readObject();
				server.enviarResposta(r, t.getClientID());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
