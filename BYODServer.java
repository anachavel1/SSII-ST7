package Mens_Servidor_300Hilos;

import java.io.*;
import javax.net.ssl.*;
import java.security.SecureRandom;
import java.security.KeyStore;
import java.io.FileInputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BYODServer {

    public static void main(String[] args) {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLSv1.3");
            
            String keystoreFile = System.getProperty("user.home") + "/Desktop/cositas.jks";
            KeyStore keyStore = KeyStore.getInstance("JKS");
            char[] passphrase = "123456".toCharArray();
            keyStore.load(new FileInputStream(keystoreFile), passphrase);

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore, passphrase);

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
            trustManagerFactory.init(keyStore);

            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());

            SSLServerSocketFactory factory = sslContext.getServerSocketFactory();
            SSLServerSocket serverSocket = (SSLServerSocket) factory.createServerSocket(7070);

            System.err.println("Servidor iniciado. Esperando conexiones...");

            // Crear un pool de hilos para manejar las conexiones entrantes.
            ExecutorService executorService = Executors.newFixedThreadPool(300);

            while (true) {
                final SSLSocket socket = (SSLSocket) serverSocket.accept();

                // Usar el pool de hilos para manejar la conexión.
                executorService.submit(() -> {
                    try {
                        handleConnection(socket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void handleConnection(SSLSocket socket) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String msg = input.readLine();

        PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

        if (msg != null && msg.equals("Hola")) {
            output.println("Bienvenido al servidor");
        } else {
            output.println("Mensaje incorrecto.");
        }

        output.close();
        input.close();
        socket.close();
    }
}
