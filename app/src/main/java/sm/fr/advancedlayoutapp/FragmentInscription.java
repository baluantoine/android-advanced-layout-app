package sm.fr.advancedlayoutapp;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentInscription extends Fragment {
    DrawerActivity parentActivity;
    EditText editTextInscription;


    public FragmentInscription() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inscription, container, false);

        //Récupération d'une référence au CHAP du formulare
        editTextInscription = view.findViewById(R.id.editTextInscription);

        //Récupération d'un référence de l'activité
        parentActivity = (DrawerActivity) getActivity();

        EditText userEditText = view.findViewById(R.id.editTextInscription);
        String userName = userEditText.getText().toString();

        //Gestion du clic sur le bouton valider
        Button btValid = view.findViewById(R.id.buttonInsription);
        btValid.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Récupération de la saisie de l'utilisateur
                String userName = editTextInscription.getText().toString();

                //Récupération de l'utilisateur et modification du nom de
                parentActivity.getUser().setUserName(userName);

                //Navigation vers le fragment B en passant par l'activité
                parentActivity.goToFragmentB();
            }
        });
        return view;
    }
}
