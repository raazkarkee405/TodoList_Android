package com.example.todolistmvvm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.todolistmvvm.category.AddNewCategory;
import com.example.todolistmvvm.category.CategoryFragment;
import com.example.todolistmvvm.category.CategoryViewModel;
import com.example.todolistmvvm.category.EditCategory;
import com.example.todolistmvvm.category.EmptyCategoryFragment;
import com.example.todolistmvvm.database.AppDatabase;
import com.example.todolistmvvm.database.CategoriesEntity;
import com.example.todolistmvvm.tasks.AllTasksFragment;
import com.example.todolistmvvm.tasks.NoTasksFragment;
import com.google.android.material.navigation.NavigationView;


import java.util.List;

    public class MainActivity extends AppCompatActivity {

        private DrawerLayout drawerLayout;
        private Menu menu;
        private AppDatabase db;
        public static  int categoryID;
        private Fragment fragment;
        public static boolean showEditButton;
        private static final String TAG = MainActivity.class.getSimpleName();
        CategoryViewModel viewModel;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            //Set the toolbar as the action bar
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            //Add the nav drawer button
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

            //Get database
            db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "todolist").allowMainThreadQueries().build();

            openHomeCategory();

            // Get drawer_layout from layout file
            drawerLayout = findViewById(R.id.drawer_layout);

            // Handler navigation click event
            NavigationView navigationView = findViewById(R.id.nav_view);

            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    // set item as selected to persist highlight
                    menuItem.setChecked(true);
                    // close drawer when item is tapped
                    drawerLayout.closeDrawers();

                    // Add code here to update the UI based on the item selected
                    // For example, swap UI fragments here

                    categoryID = menuItem.getItemId();

                    switch (categoryID) {
                        case R.id.home:
                            openHomeCategory();
                            break;

                        case R.id.add_new:
                            Intent intent = new Intent(getApplicationContext(), AddNewCategory.class);
                            startActivity(intent);
                            break;

                        default:
                            setTitle(menuItem.toString());

                            if (db.TaskDAO().getNumberOfTasksInCategory(categoryID) == 0) {
                                fragment = new EmptyCategoryFragment();
                            } else {
                                fragment = new CategoryFragment();
                            }
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

                            // Show edit button on toolbar
                            showEditButton = true;
                            invalidateOptionsMenu();
                    }
                    return true;
                }
            });

            // Get menu of navigationView
            menu = navigationView.getMenu();

            // Disable tint of icons in navigation drawer
            navigationView.setItemIconTintList(null);
        }

        @Override
        protected void onStart() {
            super.onStart();

            // Reload toolbar when activity becomes visible to update icons
            invalidateOptionsMenu();

            // Get the category from database when activity starts
            CategoriesEntity categoriesEntity = db.CategoriesDAO().getCategory(categoryID);

            if (categoriesEntity != null) {
                // Category still exists, update name and load category fragment
                setTitle(categoriesEntity.getCategory());

                // Check if category is empty
                if (db.TaskDAO().getNumberOfTasksInCategory(categoryID) == 0){
                    fragment = new EmptyCategoryFragment();
                }else {
                    fragment = new CategoryFragment();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            } else {
                openHomeCategory();
            }
//            }
//        });

            // Clear current menu to avoid duplication
            menu.clear();

            // Append "Home" menu resource
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.drawer_home_view, menu);

            // Append "Add Category" menu resource
            inflater.inflate(R.menu.drawer_add_new_category_view, menu);

            // Get categories from database and append to menu
            viewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
          //  int ID = 0;
            viewModel.getCategory(categoryID);
            viewModel.getCategories().observe(this, new Observer<List<CategoriesEntity>>() {
                @Override
                public void onChanged(List<CategoriesEntity> categoriesEntities) {
                    Log.d(TAG, "Receiving database update categories from ViewModel");
//                categories.removeObserver(this);
                    for (int i = 0; i < categoriesEntities.size(); i++) {
                        CategoriesEntity c = categoriesEntities.get(i);
                        menu.add(1, c.getID(), Menu.NONE, c.getCategory());

                    }
                }
            });

        }

        // Open the drawer when the button is tapped


        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    drawerLayout.openDrawer(GravityCompat.START);
                    return true;

                case R.id.edit:
                    Intent intent = new Intent(getApplicationContext(), EditCategory.class);
                    intent.putExtra("categoryID", categoryID);
                    startActivity(intent);
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.categegory_toolbar_view, menu);

            // If edit button on toolbar should be shown
            if (!showEditButton) {
                menu.findItem(R.id.edit).setVisible(false);
            }

            return true;
        }

        // Helper function to open home category
        private void openHomeCategory() {
            setTitle("Home");
            // Check if there are any categories
            if (db.TaskDAO().getNumberOfTasks() == 0) {
                fragment = new NoTasksFragment();
            }else {
                fragment = new AllTasksFragment();
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

            // Hide edit button on toolbar
            showEditButton = false;
            invalidateOptionsMenu();
        }

}
