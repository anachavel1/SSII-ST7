package intento;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.net.ssl.*;

public class BYODServer {

	public static void main(String[] args) throws IOException, InterruptedException {

		try {
			SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
			SSLServerSocket serverSocket = (SSLServerSocket) factory.createServerSocket(3343);

			// wait for client connection and check login information

			System.err.println("Esperando conexiones del cliente ....");
			SSLSocket socket = (SSLSocket) serverSocket.accept();

			// abre un BufferedReader para leer los datos del cliente
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String msg = input.readLine();

			// abre un PrintWriter para enviar datos al cliente
			PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

			if (msg.equals("Hola")) {
				output.println("Welcome to the Server");
			} else {
				output.println("Incorrect message.");
			}

			output.close();
			input.close();
			socket.close();

		} // end try

		// handle exception communicating with client
		catch (IOException ioException) {
			ioException.printStackTrace();
		}

	}

}
