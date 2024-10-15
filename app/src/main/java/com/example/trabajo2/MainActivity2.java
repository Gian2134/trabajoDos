package com.example.trabajo2;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    private ListView listproductos;
    private ArrayAdapter<String> adapter;
    private List<String> listaPedidos;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        listproductos = findViewById(R.id.listproductos);
        listaPedidos = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaPedidos);
        listproductos.setAdapter(adapter);

        // Inicializar Firebase Database
        database = FirebaseDatabase.getInstance().getReference("pedidos");
        listarDatos();
    }

    private void listarDatos() {
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaPedidos.clear(); // Limpiar la lista antes de agregar nuevos datos
                for (DataSnapshot pedidoSnapshot : snapshot.getChildren()) {
                    Pedido pedido = pedidoSnapshot.getValue(Pedido.class);
                    if (pedido != null) {
                        listaPedidos.add("Nombre: " + pedido.nombre + ", Producto: " + pedido.producto);
                    }
                }
                Log.d("Firebase", "Datos recibidos: " + listaPedidos); // Log de datos recibidos
                adapter.notifyDataSetChanged(); // Notificar al adaptador que los datos han cambiado
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error al obtener datos: " + error.getMessage());
            }
        });
    }
}
