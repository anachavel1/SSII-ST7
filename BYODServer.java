package intento;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.net.ssl.*;

public class BYODServer {

    private static ServerSocket serverSocket;

    public static void main(String[] args) throws IOException {
        SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        serverSocket = (SSLServerSocket) factory.createServerSocket(7070);

        System.err.println("\nServidor iniciado. Esperando conexiones...");

        // Crear un pool de hilos para manejar las conexiones entrantes.
        ExecutorService executorService = Executors.newFixedThreadPool(300);

        while (true) {
            final SSLSocket socket = (SSLSocket) serverSocket.accept();

            // Usar un hilo del pool para manejar la conexión.
            executorService.execute(() -> {
                try {
                    handleConnection(socket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    static void handleConnection(SSLSocket socket) throws IOException {
        //enCiphersuite indicara las cipherSuites que queremos usar
        String[] enCiphersuite = ((SSLServerSocket) serverSocket).getEnabledCipherSuites();
		((SSLServerSocket) serverSocket).setEnabledCipherSuites(enCiphersuite);
		
		System.out.println("Enabled ciphersuites are: ");
		for (String cs: Arrays.asList(enCiphersuite)) {
			System.out.println("\t" + cs);
		}
        
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String msg = input.readLine();
        System.err.println(msg);

        PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

        if (msg != null && msg.contains("/")) {
            output.println("Bienvenido al servidor");
        } else {
            output.println("Mensaje incorrecto.");
        }

        output.close();
        input.close();
        socket.close();
    }
}
