/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socket.server;

import java.io.*;
import java.net.*;
import static java.lang.Thread.sleep;

/**
 *
 * @author 5G
 */
public class SocketServer {

    //socket
    private Socket connessione = null;
    private ServerSocket socket = null;

    //input
    private InputStream in = null;
    private InputStreamReader input = null;
    private BufferedReader sIN = null;

    //output
    private OutputStream out = null;
    private PrintWriter sOUT = null;
    private Ui mask = null;

    SocketServer(int porta) {
        //interface
        mask = new Ui("Server");
        mask.setVisible(true);

        try {
            //apertura porta in ascolto
            socket = new ServerSocket(porta);
            System.out.println("Il server è in ascolto sulla porta " + porta);

            //accetta la connessione
            System.out.println("Il server è in attesa di connessioni.");
            connessione = socket.accept();
            System.out.println("Connessione stabilita.");

            //input
            in = connessione.getInputStream();
            input = new InputStreamReader(in);
            sIN = new BufferedReader(input);

            //output
            out = connessione.getOutputStream();
            sOUT = new PrintWriter(out);

            //action listener for send button
            mask.send.addActionListener(e -> {
                //append message to chatBox
                String messageOUT = mask.field.getText();
                mask.sendMessage(mask.actor, messageOUT);

                //send message and empty buffer
                sOUT.println(messageOUT);
                sOUT.flush();
                System.out.println("sending -> " + messageOUT);
            });
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    void process() throws IOException, InterruptedException {
        String message;
        while (true) {
            if (sIN.ready()) {
                System.out.print("received <- ");
                message = sIN.readLine();
                System.out.println(message);
                mask.getMessage("Client", message);
            }
            sleep(100);

        }
    }

    public static void main(String[] args) {
        //create server object
        SocketServer server = new SocketServer(3333);

        //process messages
        try {
            server.process();
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }
}
