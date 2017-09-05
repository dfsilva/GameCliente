package com.anhanguera.gamecliente;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String login = ((EditText) findViewById(R.id.editText)).getText().toString();

                //Executa um código em background
                new AsyncTask<Void, Void, AutenticacaoResponse>(){
                    @Override
                    protected AutenticacaoResponse doInBackground(Void... voids) {

                        ManagedChannel canal = ManagedChannelBuilder.forAddress("10.0.2.2", 50051)
                                .usePlaintext(true).build();
                        AutenticacaoGrpc.AutenticacaoBlockingStub stub
                                = AutenticacaoGrpc.newBlockingStub(canal);

                        AutenticacaoRequest request = AutenticacaoRequest.newBuilder()
                                .setUsuario(login).build();

                        return stub.autenticar(request);
                    }

                    @Override
                    protected void onPostExecute(final AutenticacaoResponse response) {
                        if(response.getCodigo() < 0){
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getBaseContext(), response.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }else{
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getBaseContext(), response.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }.execute();
            }
        });
    }
}
