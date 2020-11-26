package controllers;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.ristodroid.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * In questa classe ci occupiamo di scambiare i vari fragment
 */
public class Dashboard extends AppCompatActivity  {

    private NavHostFragment navHostFragment;
    private BottomNavigationView navMenu;
    private NavController navController;

    @Override
    public boolean onSupportNavigateUp() {
        return Navigation.findNavController(this, navHostFragment.getView().getId()).navigateUp()
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_menu, R.id.navigation_summary, R.id.navigation_receipt)
                .build();

        navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        navMenu= this.findViewById(R.id.nav_view);
        Utility.setSummaryBadge(navMenu);
    }

}