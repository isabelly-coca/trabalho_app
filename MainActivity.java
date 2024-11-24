package com.example.montadorapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView textViewTempo;
    private Button buttonIniciar, buttonAnexarImagem;
    private ImageView imageViewMontagem;
    private CountDownTimer timer;
    private long tempoInicial = 0;  // Tempo em milissegundos
    private boolean montando = false;

    // Lista de montagens (simulação de dados)
    private ArrayList<String> montagens = new ArrayList<>();
    private RecyclerView recyclerViewMontagens;
    private MontagemAdapter montagemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewTempo = findViewById(R.id.textViewTempo);
        buttonIniciar = findViewById(R.id.buttonIniciar);
        buttonAnexarImagem = findViewById(R.id.buttonAnexarImagem);
        imageViewMontagem = findViewById(R.id.imageViewMontagem);
        recyclerViewMontagens = findViewById(R.id.recyclerViewMontagens);

        // Inicializar RecyclerView com uma lista de exemplo
        montagens.add("Montagem 1");
        montagens.add("Montagem 2");
        montagens.add("Montagem 3");
        montagemAdapter = new MontagemAdapter(montagens);
        recyclerViewMontagens.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMontagens.setAdapter(montagemAdapter);

        // Ao clicar no botão "Iniciar Montagem"
        buttonIniciar.setOnClickListener(v -> {
            if (!montando) {
                // Iniciar contagem de tempo
                montando = true;
                tempoInicial = 0;
                timer = new CountDownTimer(600000, 1000) { // 10 minutos
                    @Override
                    public void onTick(long millisUntilFinished) {
                        tempoInicial = millisUntilFinished;
                        int minutes = (int) (millisUntilFinished / 1000) / 60;
                        int seconds = (int) (millisUntilFinished / 1000) % 60;
                        textViewTempo.setText(String.format("%02d:%02d", minutes, seconds));
                    }

                    @Override
                    public void onFinish() {
                        textViewTempo.setText("00:00");
                        montando = false;
                    }
                }.start();
            }
        });

        // Ao clicar no botão "Anexar Imagem"
        buttonAnexarImagem.setOnClickListener(v -> {
            if (montando) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                imageCaptureResult.launch(cameraIntent);
            }
        });
    }

    // Registro da atividade para captura de imagem
    private final ActivityResultCallback<ActivityResult> imageCaptureResult = result -> {
        if (result.getResultCode() == RESULT_OK) {
            Bitmap imageBitmap = (Bitmap) result.getData().getExtras().get("data");
            imageViewMontagem.setImageBitmap(imageBitmap);
        }
    };
}
