package poo2.sockets.exemplo1;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class SocketCliente {
	
	
	public static void main(String[] args) throws IOException {
        
		String ip = "localhost";
		
		Socket socket = new Socket(ip, 59090);
        Scanner in = new Scanner(socket.getInputStream());
        System.out.println("Server response: " + in.nextLine());
    }
	

}
