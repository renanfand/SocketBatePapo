package poo2.sockets.exemplo4;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Set;
import java.util.Date;
import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ChatServer {

    private static Set<String> names = new HashSet<>();
    private static Set<String> writingNames = new HashSet<>();
    private static Set<PrintWriter> writers = new HashSet<>();

    public static void main(String[] args) throws Exception {
        System.out.println("Servidor ativo...");
        Executor pool = Executors.newFixedThreadPool(500);
        
        try (ServerSocket listener = new ServerSocket(59001)) {
            while (true) {
                pool.execute(new Handler(listener.accept()));
            }
        }
    }

    private static class Handler implements Runnable {
        private String name;
        private Socket socket;
        private Scanner in;
        private PrintWriter out;

        public Handler(Socket socket) {
            this.socket = socket;
        }
        
        private String getDateTime() { 
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
            Date date = new Date(); 
            return dateFormat.format(date); 
        }

        public void run() {
            try {
                in = new Scanner(socket.getInputStream());
                out = new PrintWriter(socket.getOutputStream(), true);

                while (true) {
                    out.println("SUBMITNAME");
                    name = in.nextLine();
                    if (name == null) {
                        return;
                    }
                    synchronized (names) {
                        if (!name.isEmpty() && !names.contains(name)) {
                            names.add(name);
                            break;
                        }
                    }
                }

                out.println("NAMEACCEPTED " + name);
                out.println("ONLINENAMES" + names.toString());
                out.println("WRITTINGNAMES" + writingNames.toString());
                for (PrintWriter writer : writers) {

                	writer.println("ONLINENAMES" + names.toString());
                    writer.println("MESSAGE " + name + " entrou agora!" + "\t\t\t" + this.getDateTime());
                }
                writers.add(out);

                while (true) {
                    String input = in.nextLine();
                    if (input.toLowerCase().startsWith("/quit")) {
                        return;
                    }
                    if (input.startsWith("WRITTING")) {
                    	writingNames.add(name);
                    	
                        for (PrintWriter writer : writers) {
                            writer.println("WRITTINGNAMES" + writingNames.toString());
                        }
                    } else if (input.startsWith("STOPPINGWRITTING")) {
                    	writingNames.remove(name);

	                	 for (PrintWriter writer : writers) {
	                         writer.println("WRITTINGNAMES" + writingNames.toString());
	                     }
                    }
                    else {

                        for (PrintWriter writer : writers) {
                            writer.println("MESSAGE " + name + ": " + input + "\t\t\t" + this.getDateTime());
                        }	
                    }
                }
            } 
            catch (Exception e) {
                System.out.println(e);
            } 
            finally {
                if (out != null) {
                	writers.remove(out);
                	
                }
                if (name != null) {
                    //System.out.println(name + " saiu!" + "\t\t\t" + this.getDateTime());
                    names.remove(name);
                    writingNames.remove(name);

                    for (PrintWriter writer : writers) {

                    	writer.println("WRITTINGNAMES" + writingNames.toString());
                    	writer.println("ONLINENAMES" + names.toString());
                        writer.println("MESSAGE " + name + " saiu! " + "\t\t\t" + this.getDateTime());
                    }
                }
                try { socket.close(); } catch (IOException e) {}
            }
        }
    }

}
