package client.tictactoe.jakub.czachor.tictactoeclient.fragments;


import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import client.tictactoe.jakub.czachor.tictactoeclient.R;
import client.tictactoe.jakub.czachor.tictactoeclient.utils.ViewPagerAdapter;

public class AppMainFragment extends Fragment {
    private String playerName;

    public AppMainFragment() {
    }

    public static AppMainFragment newInstance(String playerName) {
        AppMainFragment f = new AppMainFragment();
        Bundle args = new Bundle();
        args.putString("playerName", playerName);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_main, container, false);
        this.loadArgs();
        this.loadViewPager(view);
        return view;
    }

    private void loadViewPager(View view) {
        ViewPager viewPager = view.findViewById(R.id.pager);
        ViewPagerAdapter myPagerAdapter = new ViewPagerAdapter(getFragmentManager(), this.playerName);
        viewPager.setAdapter(myPagerAdapter);
        TabLayout tabLayout = view.findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    void loadArgs() {
        Bundle args = getArguments();
        if (args != null) {
            this.playerName = args.getString("playerName");
        }
    }
}
