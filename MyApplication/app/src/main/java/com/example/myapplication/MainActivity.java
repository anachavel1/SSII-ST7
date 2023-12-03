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
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import android.util.Base64;


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
                        String mensaje = camas + "|" + mesas + "|" + sillas + "|" + sillones + "|" + idEmpleado;
                        //     mensaje : c###/m###/s###/s###/id#####/


                        // 2. Firmar los datos
                        String privateKeyPEM = "-----BEGIN RSA PRIVATE KEY-----\n" +
                                "MIIEpQIBAAKCAQEAxZV1ATT0gdajScIBsqzgbUvitYtFatcmWE0ABgbB8DFvPRJj\n" +
                                "7eDpFn0g43x6ZKa33lSpgqBGCuk0U/tbsOKxvile8z8nY8t4Aa3d9Fs5xoU10aVO\n" +
                                "QenP4D6IMV3svNnbcB3pXoAuDdZtppYZiRHeGvISnSi8ZXBnQrJPaIjyjOTO1JvI\n" +
                                "SEs2qIJ+EXY98Ba9ewOigfZCvdLUIaxQ8fkFPljOidlcC5jpzsxdh8AyriHu6+5S\n" +
                                "4u5ceY43iWY6XYhcVY7IZ7BypfqjiawM7IXIrTL6C9qQfWdm7r213KH83cR/DkiS\n" +
                                "220ka58MVfnJbXtbcml7wwPK4N64fOLmwFWCtQIDAQABAoIBAALGDlZt/+CyuqgW\n" +
                                "aR+w/ZWLMyuJQPg2/PrQWee7wI02pHEfCPgV6C/Aoe2Pmcg/7uTYG2kSBumwTGPB\n" +
                                "a5AYHpakzghsIolsGcssPalofm8i/5Zp06dpONnVBuRqhSBBjqsMQHJrKjpXgEKO\n" +
                                "aN1butp1+3zpTXu/EDQHfMXi5kTECypbKA63rv3wDxMywqszqny2MaWfD4VoZTPe\n" +
                                "Md88AYzfsmIVzMOrpBknO/2dZta7GQ1g6RaIT+lLsYrvDksP9XZIGbTxKXWHW5Zv\n" +
                                "/32tMfN8apHVhseyLEg6gm5pFbtTj9s4BQVapJuUiIdb2qStdfVnTdyItWULPp3q\n" +
                                "GxTA9gECgYEA+Qm0uADAhnvxUY0nk8xNmsvDeGYy098nwpNcPGulPdiB30lTfZX1\n" +
                                "72IENBl25xwcX26jylBhoSN/U5AZHdK63luAuOWcy5VUB3kDB3zZU2nowFk3i6zy\n" +
                                "AJE99Psgdu4OeerLyw1zuCWsC3K2Pb9AQj4BaCwouoLKgLreT3xOOGUCgYEAyxuC\n" +
                                "N9PbdvUt1MsIBfn0Ev8zQ8KF5dXJgeZpMi3fzdTMAsp9SOcN5Q5b4NjAQ74BKy7U\n" +
                                "+kIFX56EFVRne6yuLlPG1DoLMXJfEG9iXOaUEyAgeIPcueZ0SvvjL+3iUYJF1IEE\n" +
                                "gUz0bMSEyCcNj/wdbP+9/EKpjadcqPc+MS7edBECgYEAjdCBIOcMu4iI+y+ugfOt\n" +
                                "naC7RyyrdQt20M6pj56dEoLgMg7HhJSI4DCoxCJcG29emNmgW+06K1DTiPpd0yXV\n" +
                                "VBo3SxM2HpiCqV634uOtvlppOF+VyexKQxhyd7cp3Y/innqeYprectbBCiPgs3jd\n" +
                                "VtuIYZID/3HLb1L6lbjrsiECgYEAwi6Vq8xYGX5FCBnonNYhPPxSRek3XMqtcg0Y\n" +
                                "7Amh3EcjmVOAvm9xAFpfCzQPdXS151RJ+M2gF5AU1dOxcDNjABXGlWa9BtRDARKM\n" +
                                "pycn0LU5dh8Tq4QiEQKLbWpwot765jVHWlt5oHeuPzHfLJash2ZmfEQ7mJu24jAn\n" +
                                "fJxDwBECgYEA1ejOjkRgopIIC/1ZGBsvGHirbpJB3SriKVzECvkfuONuBZKZQ+ne\n" +
                                "8g++eC7+jePufArEh63wopxK1RXBZmc/GTWYU62ILFX7AREoe629InrBYJSfwOPz\n" +
                                "7V3nUv0JlByLfhxgrlmAh/f7OergyFSMqOc4LNajSO+8DrUl08IRkjs=\n" +
                                "-----END RSA PRIVATE KEY-----\n";

                        // Eliminar los encabezados y retornos de carro
                        privateKeyPEM = privateKeyPEM.replace("-----BEGIN RSA PRIVATE KEY-----", "");
                        privateKeyPEM = privateKeyPEM.replace("-----END RSA PRIVATE KEY-----", "");
                        privateKeyPEM = privateKeyPEM.replaceAll("\\s", "");
                        privateKeyPEM = privateKeyPEM.replaceAll(" ", "");


                        // Decodificar la clave pública desde Base64
                        byte[] privateKeyBytes = new byte[0];
                        privateKeyBytes = Base64.decode(privateKeyPEM, Base64.DEFAULT);


                        byte[] firma = new byte[0];
                        try {
                            // Crear la especificación de la clave privada
                            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(privateKeyBytes);

                            // Obtener una instancia de KeyFactory para RSA
                            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

                            // Generar la clave privada
                            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
                            // Crear una instancia de Signature con el algoritmo de firma
                            Signature sg = Signature.getInstance("SHA256withRSA");

                            // Inicializar la instancia con la clave privada
                            sg.initSign(privateKey);


                            // Actualizar la firma con los bytes del mensaje
                            sg.update(mensaje.getBytes());

                            // Firma
                            firma = sg.sign();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // 3. Enviar los datos

                        byte[] finalFirma = firma;

                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    // Conectar al servidor
                                    Socket socket = new Socket(server, port);

                                    // Enviar datos al servidor
                                    String mensajecompleto = mensaje + "||" + finalFirma;
                                    Log.d("Atenchon","El mensaje es: " + mensaje);
                                    Log.d("Atenchon","La firma es: " + finalFirma);

                                    OutputStream outputStream = socket.getOutputStream();
                                    outputStream.write(mensajecompleto.getBytes());

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
