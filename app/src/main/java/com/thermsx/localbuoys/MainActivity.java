package com.thermsx.localbuoys;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.socks.library.KLog;
import com.thermsx.localbuoys.api.ApiFactory;
import com.thermsx.localbuoys.api.LocalBuoyService;
import com.thermsx.localbuoys.api.LocationListResponse;
import com.thermsx.localbuoys.model.Item;
import com.thermsx.localbuoys.provider.table.BrowseTable;
import com.thermsx.localbuoys.ui.fragment.BrowseDBFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements BrowseDBFragment.BrowseFragmentListener {
    private static final String FRAGMENT_TAG = "items_list_cantainer";

    private static final String TAG = "MainActivity";
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initBrowseFragment();

        mProgressBar = (ProgressBar) findViewById(R.id.progress);

        LocalBuoyService service = ApiFactory.getService();
        Call<LocationListResponse> call = service.getLocationList();
        call.enqueue(new Callback<LocationListResponse>() {
            @Override
            public void onResponse(Call<LocationListResponse> call, Response<LocationListResponse> response) {
                mProgressBar.setVisibility(View.GONE);
                LocationListResponse body = response.body();
                KLog.d(body.getResultCodeName());

                BrowseTable.clean(getContext());
                for (Item item : body.getItems().get(0).getItems()) {
                    BrowseTable.insert(getContext(), item);
                    if (item.getItems() != null) {
                        for (Item a : item.getItems()) {
                            if (a.isBrowsable()) {
                                BrowseTable.insert(getContext(), a);
                            }
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<LocationListResponse> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private void initBrowseFragment() {
        BrowseDBFragment fragment = getBrowseFragment();
        if (fragment == null) {
            fragment = BrowseDBFragment.newInstance(-1);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, fragment, FRAGMENT_TAG);
            transaction.commit();
        }
    }

    private BrowseDBFragment getBrowseFragment() {
        return (BrowseDBFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
    }

    @Override
    public void onItemSelected(View view, long id) {
        KLog.d("id: " + id);
        final BrowseDBFragment fragment = BrowseDBFragment.newInstance(id);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment, FRAGMENT_TAG);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private Context getContext() {
        return this;
    }


}
