
package com.isaque.hamburgueriaz;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private int quantidade = 0;
    private static final double PRECO_BASE = 20.0;
    private static final double PRECO_BACON = 2.0;
    private static final double PRECO_QUEIJO = 2.0;
    private static final double PRECO_ONION_RINGS = 3.0;
    private static final Locale LOCALE_BR = new Locale("pt", "BR");

    private EditText nomeCliente;
    private CheckBox chkBacon;
    private CheckBox chkQueijo;
    private CheckBox chkOnionRings;
    private TextView txtQuantidade;
    private TextView txtPrecoTotal;
    private TextView txtResumoPedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nomeCliente = findViewById(R.id.edit_text_nome);
        chkBacon = findViewById(R.id.checkbox_bacon);
        chkQueijo = findViewById(R.id.checkbox_queijo);
        chkOnionRings = findViewById(R.id.checkbox_onion_rings);
        txtQuantidade = findViewById(R.id.text_view_quantidade);
        txtPrecoTotal = findViewById(R.id.text_view_preco_total);
        txtResumoPedido = findViewById(R.id.text_view_resumo_pedido);

        Button btnSomar = findViewById(R.id.button_somar);
        Button btnSubtrair = findViewById(R.id.button_subtrair);
        Button btnEnviarPedido = findViewById(R.id.button_enviar_pedido);

        btnSomar.setOnClickListener(v -> somar());
        btnSubtrair.setOnClickListener(v -> subtrair());
        btnEnviarPedido.setOnClickListener(v -> enviarPedido());
        chkBacon.setOnCheckedChangeListener((buttonView, isChecked) -> atualizarPrecoTotal());
        chkQueijo.setOnCheckedChangeListener((buttonView, isChecked) -> atualizarPrecoTotal());
        chkOnionRings.setOnCheckedChangeListener((buttonView, isChecked) -> atualizarPrecoTotal());

        atualizarQuantidade();
        atualizarPrecoTotal();
    }

    public void somar() {
        quantidade++;
        atualizarQuantidade();
    }

    public void subtrair() {
        if (quantidade > 0) {
            quantidade--;
            atualizarQuantidade();
        }
    }

    private void atualizarQuantidade() {
        txtQuantidade.setText(String.valueOf(quantidade));
        atualizarPrecoTotal();
    }

    private void atualizarPrecoTotal() {
        double precoFinal = calcularPrecoFinal(
                chkBacon.isChecked(),
                chkQueijo.isChecked(),
                chkOnionRings.isChecked()
        );
        txtPrecoTotal.setText(String.format(LOCALE_BR, "R$ %.2f", precoFinal));
    }

    public void enviarPedido() {
        String nome = nomeCliente.getText().toString().trim();
        boolean temBacon = chkBacon.isChecked();
        boolean temQueijo = chkQueijo.isChecked();
        boolean temOnionRings = chkOnionRings.isChecked();

        double precoFinal = calcularPrecoFinal(temBacon, temQueijo, temOnionRings);
        String precoFinalTexto = String.format(LOCALE_BR, "R$ %.2f", precoFinal);

        String resumo = "Nome do cliente: " + nome + "\n"
                + "Tem Bacon? " + (temBacon ? "Sim" : "Nao") + "\n"
                + "Tem Queijo? " + (temQueijo ? "Sim" : "Nao") + "\n"
                + "Tem Onion Rings? " + (temOnionRings ? "Sim" : "Nao") + "\n"
                + "Quantidade: " + quantidade + "\n"
                + "Preco final: " + precoFinalTexto;

        txtResumoPedido.setText(resumo);
        txtPrecoTotal.setText(precoFinalTexto);
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Pedido de " + nome);
        intent.putExtra(Intent.EXTRA_TEXT, resumo);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private double calcularPrecoFinal(boolean temBacon, boolean temQueijo, boolean temOnionRings) {
        double precoAdicionais = 0;
        if (temBacon) {
            precoAdicionais += PRECO_BACON;
        }
        if (temQueijo) {
            precoAdicionais += PRECO_QUEIJO;
        }
        if (temOnionRings) {
            precoAdicionais += PRECO_ONION_RINGS;
        }
        return (PRECO_BASE + precoAdicionais) * quantidade;
    }
}
