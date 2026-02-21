package com.example.hamburgueriaz;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    // Variáveis para armazenar os dados do pedido
    private int quantidade = 0;
    private TextView quantidadeTV;
    private EditText nomeClienteET;
    private CheckBox baconCB, queijoCB, onionRingsCB;
    private TextView precoTotalTV;
    private TextView resumoPedidoTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializando os componentes da interface
        quantidadeTV = findViewById(R.id.quantidade);
        Button adicionarButton = findViewById(R.id.aumentar);
        Button subtrairButton = findViewById(R.id.reduzir);
        nomeClienteET = findViewById(R.id.nome);
        baconCB = findViewById(R.id.checkbox_B);
        queijoCB = findViewById(R.id.checkbox_Q);
        onionRingsCB = findViewById(R.id.checkbox_O);
        precoTotalTV = findViewById(R.id.valorTotal);
        resumoPedidoTV = findViewById(R.id.resumo);
        Button enviarPedidoButton = findViewById(R.id.fazerPedido);
        // Configurando eventos de clique e atualizando a interface
        configurarEventosCheckBox();
        atualizarQuantidadeTextView();
        atualizarPrecoTotal();

        adicionarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aumentar();
            }
        });

        subtrairButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reduzir();
            }
        });

        enviarPedidoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fazerPedido();
            }
        });
    }

    private void aumentar() {
        quantidade++;
        atualizarQuantidadeTextView();
        atualizarPrecoTotal();
    }

    private void reduzir() {
        if (quantidade > 0) {
            quantidade--;
            atualizarQuantidadeTextView();
            atualizarPrecoTotal();
        }
    }

    private void atualizarQuantidadeTextView() {
        quantidadeTV.setText(String.valueOf(quantidade));
    }

    private void configurarEventosCheckBox() {
        baconCB.setOnCheckedChangeListener((buttonView, isChecked) -> atualizarPrecoTotal());
        queijoCB.setOnCheckedChangeListener((buttonView, isChecked) -> atualizarPrecoTotal());
        onionRingsCB.setOnCheckedChangeListener((buttonView, isChecked) -> atualizarPrecoTotal());
    }

    private void atualizarPrecoTotal() {
        boolean temBacon = baconCB.isChecked();
        boolean temQueijo = queijoCB.isChecked();
        boolean temOnionRings = onionRingsCB.isChecked();

        double precoTotal = Total(temBacon, temQueijo, temOnionRings);
        precoTotalTV.setText("Preço Total: R$ " + String.format("%.2f", precoTotal));
    }

    private double Total(boolean temBacon, boolean temQueijo, boolean temOnionRings) {
        double precoBase = 20.0;
        double precoBacon = temBacon ? 2.00 : 0.0; // marcado : desmarcado
        double precoQueijo = temQueijo ? 2.00 : 0.0;
        double precoOnionRings = temOnionRings ? 3.00 : 0.0;

        return (precoBase + precoBacon + precoQueijo + precoOnionRings) * quantidade;
    }

    private void fazerPedido() {
        String nomeCliente = nomeClienteET.getText().toString();
        boolean temBacon = baconCB.isChecked();
        boolean temQueijo = queijoCB.isChecked();
        boolean temOnionRings = onionRingsCB.isChecked();

        double VTotal = Total(temBacon, temQueijo, temOnionRings);

        String resumo =
                "Resumo do Pedido:\n" +
                "Nome do cliente: " + nomeCliente + "\n" +
                "Tem Bacon? " + (temBacon ? "Sim" : "Não") + "\n" +
                "Tem Queijo? " + (temQueijo ? "Sim" : "Não") + "\n" +
                "Tem Onion Rings? " + (temOnionRings ? "Sim" : "Não") + "\n" +
                "Quantidade: " + quantidade + "\n" +
                "Preço Final: R$ " + String.format("%.2f", VTotal);

        resumoPedidoTV.setText(resumo);
        precoTotalTV.setText("Preço Final: R$ " + String.format("%.2f", VTotal));

        Intent intent = new Intent(Intent.ACTION_SENDTO); //intent para enviar email
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Pedido de " + nomeCliente); //Envia o nome do cliente como assunto
        intent.putExtra(Intent.EXTRA_TEXT, resumo); //Envia o rasumo do pedido
        startActivity(intent); //Esta linha inicia o intent
    }
}
