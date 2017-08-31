package com.anhanguera.gamecliente;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import br.com.diegosilva.grpc.hello.AutenticacaoGrpc;
import br.com.diegosilva.grpc.hello.AutenticacaoRequest;
import br.com.diegosilva.grpc.hello.AutenticacaoResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class MainActivity extends AppCompatActivity {

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(view -> {

            String login = ((EditText) findViewById(R.id.editText)).getText().toString();

            new Thread(() -> {
                ManagedChannel canal = ManagedChannelBuilder.forAddress("10.0.2.2", 50051)
                        .usePlaintext(true).build();
                AutenticacaoGrpc.AutenticacaoBlockingStub stub
                        = AutenticacaoGrpc.newBlockingStub(canal);

                AutenticacaoRequest request = AutenticacaoRequest.newBuilder()
                        .setUsuario(login).build();

                AutenticacaoResponse response =  stub.autenticar(request);

                if(response.getCodigo() < 0){
                    handler.post(()->{
                        Toast.makeText(getBaseContext(), response.getMessage(), Toast.LENGTH_LONG).show();
                    });
                }else{
                    handler.post(()->{
                        Toast.makeText(getBaseContext(), response.getMessage(), Toast.LENGTH_LONG).show();
                    });
                }

            }).start();
        });
    }
}
