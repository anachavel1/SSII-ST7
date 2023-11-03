package intento;

import java.io.*;
import javax.net.ssl.*;
import java.security.SecureRandom;
import java.security.KeyStore;
import java.io.FileInputStream;

public class EsteSiQueSi_Servidor {

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

            while (true) {
                final SSLSocket socket = (SSLSocket) serverSocket.accept();

                // Usar un hilo para manejar la conexión.
                new Thread(() -> {
                    try {
                        handleConnection(socket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
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
