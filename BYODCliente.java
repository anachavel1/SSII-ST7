package Mens_Servidor_300Hilos;

import java.io.*;
import javax.net.ssl.*;
import javax.swing.JOptionPane;

import java.security.SecureRandom;

public class BYODCliente {

	public static void main(String[] args) {
		try {
			SSLContext sslContext = SSLContext.getInstance("TLSv1.3");
			sslContext.init(null, null, new SecureRandom());

			SSLSocketFactory factory = sslContext.getSocketFactory();
			SSLSocket socket = (SSLSocket) factory.createSocket("localhost", 7070);

			// Configura la ciphersuite deseada
            String[] enabledCipherSuites = { "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384" };
            socket.setEnabledCipherSuites(enabledCipherSuites);
            
			// Crea un BufferedReader para leer la respuesta del servidor
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			// Crea un PrintWriter para enviar el mensaje al servidor
			PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

//			// Ventana para ingresar un mensaje
//			String msg = JOptionPane.showInputDialog(null, "Introduzca su mensaje: ");
			
			// Modifica el mensaje a "usuario/contrasena"
            String msg = "usuario/contrasena";
            
			// Envía el mensaje al servidor --> printLn()
			output.println(msg);
			// .flush() => se asegura me enviar a través de la conexión de red los datos pendientes
			output.flush();
            
			
			// Lee la respuesta del servidor
			String response = input.readLine();
			System.out.println("Respuesta del servidor: " + response);

			// Muestra la respuesta al usuario
//            JOptionPane.showMessageDialog(null, "Respuesta del servidor: " + response);
            
			// Cierra los flujos y el socket
			output.close();
			input.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
