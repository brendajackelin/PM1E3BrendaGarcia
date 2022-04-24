package com.example.pm1e3brendagarcia.controlador;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pm1e3brendagarcia.R;
import com.example.pm1e3brendagarcia.modelo.Medicamento;

import java.util.ArrayList;
import java.util.List;

public class MedicamentoAdapter extends RecyclerView.Adapter<MedicamentoAdapter.medicamentoView> {

    private List<Medicamento> medicamentoList = new ArrayList<>();
    private Context context;
    private ArrayList<Medicamento>medicamentoArrayList;
    private AuxiliarMedicamento auxiliarMedicamento;

    public MedicamentoAdapter(AuxiliarMedicamento auxiliarMedicamento,ArrayList<Medicamento> medicamentoList){
        this.medicamentoList = medicamentoList;
        this.auxiliarMedicamento = auxiliarMedicamento;
        medicamentoArrayList=medicamentoList;
    }

    @NonNull
    @Override
    public medicamentoView onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_mostrar,viewGroup,false);
        return new medicamentoView(view);

    }

    @Override
    public void onBindViewHolder(medicamentoView medicamentoView, int posicion) {
        Medicamento model=medicamentoArrayList.get(posicion);
        byte[]image=model.getImagen();
        Bitmap bitmap= BitmapFactory.decodeByteArray(image,0,image.length);

        Medicamento medicamento = medicamentoList.get(posicion);
        medicamentoView.txtDescripcionMostrar.setText(medicamento.getDescripcion());
        medicamentoView.txtCantidadMostrar.setText(String.valueOf(medicamento.getCantidad()));
        medicamentoView.txtHoraMostrar.setText(medicamento.getHoras());
        medicamentoView.txtPeriodoMostrar.setText(String.valueOf(medicamento.getPeriocidad()));
        medicamentoView.img.setImageBitmap(bitmap);
        medicamentoView.btnModificar.setOnClickListener(new eventoEditar(medicamento));
    }

    @Override
    public int getItemCount() {
        return medicamentoList.size();
    }

    public void agregarMedicamento(Medicamento medicamento){
        medicamentoList.add(medicamento);
        this.notifyDataSetChanged();
    }

    class eventoEditar implements View.OnClickListener{
        private Medicamento medicamento;
        public eventoEditar(Medicamento medicamento){
            this.medicamento=medicamento;
        }

        @Override
        public void onClick(View v){
            auxiliarMedicamento.OpcionEditar(medicamento);
        }
    }

    public class medicamentoView extends RecyclerView.ViewHolder{
            private TextView txtDescripcionMostrar,txtCantidadMostrar,txtHoraMostrar,txtPeriodoMostrar;
            private ImageView img;
            private Button btnModificar, btnEliminar;

            public medicamentoView(@NonNull View itemView){
                super(itemView);

                img = itemView.findViewById(R.id.img);
                txtDescripcionMostrar = itemView.findViewById(R.id.txtDescripcionMostrar);
                txtCantidadMostrar = itemView.findViewById(R.id.txtCantidadMostrar);
                txtHoraMostrar = itemView.findViewById(R.id.txtHoraMostrar);
                txtPeriodoMostrar = itemView.findViewById(R.id.txtPeriodoMostrar);
                btnModificar = itemView.findViewById(R.id.btnModificar);
            }
    }
}
