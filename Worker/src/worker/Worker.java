package worker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Worker {

	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	public void ligarAoServidor() throws IOException {
		InetAddress address = InetAddress.getByName(null);

		socket = new Socket(address, 8080);

		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());

		out.writeObject(1);

		WorkerThread w = new WorkerThread(in, out);
		w.start();
	}

	public static void main(String[] args) {
		try {
			Worker w = new Worker();
			w.ligarAoServidor();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
