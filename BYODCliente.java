package intento;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import javax.net.ssl.*;
import javax.swing.JOptionPane;

public class BYODCliente {

	public static void main(String[] args) throws IOException {
		try {

			SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
			SSLSocket socket = (SSLSocket) factory.createSocket("localhost", 7070);

//			INICIA proceso de establecimiento de un conexión segura, a través del protocolo SSL/TLS
//			** protocolo SSL/TLS => intercambio información segura cliente-servidor establece
//			 						una conexión cifrada y autenticada
			socket.startHandshake();

//			.getSession() => devuelve la sesión SSL que usamos en la conexión.

//			.getCipherSuite() => obtiene el nomb. del algoritmo de cifrado que usamos en la
//								 conexión SSL/TLS. Nos dice como se cifran los datos transmitidos
//				Ej: TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384_P384 --> pag 9 (PDF - PAI3)
			System.out.println("Sesion cipher suite is" + socket.getSession().getCipherSuite());

			// crea un BufferedReader para leer la respuesta del servidor
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			// crea un PrintWriter para eviar el login del usuario
			PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

			// prompt user for user name
			String msg = JOptionPane.showInputDialog(null, "Introduzca su mensaje: ");
			// send user name to server
			// printLn() => imprime una nueva línea después de imprimir "msg"
			output.println(msg);

			// .flush() => se asegura me enviar a través de la conexión de red los datos
			// pendientes
			output.flush();

			// read response from server
			String response = input.readLine(); // lee la respuesta del servidor
			System.out.println(response); // muestra la respuesta al cliente

			// display response to user
			JOptionPane.showMessageDialog(null, response);

			// clean up streams and Socket
			output.close();
			input.close();
			socket.close();

		} // end try

		// handle exception communicating with server
		catch (IOException ioException) {
			ioException.printStackTrace();
		}

		// exit application
		finally {
			System.exit(0);
		}

	}
}
