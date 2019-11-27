package poo2.sockets.exemplo1;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class SocketServidor {

	public static void main(String[] args) throws IOException {
		
		try (ServerSocket listener = new ServerSocket(59090)) {

			System.out.println("Servidor aguardando requisições...");

			while (true) {
				try (Socket socket = listener.accept()) {
		
					PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
					out.println(new Date().toString());
				}
			}
		}
	}
}
