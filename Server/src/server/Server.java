package server;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import common.Noticia;
import common.Pedido;
import common.Resposta;
import common.Tarefa;

public class Server {

	private ArrayList<Noticia> news = new ArrayList<Noticia>();
	private BlockingQueue<Tarefa> tarefas = new BlockingQueue<Tarefa>();
	private ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();
	private ArrayList<WorkerHandler> workers = new ArrayList<WorkerHandler>();

	public static final int PORTO = 8080;
	private ServerSocket ss;
	private int counterID = 0;

	public Server() throws IOException {
		ss = new ServerSocket(PORTO);
		listar();
	}

	private void listar() {
		File[] files = new File("news29out").listFiles(new FileFilter() {
			public boolean accept(File f) {
				if (f.getName().endsWith(".txt"))
					return true;
				else
					return false;
			}
		});
		ler(files);
	}

	private void ler(File[] files) {
		for (File f : files) {
			try {
				Scanner sc = new Scanner(f, "UTF-8");
				String titulo = sc.nextLine();
				String texto = sc.nextLine();
				news.add(new Noticia(titulo, texto, f.getName()));
				sc.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public String identificarNoticia(String nomeFicheiro) {
		String texto = "";
		for (Noticia n : news) {
			if (nomeFicheiro.equals(n.getNomeFicheiro())) {
				texto = n.getTexto();
			}
		}
		return texto;
	}

	public void criarTarefa(Pedido p, int id) {
		if (p.getId() == 0) {
			for (Noticia n : news) {
				try {
					tarefas.offer(new Tarefa(p.getExpression(), n, id));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else if (p.getId() == 1) {
			identificarNoticia(p.getExpression());
		}
	}

	public BlockingQueue<Tarefa> getTasks() {
		return tarefas;
	}

	public void servir() throws IOException {
		while (true) {
			Socket s = ss.accept();

			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(s.getInputStream());

			try {
				Object o = in.readObject();
				if (o instanceof Integer && ((int) o) == 0) {
					ClientHandler client = new ClientHandler(s, this, in, out, counterID);
					clients.add(client);
					client.start();
					counterID++;
				}
				if (o instanceof Integer && ((int) o) == 1) {
					WorkerHandler worker = new WorkerHandler(this, in, out);
					workers.add(worker);
					worker.start();
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public synchronized void enviarResposta(Resposta resposta, int clientID) {
		for (ClientHandler c : clients) {
			if (c.getIdentifier() == clientID) {
				c.getBarrier().barrierPost();
				if (resposta.getIndices().size() != 0) {
					c.adicionarResposta(resposta);
				}
			}
		}
	}

	public int numNoticias() {
		return news.size();
	}

	public synchronized void fechar(int id) {
		Iterator<ClientHandler> it = clients.iterator();
	    while (it.hasNext()) {
	        if (it.next().getIdentifier() == id) {
	            it.remove();
	            break;
	        }
	    }
	}

	public static void main(String[] args) {
		try {
			System.out.println("Launched server...");
			Server s = new Server();
			s.servir();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
