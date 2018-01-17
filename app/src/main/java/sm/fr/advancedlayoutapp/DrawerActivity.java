package sm.fr.advancedlayoutapp;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;
import java.util.List;
import sm.fr.advancedlayoutapp.model.User;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private User user;
    //Code pour référencer le login
    public final int LOGIN_REQUESTCODE = 1;

    private TextView userNameTextView;
    private TextView userEmailTextView;

    private NavigationView navigationView;
    private DrawerLayout drawer;

    private FirebaseUser fbUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Référence aux TextView dans l'en-tête de la navigation
        View headerView = ((NavigationView) navigationView.findViewById(R.id.nav_view)).getHeaderView(0);
        userEmailTextView = headerView.findViewById(R.id.headerUserEmail);
        userNameTextView = headerView.findViewById(R.id.headerUserName);

        //Instanciation d'un utilisateur
        this.user = new User();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            navigateToFragment(new FragmentB());
        }

        else if (id == R.id.nav_gallery) {
            navigateToFragment(new FragmentInscription());
        }

        else if (id == R.id.nav_slideshow) {
            navigateToFragment(new RandomUserFragment());

        } else if (id == R.id.nav_manage) {
            Intent mapIntention = new  Intent(this, MapsActivity.class);
            startActivity(mapIntention);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void navigateToFragment(Fragment targetFragment){
        getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, targetFragment).commit();
    }

    /**
     * @return
     */
    public User getUser(){
        return this.user;
    }

    public void goToFragmentB(){
        navigateToFragment(new FragmentB());
    }

    /**
     * Lancement de la procédure d'authentification
     * @param item
     */
    public void onLogin(MenuItem item) {
        //Définir une liste des fournisseur d'authentification
        List<AuthUI.IdpConfig> providers = new ArrayList<>();
                providers.add(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build());
        //Lancement de l'activité d'authentification
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build(), LOGIN_REQUESTCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == LOGIN_REQUESTCODE){
            //Récupération de la réponse
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if(requestCode == RESULT_OK){
                fbUser = FirebaseAuth.getInstance().getCurrentUser();

                //Afficher des infos utilisateur
                if(fbUser != null){
                    String userName = fbUser.getDisplayName();
                    String userEmail = fbUser.getEmail();
                    userNameTextView.setText(userName);
                    userEmailTextView.setText(userEmail);
                }

                //Masquage du lien login
                navigationView.getMenu().findItem(R.id.action_login).setVisible(false);
                //Affichage  du lien logout
                navigationView.getMenu().findItem(R.id.action_logout).setVisible(true);

            }else{
                if(response != null){
                    Log.d("Main", "Erreur Fireauth code: " + response.getErrorCode());
                }
                Toast.makeText(this, "Impossible de s'authentifier", Toast.LENGTH_LONG);
            }
        }
    }

    /**
     * Gestion de déconnexion login et logout
     * @param item
     */
    public void onLogout(MenuItem item){
        AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //Affichage du lien Login
                navigationView.getMenu().findItem(R.id.action_login).setVisible(true);
                //Affichage du lien Logout
                navigationView.getMenu().findItem(R.id.action_logout).setVisible(false);

                //Suppression des infos utilisateur dans l'en-tête
                userNameTextView.setText("");
                userEmailTextView.setText("");

                fbUser = null;

                //Fermeture du menu
                drawer.closeDrawer(GravityCompat.START);
            }
        });

    }
}
