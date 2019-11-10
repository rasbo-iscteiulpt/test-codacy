package cliente;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

import common.Pedido;
import common.Resposta;

public class Client {

	private JFrame frame;
	private JPanel centerPanel;
	private JPanel upperPanel;
	private JList<Resposta> newsList;
	private JTextArea textArea;
	private JButton search;
	private JTextField searchText;
	private NewsModel model = new NewsModel();

	private ArrayList<Resposta> respostas = new ArrayList<Resposta>();
	private String palavraPesquisada;

	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Socket socket;
	
	private ClientThread thread;

	private void gui() {

		JScrollPane listScrollPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		JScrollPane textScrollPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		frame = new JFrame("ISCTE Searcher");
		frame.setLayout(new BorderLayout());
		frame.setSize(500, 300);

		centerPanel = new JPanel(new GridLayout(1, 2));
		upperPanel = new JPanel();

		newsList = new JList<Resposta>(model);
		listScrollPane.setViewportView(newsList);

		textArea = new JTextArea();
		textArea.setEditable(false);
		textScrollPane.setViewportView(textArea);

		centerPanel.add(listScrollPane);
		centerPanel.add(textScrollPane);

		search = new JButton("Search");

		searchText = new JTextField();
		searchText.setColumns(20);

		upperPanel.add(searchText);
		upperPanel.add(search);

		frame.add(upperPanel, BorderLayout.NORTH);
		frame.add(centerPanel, BorderLayout.CENTER);

		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				try {
					out.close();
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				frame.dispose();
				System.exit(0);
			}
		});
		
		search.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (searchText.getText() != null && !searchText.getText().equals("")) {
					newsList.clearSelection();
					textArea.setText("");
					search.setEnabled(false);
					model.clear();
					palavraPesquisada = searchText.getText();
					pedir(palavraPesquisada, 0);
				}
			}
		});

		newsList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (newsList.getSelectedIndex() != -1) {
					pedir(newsList.getSelectedValue().getNomeFicheiro(), 1);
				}
			}
		});
	}

	public void pedir(String expressao, int id) {
		try {
			Pedido p = new Pedido(expressao, id);
			out.writeObject(p);
		} catch (IOException e) {
			System.out.println("Server isn't running");
			System.exit(0);
		}
	}

	public void organizarLista(ArrayList<Resposta> o) {
		respostas = (ArrayList<Resposta>) o;
		respostas.sort(new ComparadorOcurr());
		model.fill(respostas);
	}

	public void mostrar(String texto) throws BadLocationException, IOException, ClassNotFoundException {
		textArea.setText(texto);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
	}

	//por resolver - swing utilities
	private void destacarPalavra(String palavra, Resposta r) throws BadLocationException {
		Highlighter hilit = new DefaultHighlighter();
		Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.LIGHT_GRAY);
		textArea.setHighlighter(hilit);
		for (int i : r.getIndices()) {
			hilit.addHighlight(i, i + palavra.length(), painter);
		}
	}

	public void ligarAoServidor() throws IOException {
		InetAddress address = InetAddress.getByName(null);

		socket = new Socket(address, 8080);

		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());
		
		out.writeObject(0);

		thread = new ClientThread(this, socket, in);
		thread.start();
	}

	public static void main(String[] args) {
		Client c = new Client();
		try {
			c.ligarAoServidor();
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					c.gui();				
				}
			});
		} catch (IOException e) {
			System.out.println("Server isn't running");
		}
	}

	public void enableButton(boolean state) {
		search.setEnabled(state);
	}
}
