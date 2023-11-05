package pruebas;

import java.io.*;
import javax.net.ssl.*;
import java.security.SecureRandom;

public class Prueba_Client_300_hilos {

    public static void main(String[] args) {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLSv1.3");
            sslContext.init(null, null, new SecureRandom());

            SSLSocketFactory factory = sslContext.getSocketFactory();

            // Crear un array de hilos para enviar mensajes simultáneos
            Thread[] threads = new Thread[300];

            for (int i = 0; i < 300; i++) {
                final int threadIndex = i;

                // Crear un hilo para enviar un mensaje al servidor
                threads[i] = new Thread(() -> {
                    try {
                        SSLSocket socket = (SSLSocket) factory.createSocket("localhost", 7070);

                        // Crea un BufferedReader para leer la respuesta del servidor
                        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                        // Crea un PrintWriter para enviar el mensaje al servidor
                        PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

                        // Generar un usuario y contraseña aleatorios
                        String usuario = "usuario" + (int)(Math.random() * 1000);
                        String contrasena = "contrasena" + (int)(Math.random() * 1000);

                        // Generar un mensaje aleatorio
                        String msg;
                        if (Math.random() < 0.9) {
                            // Mensaje correcto
                            msg = usuario + "/" + contrasena;
                        } else {
                            // Mensaje incorrecto
                            msg = "Mensaje incorrecto";
                        }

                        // Envía el mensaje al servidor
                        output.println(msg);
                        output.flush();

                        // Lee la respuesta del servidor
                        String response = input.readLine();
                        System.out.println("Respuesta del servidor para el hilo " + threadIndex + ": " + response);

                        // Cierra los flujos y el socket
                        output.close();
                        input.close();
                        socket.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                // Iniciar el hilo
                threads[i].start();
            }

            // Esperar a que todos los hilos terminen
            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
