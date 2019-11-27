package poo2.sockets.exemplo3;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class Cliente {

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.err.println("Passe o endereco IP do servidor como argumento na linha de comando");
			return;
		}
		
		try (Socket socket = new Socket(args[0], 59898)) {
			System.out.println("Entre com as linhas de texto e então Ctrl+D ou Ctrl+C para sair");

			String msg = null;

			while (msg == null || msg.equals("")) {
				msg = JOptionPane.showInputDialog("Cliente");
			}

			JOptionPane.showMessageDialog(null, msg);

			Scanner scanner = new Scanner(System.in);
			Scanner in = new Scanner(socket.getInputStream());

			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

			while (scanner.hasNextLine()) {
				out.println(scanner.nextLine());
				JOptionPane.showMessageDialog(null, in.nextLine());
				//System.out.println(in.nextLine());
			}
		}
	}

}
