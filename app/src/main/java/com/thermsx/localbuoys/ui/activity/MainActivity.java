package com.thermsx.localbuoys.ui.activity;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.socks.library.KLog;
import com.thermsx.localbuoys.R;
import com.thermsx.localbuoys.api.ApiFactory;
import com.thermsx.localbuoys.api.LocalBuoyService;
import com.thermsx.localbuoys.api.response.LocationListResponse;
import com.thermsx.localbuoys.databinding.ActivityMainBinding;
import com.thermsx.localbuoys.model.LocationNode;
import com.thermsx.localbuoys.provider.table.BrowseContract;
import com.thermsx.localbuoys.ui.fragment.BrowseFragment;
import com.thermsx.localbuoys.util.CallbackAdapter;

import retrofit2.Call;

public class MainActivity extends ToolbarActivity implements BrowseFragment.BrowseFragmentListener {
    private static final String FRAGMENT_TAG = "items_list_container";
    private static final String SAVED_ITEM_ID = "com.thermsx.localbuoys.ITEM_ID";
    private static final String SAVED_IS_LOADING = "com.thermsx.localbuoys.IS_LOADING";

    private ActivityMainBinding mBinding;
    private boolean mIsLoading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initializeToolbar();
        initializeFromParams(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        long itemId = getItemId();
        if (itemId != BrowseContract.ROOT_ID) {
            outState.putLong(SAVED_ITEM_ID, itemId);
        }
        outState.putBoolean(SAVED_IS_LOADING, mIsLoading);
        super.onSaveInstanceState(outState);
    }

    private BrowseFragment getBrowseFragment() {
        return (BrowseFragment) getFragmentManager().findFragmentByTag(FRAGMENT_TAG);
    }

    @Override
    public void onItemSelected(View view, LocationNode node) {
        long id = node.getLocationId();
        KLog.d("id: " + id + "; isBrowsable: " + node.isBrowsable());
        if (node.isBrowsable()) {
            navigateTo(id);
        } else {
            Intent intent = new Intent(getContext(), DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_ITEM_ID, node.getLocationId());
            startActivity(intent);
        }
    }

    @Override
    public void setToolbarTitle(CharSequence title) {
        KLog.d(title);
        if (title == null) {
            title = getString(R.string.app_name);
        }
        setTitle(title);
    }

    private boolean isLoading() {
        return mIsLoading;
    }

    private void setLoading(final boolean isLoading) {
        mIsLoading = isLoading;
        mBinding.setIsLoading(mIsLoading);
    }

    protected void initializeFromParams(Bundle savedInstanceState) {
        long itemId = BrowseContract.ROOT_ID;

        if (savedInstanceState != null) {
            itemId = savedInstanceState.getLong(SAVED_ITEM_ID, BrowseContract.ROOT_ID);
            boolean loading = savedInstanceState.getBoolean(SAVED_IS_LOADING, false);
            setLoading(loading);
        } else {
            loadData();
        }
        navigateTo(itemId);
        updateToolbar();
    }

    private void navigateTo(long itemId) {
        KLog.d("id: " + itemId);

        BrowseFragment fragment = getBrowseFragment();
        if (fragment == null || fragment.getItemId() != itemId) {
            fragment = new BrowseFragment();
            fragment.setItemId(itemId);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.setCustomAnimations(
                    R.animator.slide_in_from_right, R.animator.slide_out_to_left,
                    R.animator.slide_in_from_left, R.animator.slide_out_to_right);
            transaction.replace(R.id.container, fragment, FRAGMENT_TAG);
            if (itemId > -1) {
                transaction.addToBackStack(null);
            }
            transaction.commit();
        }
    }

    private void loadData() {
        setLoading(true);
        LocalBuoyService service = ApiFactory.getService();
        Call<LocationListResponse> call = service.getLocationList();
        call.enqueue(new CallbackAdapter<LocationListResponse>() {
            @Override
            public void onSuccess(Call<LocationListResponse> call, LocationListResponse response) {
                super.onSuccess(call, response);
                KLog.d();
                setLoading(false);
                BrowseContract.saveHierarchy(getContext(), response.getRootItem());
            }

            @Override
            public void onError(Call<LocationListResponse> call, String error) {
                super.onError(call, error);
                setLoading(false);
                Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private long getItemId() {
        BrowseFragment fragment = getBrowseFragment();
        if (fragment == null) {
            return BrowseContract.ROOT_ID;
        }
        return fragment.getItemId();
    }

    private Context getContext() {
        return this;
    }

}
