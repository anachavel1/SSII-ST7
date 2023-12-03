package com.example.myapplication;

import android.app.AlertDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class MainActivity extends AppCompatActivity {

    // Setup Server information
    protected static String server = "10.0.2.2";
    //10.0.2.2
    //192.168.1.134
    protected static int port = 7070;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Capturamos el boton de Enviar
        View button = findViewById(R.id.button_send);

        // Llama al listener del boton Enviar
        button.setOnClickListener(view -> showDialog());


    }

    // Creación de un cuadro de dialogo para confirmar pedido
    private void showDialog() throws Resources.NotFoundException {

        EditText ncamas = (EditText) findViewById(R.id.ncamas);
        EditText nmesas = (EditText) findViewById(R.id.nmesas);
        EditText nsillas = (EditText) findViewById(R.id.nsillas);
        EditText nsillones = (EditText) findViewById(R.id.nsillones);
        EditText nidEmpleado = (EditText) findViewById(R.id.idEmpleado);


        String txcamas = ncamas.getText().toString();
        String txmesas = nmesas.getText().toString();
        String txsillas = nsillas.getText().toString();
        String txsillones = nsillones.getText().toString();
        String txidEmpleado = nidEmpleado.getText().toString();

        if (txcamas.matches("")) {
            txcamas = "0";
        }
        if (txmesas.matches("")) {
            txmesas = "0";
        }
        if (txsillas.matches("")) {
            txsillas = "0";
        }
        if (txsillones.matches("")) {
            txsillones = "0";
        }
        if (txidEmpleado.matches("")) {
            txidEmpleado = "0";
        }

        int camas = Integer.parseInt(txcamas);
        int mesas = Integer.parseInt(txmesas);
        int sillas = Integer.parseInt(txsillas);
        int sillones = Integer.parseInt(txsillones);
        int idEmpleado = Integer.parseInt(txidEmpleado);

        if (idEmpleado == 0) {
            // Mostramos un mensaje emergente;
            Toast.makeText(getApplicationContext(), "Indique un numero de empleado válido", Toast.LENGTH_SHORT).show();
        }
        if (camas >= 300 ||
                mesas >= 300 ||
                sillas >= 300 ||
                sillones >= 300) {
            // Mostramos un mensaje emergente;
            Toast.makeText(getApplicationContext(), "Selecciona una cantidad entre 0 y 300", Toast.LENGTH_SHORT).show();
        }
        if (camas == 0 &&
                mesas == 0 &&
                sillas == 0 &&
                sillones == 0) {
            Toast.makeText(getApplicationContext(), "El pedido no puede estar vacío", Toast.LENGTH_SHORT).show();
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Enviar");
            builder.setMessage("Se va a proceder al envio");
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            // Catch ok button and send information
            builder.setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {

                // 1. Extraer los datos de la vista
                String mensaje = camas + "|" + mesas + "|" + sillas + "|" + sillones + "|" + idEmpleado + "|";
                //     mensaje : c###/m###/s###/s###/id#####/


                // 2. Firmar los datos

                // 3. Enviar los datos

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // Conectar al servidor
                            Socket socket = new Socket(server, port);

                            // Enviar datos al servidor
                            OutputStream outputStream = socket.getOutputStream();
                            outputStream.write(mensaje.getBytes());

                            // Recibir respuesta del servidor
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            final String response = bufferedReader.readLine();

                            // Actualizar la interfaz de usuario desde el hilo principal
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d("SocketClient", "Respuesta del servidor: " + response);
                                    // Aquí puedes realizar cualquier acción en la interfaz de usuario con la respuesta del servidor
                                }
                            });

                            // Cerrar el socket
                            socket.close();

                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();



                        Toast.makeText(getApplicationContext(), "Pedido enviado", Toast.LENGTH_SHORT).show();

                // exit application





                Toast.makeText(MainActivity.this, "Petición enviada correctamente", Toast.LENGTH_SHORT).show();
            }

            );
            builder.setNegativeButton(android.R.string.no, null);
            builder.show();
        }
    }


}
