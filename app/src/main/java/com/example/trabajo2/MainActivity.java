package com.example.trabajo2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText textnombre, textproducto;
    private Button buttonIng, ButtonS;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Referencias de los campos y botones
        textnombre = findViewById(R.id.textnombre);
        textproducto = findViewById(R.id.textproducto);
        buttonIng = findViewById(R.id.buttonIng);
        ButtonS = findViewById(R.id.ButtonS);

        // Inicializar Firebase Database
        database = FirebaseDatabase.getInstance().getReference("pedidos");

        // Botón para ingresar los datos en Firebase
        buttonIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = textnombre.getText().toString().trim();
                String producto = textproducto.getText().toString().trim();

                if (!nombre.isEmpty() && !producto.isEmpty()) {
                    enviarDatosAFirebase(nombre, producto);
                } else {
                    Toast.makeText(MainActivity.this, "Por favor, completa ambos campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Botón para ir a la segunda actividad
        ButtonS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });
    }

    private void enviarDatosAFirebase(String nombre, String producto) {
        String id = database.push().getKey(); // Generar un ID único para cada pedido
        Pedido pedido = new Pedido(nombre, producto);

        Log.d("Firebase", "Guardando pedido: " + nombre + ", " + producto); // Agregar Log

        database.child(id).setValue(pedido).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(MainActivity.this, "Pedido guardado exitosamente", Toast.LENGTH_SHORT).show();
                textnombre.setText(""); // Limpiar los campos después de guardar
                textproducto.setText("");
            } else {
                Log.e("Firebase", "Error al guardar el pedido: " + task.getException());
                Toast.makeText(MainActivity.this, "Error al guardar el pedido", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

// Clase del modelo para los pedidos
class Pedido {
    public String nombre;
    public String producto;

    public Pedido() { }  // Constructor vacío requerido por Firebase

    public Pedido(String nombre, String producto) {
        this.nombre = nombre;
        this.producto = producto;
    }
}
