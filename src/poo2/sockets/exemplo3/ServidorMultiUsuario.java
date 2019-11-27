package poo2.sockets.exemplo3;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.jar.JarOutputStream;

import javax.swing.JOptionPane;

public class ServidorMultiUsuario {

	public static void main(String[] args) throws Exception {
		try (ServerSocket listener = new ServerSocket(59898)) {
			System.out.println("Servidor executando...");
			ExecutorService pool = Executors.newFixedThreadPool(20);
			while (true) {
				pool.execute(new ServicoSocket(listener.accept()));
			}
		}
	}

	private static class ServicoSocket implements Runnable {

		private Socket socket;

		ServicoSocket(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			System.out.println("Conectado: " + socket);
			
			try {
				Scanner in = new Scanner(socket.getInputStream());
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				
				while (in.hasNextLine()) {
					out.println(in.nextLine().toUpperCase());
				}
			} 
			
			catch (Exception e) {
				System.out.println("Erro:" + socket);
			} 
			
			finally {
				try {
					socket.close();
				} 
				catch (IOException e) {}
				System.out.println("Socket fechado: " + socket);
			}
		}
	}

}
