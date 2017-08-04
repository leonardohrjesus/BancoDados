package com.example.amministratore.bancodados;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {



    EditText editCodigo,editNome,editTelefone,editEmail;
    Button btnLimpar,btnSalvar,btnExcluir;
    ListView listViewClientes;

    ArrayAdapter<String> adpter;
    ArrayList<String> arraylist;

    BancoDados db = new BancoDados(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editCodigo = (EditText) findViewById(R.id.editCodigo);
        editNome = (EditText) findViewById(R.id.editNome);
        editTelefone = (EditText) findViewById(R.id.editTelefone);
        editEmail = (EditText) findViewById(R.id.editEmail);

        btnLimpar = (Button) findViewById(R.id.btnLimpar);
        btnSalvar = (Button) findViewById(R.id.btnSalvar);
        btnExcluir = (Button) findViewById(R.id.btnExcluir);

        listViewClientes = (ListView) findViewById(R.id.ListViewClientes);


        listarClientes();
        //LIMPAR CAMPOS
        btnLimpar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                limparcampos();
            }
        });

        //SALVAR  OU INSERINDO OU ATUALIZANDO
        btnSalvar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                String codigo = editCodigo.getText().toString();
                String nome   = editNome.getText().toString();
                String telefone = editTelefone.getText().toString();
                String email = editEmail.getText().toString();

                if (nome.isEmpty()){
                    editNome.setError("Este campo è obrigatorio");

                } else if (codigo.isEmpty()){
                    //insert
                    db.addCliente(new Cliente(nome,telefone,email));

                    Toast.makeText(MainActivity.this,"Cliente inserido com sucesso", Toast.LENGTH_LONG).show();
                    limparcampos();
                    listarClientes();
                }else{
                    //update
                    db.atualizarCliente(new Cliente(Integer.parseInt(codigo),nome,telefone,email));

                    Toast.makeText(MainActivity.this,"Cliente Atualizado com sucesso", Toast.LENGTH_LONG).show();
                    limparcampos();
                    listarClientes();
                }
            }
        });

        //EXCLUIR CONTATOS

        btnExcluir.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String codigo = editCodigo.getText().toString();

                if(codigo.isEmpty()){
                    Toast.makeText(MainActivity.this," Nenhum Cliente esta selecionado", Toast.LENGTH_LONG).show();
                }else{
                    Cliente cliente = new Cliente();
                    cliente.setCodigo(Integer.parseInt(codigo));
                    db.apagarCliente(cliente);
                    limparcampos();
                    listarClientes();

                }
            }

        });

        //LISTANDO CONTATOS
        listViewClientes.setOnItemClickListener(new AdapterView.OnItemClickListener(){


            //metodo selecionar  item da list View
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String conteudo = (String) listViewClientes.getItemAtPosition(position);

                String codigo  = conteudo.substring(0,conteudo.indexOf("-"));

                Cliente cliente  = db.selecionarCliente(Integer.parseInt(codigo));

                editCodigo.setText("" + cliente.getCodigo());
                editNome.setText(cliente.getNome());
                editTelefone.setText(cliente.getTelefone());
                editEmail.setText(cliente.getEmail());
            }
        });



        //insert na unha
        //     db.addCliente(new Cliente("Leonardo Rei do android","11995373877","leonardo.hrjesus@hotmail.com"));


        //deletar na unha
        /*Cliente cliente = new Cliente();
        cliente.setCodigo(1);
        db.apagarCliente(cliente);*/


        //selecionar na unha
        //     Cliente cliente  =   db.selecionarCliente(1);


        //atualizar na unha
    /*    Cliente cliente = new Cliente();
        cliente.setCodigo(1);
        cliente.setNome("leonardo");
        cliente.setEmail("leonardo.hrjesus@gmail.com");
        cliente.setTelefone("1146841160");

        db.atualizarCliente(cliente);*/

    }
    public void listarClientes(){

        List<Cliente> clientes = db.listatodososcontatos();
        arraylist = new ArrayList<String>();

        adpter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1, arraylist);

        listViewClientes.setAdapter(adpter);

        for(Cliente c : clientes){
            arraylist.add(c.getCodigo()+ "-" + c.getNome());
            adpter.notifyDataSetChanged();

        }

    }



    void limparcampos(){
        editCodigo.setText("");
        editNome.setText("");
        editTelefone.setText("");
        editEmail.setText("");
        editNome.requestFocus();
    }



}
