package com.example.testtp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.jetbrains.annotations.NotNull;

public class PagerAdapter extends FragmentStateAdapter {
    private int mPageCount=3;
    private FavoriteFragment favoriteFragment;
    private BusSearchFragment busFragment;
    private BusstopSearchFragment busstopFragment;
    public PagerAdapter(AppCompatActivity activity){
        super(activity);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position){
        switch(position){
            case 0:
                favoriteFragment = new FavoriteFragment();
                return favoriteFragment;
            case 1:
                busFragment = new BusSearchFragment();
                return busFragment;
            case 2:
                busstopFragment = new BusstopSearchFragment();
                return busstopFragment;
            default:
                return null;
        }
    }

    @Override
    public long getItemId(int position){
        return super.getItemId(position);
    }

    @Override
    public int getItemCount(){
        return mPageCount;
    }
}
