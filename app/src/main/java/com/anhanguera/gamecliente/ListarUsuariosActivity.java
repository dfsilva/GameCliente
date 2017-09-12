package com.anhanguera.gamecliente;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.diegosilva.grpc.hello.Usuario;


import br.com.diegosilva.grpc.hello.UsuariosGrpc;
import io.grpc.stub.StreamObserver;

public class ListarUsuariosActivity extends AppCompatActivity {

    private static String TAG = ListarUsuariosActivity.class.getName();

    private RecyclerView list;
    private UsuariosAdapter adapterUsuarios;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_usuarios);
        setTitle("Usuários Disponíveis - "+GameApp.gameApp().getUsuarioAutenticado());

        list = (RecyclerView) findViewById(R.id.list);
        adapterUsuarios = new UsuariosAdapter();
        list.setAdapter(adapterUsuarios);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        list.setLayoutManager(layout);

       listarUsuarios();
    }

    private void listarUsuarios(){
        StreamObserver<Usuario> responseObserver = new StreamObserver<Usuario>() {
            @Override
            public void onNext(final Usuario value) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapterUsuarios.adicionarUsuario(value);
                    }
                });
            }
            @Override
            public void onError(Throwable t) {
            }
            @Override
            public void onCompleted() {
            }
        };

        Usuario request = Usuario.newBuilder().setNome(GameApp.gameApp().getUsuarioAutenticado()).build();
        UsuariosGrpc.UsuariosStub stub = UsuariosGrpc.newStub(GameApp.gameApp().getChannel());

        stub.listarUsuarios(request, responseObserver);
    }


    class UsuariosAdapter extends RecyclerView.Adapter<UsuarioViewHolder>{

        private List<Usuario> usuarios = new ArrayList<>();

        @Override
        public UsuarioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.usuario_view_holder, parent, false);
            return new UsuarioViewHolder(view);
        }

        @Override
        public void onBindViewHolder(UsuarioViewHolder holder, int position) {
            Usuario usuario = usuarios.get(position);
            holder.txView.setText(usuario.getNome());
        }

        @Override
        public int getItemCount() {
            return usuarios.size();
        }

        public void adicionarUsuario(Usuario usuario){
            usuarios.add(usuario);
            notifyItemInserted(usuarios.size());
        }
    }

    class UsuarioViewHolder extends RecyclerView.ViewHolder{
        final TextView txView;
        public UsuarioViewHolder(View view){
            super(view);
            txView = (TextView) view.findViewById(R.id.txNome);
        }
    }
}
