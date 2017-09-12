package com.anhanguera.gamecliente;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import br.com.diegosilva.grpc.hello.AutenticacaoGrpc;
import br.com.diegosilva.grpc.hello.AutenticacaoRequest;
import br.com.diegosilva.grpc.hello.AutenticacaoResponse;

public class MainActivity extends AppCompatActivity {

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setTitle("Game App");

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String login = ((EditText) findViewById(R.id.editText)).getText().toString();

                new AsyncTask<Void, Void, AutenticacaoResponse>(){

                    //Executa o código pesado, asincrono
                    @Override
                    protected AutenticacaoResponse doInBackground(Void... voids) {


                        AutenticacaoGrpc.AutenticacaoBlockingStub stub
                                = AutenticacaoGrpc.newBlockingStub(GameApp.gameApp().getChannel());

                        AutenticacaoRequest request = AutenticacaoRequest.newBuilder()
                                .setUsuario(login).build();

                        return stub.autenticar(request);
                    }

                    //atualiza a interface
                    @Override
                    protected void onPostExecute(final AutenticacaoResponse response) {
                        if(response.getCodigo() < 0){
                            Snackbar.make(findViewById(android.R.id.content), response.getMessage(), Snackbar.LENGTH_LONG).show();
                        }else{
                            GameApp.gameApp().setUsuarioAutenticado(login);
                            startActivity(new Intent(MainActivity.this, ListarUsuariosActivity.class));
                        }
                    }
                }.execute();
            }
        });
    }
}
