package janettha.activity1.Act2.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import github.hellocsl.cursorwheel.CursorWheelLayout;
import janettha.activity1.Models.Emocion;
import janettha.activity1.Models.Emociones;
import janettha.activity1.R;

/**
 * Created by janeth on 2018-02-19.
 */

public class WheelmageAdapter extends CursorWheelLayout.CycleWheelAdapter {

    private Context context;
    private List<Emocion> listEmociones = new ArrayList<Emocion>();
    private LayoutInflater inflater;
    private int gravity;

    public WheelmageAdapter(Context context, View view, String s) {
        Emociones e = new Emociones();
        this.context = context;
        this.listEmociones = e.Emociones(context, s);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 12;
    }

    @Override
    public View getView(View parent, int position) {
        View root = inflater.inflate(R.layout.wheel_image_layout, null, false);
        ImageView imageView = (ImageView) root.findViewById(R.id.wheel_menu_image);
        Uri ruta;
        //String pd = Uri.parse(data.getUrl()).getPath();
        //imageView.setImageURI(Uri.parse(data.getUrl()));
        //imageView.setImageResource(pd);
        ruta = Uri.parse(listEmociones.get(position).getUrl());
        Picasso.with(parent.getContext())
                .load(ruta)
                .resize(90, 90)
                .centerCrop()
                .into(imageView);
        return root;
    }

    @Override
    public Emocion getItem(int position) {
        return listEmociones.get(position);
    }
}
