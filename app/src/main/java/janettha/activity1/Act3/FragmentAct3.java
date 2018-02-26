package janettha.activity1.Act3;

import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import janettha.activity1.Models.Emocion;
import janettha.activity1.Models.Emociones;
import janettha.activity1.R;


public class FragmentAct3 extends Fragment {
    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";

    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int mPageNumber;

    private String texto;

    TextToSpeech t1;

    public static FragmentAct3 create(int pageNumber, Context context, int id) {
        List<Emocion> emociones = new ArrayList<Emocion>();
        Emociones em = new Emociones();
        FragmentAct3 fragment = new FragmentAct3();
        emociones = em.Emociones(context, "f");
        Bundle args = new Bundle();

        args.putInt(ARG_PAGE, pageNumber);
        args.putString(Activity3.ARG_tx,emociones.get(id).getName());
        //args.putString(Activity3.ARG_tx,"Holo mundo");

        fragment.setArguments(args);
        return fragment;
    }

    public FragmentAct3() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            mPageNumber = getArguments().getInt(ARG_PAGE);
            texto = getArguments().getString(Activity3.ARG_tx);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_fragment_act3, container, false);

        // Set the title view to show the page number.
        ((TextView) rootView.findViewById(R.id.text1)).setText(
                getString(R.string.title_template_step, mPageNumber + 1));
        ((TextView) rootView.findViewById(R.id.txtText)).setText(texto);
        Button btn = (Button) rootView.findViewById(R.id.btnSpeak);

        t1=new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.getDefault());
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = texto;
                Toast.makeText(getContext(), toSpeak,Toast.LENGTH_SHORT).show();
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        return rootView;
    }

    public void onPause(){
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }
}
