package com.example.mohamed.nabdpharmacy.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mohamed.nabdpharmacy.retrofit.ApiClient;
import com.example.mohamed.nabdpharmacy.retrofit.ApiInterface;
import com.example.mohamed.nabdpharmacy.dialog.MyDialogs;
import com.example.mohamed.nabdpharmacy.R;
import com.example.mohamed.nabdpharmacy.adapter.RecyclerAdapter;
import com.example.mohamed.nabdpharmacy.model.Product;
import com.example.mohamed.nabdpharmacy.utilities.ConnectionUtils;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecyclerAdapter.ItemClickCallBack,SearchView.OnQueryTextListener{

    FastScrollRecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    RecyclerView.LayoutManager layoutManager;
    List<Product> productList;
    ApiInterface apiInterface;
    Toolbar toolbar;
    SwipeRefreshLayout refreshLayout;
    MenuItem alphabeticSortMenuItem,expirationDateSortMenuItem,defaultSortMenuItem;

    NavigationView navigationView;
    // Within which the entire activity is enclosed
     DrawerLayout mDrawerLayout;
    // ActionBarDrawerToggle indicates the presence of Navigation Drawer in the action bar
     ActionBarDrawerToggle mDrawerToggle;

    boolean isNetworkAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.bar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        recyclerView = findViewById(R.id.recycler_view);
        refreshLayout = findViewById(R.id.swipe_refresh);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        layoutManager =new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

       MyDialogs.showProgressDialog(MainActivity.this);

        loadData();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//
                loadData();
                refreshLayout.setRefreshing(false);
                defaultSortMenuItem.setChecked(true);
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,toolbar,
                R.string.open_drawer,
                R.string.close_drawer);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.action_add:

                        startActivity(new Intent(MainActivity.this,AddMedicineActivity.class));
                        mDrawerLayout.closeDrawers();
                        break;

                    case R.id.action_import_medicine_list:
                        Toast.makeText(MainActivity.this, "Coming Soon.!", Toast.LENGTH_SHORT).show();
                        break;
                }

                return true;
            }
        });
    }

    @Override
    public void onItemClick(int p) {
        Product product =recyclerAdapter.getItem(p);
        Intent intent =new Intent(MainActivity.this,DetailsActivity.class);
        intent.putExtra("product_object",product);
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(final int p) {

        AlertDialog.Builder builder =new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Delete");
        builder.setMessage("Delete "+productList.get(p).getName()+" ?");
        builder.setCancelable(true);

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                final Product product = productList.get(p);
                ApiInterface apiInterface = ApiClient.getApiClient(MainActivity.this).create(ApiInterface.class);
                Call<Product> delCall =apiInterface.deleteData(product.getName());
                delCall.enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {
                        Toast.makeText(MainActivity.this, "Deleted.!", Toast.LENGTH_SHORT).show();
                        productList.remove(p);
                        recyclerAdapter.notifyChange(p);
                    }

                    @Override
                    public void onFailure(@NonNull Call<Product> call, @NonNull Throwable t) {
                        // Toast.makeText(MainActivity.this,"Something Error.!", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog dialog =builder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);

        alphabeticSortMenuItem =menu.findItem(R.id.action_sort_by_alphabetic);
        expirationDateSortMenuItem =menu.findItem(R.id.action_sort_by_expiration_date);
        defaultSortMenuItem =menu.findItem(R.id.action_sort_by_default);
        defaultSortMenuItem.setChecked(true);

        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        searchView.setOnQueryTextListener(MainActivity.this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_sort:
                break;

            case R.id.action_sort_by_alphabetic:
                isNetworkAvailable =ConnectionUtils.isConnected(this);
                if (!item.isChecked()) {
                        alphabeticSortMenuItem.setChecked(true);
                    if (isNetworkAvailable) {
                        recyclerAdapter.alphabeticListSorting(productList);
                    }
                    else {
                        Toast.makeText(this, "No Internet Connection.!", Toast.LENGTH_SHORT).show();
                    }
                    }
                break;

            case R.id.action_sort_by_expiration_date:
                isNetworkAvailable =ConnectionUtils.isConnected(this);
                    if (!item.isChecked()) {
                        expirationDateSortMenuItem.setChecked(true);

                        if (isNetworkAvailable){
                            recyclerAdapter.dateSorting(productList);
                        }
                        else {
                            Toast.makeText(this, "No Internet Connection.!", Toast.LENGTH_SHORT).show();
                        }

                    }
                    break;

            case R.id.action_sort_by_default:
                    if (!item.isChecked()) {
                        defaultSortMenuItem.setChecked(true);
                            loadData();
                    }
                break;
            case R.id.action_reload:
                loadData();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        List<Product> list =new ArrayList<>();
        for (Product p :productList){
            String name =p.getName().toLowerCase();
            if (name.contains(newText)){
                list.add(p);
            }
        }
        recyclerAdapter.setFilter(list);
        return true;
    }

    private void loadData() {

        this.invalidateOptionsMenu();
        isNetworkAvailable = ConnectionUtils.isConnected(MainActivity.this);

        if (isNetworkAvailable) {
            apiInterface = ApiClient.getApiClient(MainActivity.this).create(ApiInterface.class);

            Call<List<Product>> call = apiInterface.getProductsInfo();

            call.enqueue(new Callback<List<Product>>() {
                @Override
                public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                    productList = response.body();
                    recyclerAdapter = new RecyclerAdapter(MainActivity.this, productList);
                    recyclerAdapter.setItemClickCallBack(MainActivity.this);
                    recyclerView.setAdapter(recyclerAdapter);
                    MyDialogs.dismissProgressDialog(MainActivity.this);
                }

                @Override
                public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                    Toast.makeText(MainActivity.this, "Something Error.!", Toast.LENGTH_SHORT).show();
                    MyDialogs.dismissProgressDialog(MainActivity.this);
                }
            });
        }
        else {
            Toast.makeText(this, "No Internet Connection.!", Toast.LENGTH_SHORT).show();
            MyDialogs.dismissProgressDialog(this);
        }
        }


    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
}
