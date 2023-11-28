package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    // Setup Server information
    protected static String server = "10.0.2.2";
    //
    protected static int port = 7070;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Capturamos el boton de Enviar
        View button = findViewById(R.id.button_send);

        // Llama al listener del boton Enviar
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });


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

        Integer camas = Integer.valueOf(txcamas);
        Integer mesas = Integer.valueOf(txmesas);
        Integer sillas = Integer.valueOf(txsillas);
        Integer sillones = Integer.valueOf(txsillones);
        Integer idEmpleado = Integer.valueOf(txidEmpleado);

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
            new AlertDialog.Builder(this)
                    .setTitle("Enviar")
                    .setMessage("Se va a proceder al envio")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                // Catch ok button and send information
                                public void onClick(DialogInterface dialog, int whichButton) {

                                    // 1. Extraer los datos de la vista
                                    String mensaje = camas.toString() +"/"+ mesas.toString() +"/"+ sillas.toString() +"/"+ sillones.toString() +"/"+idEmpleado.toString()+"/";
                                    //     mensaje : c###/m###/s###/s###/id#####/

                                    // 2. Firmar los datos

                                    // 3. Enviar los datos

                                    Toast.makeText(MainActivity.this, "Petición enviada correctamente", Toast.LENGTH_SHORT).show();
                                }
                            }

                    )
                            .

                    setNegativeButton(android.R.string.no, null)

                            .

                    show();
        }
    }


}
