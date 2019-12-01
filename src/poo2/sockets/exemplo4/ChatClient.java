package poo2.sockets.exemplo4;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatClient {

	String serverAddress;
    Scanner in;
    PrintWriter out;
    JFrame frame = new JFrame("Bate Papo");
    JTextField textField = new JTextField(50);
    JTextArea messageArea = new JTextArea(16, 50);
    JTextArea onlineArea = new JTextArea(5, 10);

    public ChatClient(String serverAddress) {
        this.serverAddress = serverAddress;

        textField.setEditable(false);
        messageArea.setEditable(false);
        onlineArea.setEditable(false);
        frame.getContentPane().add(textField, BorderLayout.SOUTH);
        frame.getContentPane().add(new JScrollPane(messageArea), BorderLayout.CENTER);
        frame.getContentPane().add(onlineArea, BorderLayout.EAST);
        frame.pack();
        
        onlineArea.setText("Usu�rios online: \n");

        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                out.println(textField.getText());
                textField.setText("");
            }
        });
    }

    private String getName() {
        return JOptionPane.showInputDialog(
            frame,
            "Seu nome:",
            "Nome do usuario",
            JOptionPane.PLAIN_MESSAGE
        );
    }

    private void run() throws IOException {
        try {
            Socket socket = new Socket(serverAddress, 59001);
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);

            while (in.hasNextLine()) {
                String line = in.nextLine();
                
                if (line.startsWith("SUBMITNAME")) {
                    out.println(getName());
                } 
                else if (line.startsWith("ONLINENAMES")) {

                    System.out.println("ONLINENAMES");
                    onlineArea.setText("Usu�rios online: \n");
                    line = line.replace("[", "").replace("]", "");
                    String[] onlineUsers = line.substring(11).split(",");
                    System.out.println(onlineUsers.length);
                    for (String onlineUser : onlineUsers) {
                    	
                        onlineArea.append(onlineUser + "\n");	
					}
                }
                else if (line.startsWith("NAMEACCEPTED")) {
                    this.frame.setTitle("Bate Papo - " + line.substring(13));
                    textField.setEditable(true);
                } 
                else if (line.startsWith("MESSAGE")) {
                    messageArea.append(line.substring(8) + "\n");
                }
            }
        } 
        finally {
            frame.setVisible(false);
            frame.dispose();
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Pass the server IP as the sole command line argument");
            return;
        }
        
        ChatClient client = new ChatClient(args[0]);
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run();
    }

}
