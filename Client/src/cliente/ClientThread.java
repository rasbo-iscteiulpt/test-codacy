package cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import javax.swing.text.BadLocationException;

import common.Resposta;

public class ClientThread extends Thread {

	private Client client;
	private ObjectInputStream in;
	private boolean running = true;
	private Socket socket;

	public ClientThread(Client client, Socket socket, ObjectInputStream in) {
		this.in = in;
		this.client = client;
		this.socket = socket;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		while (running) {
			Object o;
			try {
				o = in.readObject();
				if (o instanceof ArrayList) {
					client.organizarLista((ArrayList<Resposta>) o);
					client.enableButton(true);
				}
				if (o instanceof String) {
					client.mostrar((String) o);
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				try {
					in.close();
					running = false;
				} catch (IOException e1) {
					System.exit(-1);
				}
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
