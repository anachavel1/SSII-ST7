package intento;

import java.io.*;
import javax.net.ssl.*;
import javax.swing.JOptionPane;

import java.security.SecureRandom;

public class EsteSiQSi_Cliente {

	public static void main(String[] args) {
		try {
			SSLContext sslContext = SSLContext.getInstance("TLSv1.3");
			sslContext.init(null, null, new SecureRandom());

			SSLSocketFactory factory = sslContext.getSocketFactory();
			SSLSocket socket = (SSLSocket) factory.createSocket("localhost", 7070);

			// Crea un BufferedReader para leer la respuesta del servidor
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			// Crea un PrintWriter para enviar el mensaje al servidor
			PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

//            // Envía el mensaje al servidor
//            output.println("Hola");
//            output.flush();
			//-------------------------------------------------------------------
			// prompt user for user name
			String msg = JOptionPane.showInputDialog(null, "Introduzca su mensaje: ");
			// Envía el mensaje al servidor
			// printLn() => imprime una nueva línea después de imprimir "msg"
			output.println(msg);
			// .flush() => se asegura me enviar a través de la conexión de red los datos
			// pendientes
			output.flush();
			//--------------------------------------------------------------------

			// Lee la respuesta del servidor
			String response = input.readLine();
			System.out.println("Respuesta del servidor: " + response);

			// Muestra la respuesta al usuario
            JOptionPane.showMessageDialog(null, "Respuesta del servidor: " + response);
            
			// Cierra los flujos y el socket
			output.close();
			input.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
