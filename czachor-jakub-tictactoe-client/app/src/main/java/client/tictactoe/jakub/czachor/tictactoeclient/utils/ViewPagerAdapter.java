package client.tictactoe.jakub.czachor.tictactoeclient.utils;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import client.tictactoe.jakub.czachor.tictactoeclient.fragments.AccountFragment;
import client.tictactoe.jakub.czachor.tictactoeclient.fragments.RoomsFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private String playerName;

    public ViewPagerAdapter(FragmentManager fm, String playerName) {
        super(fm);
        this.playerName = playerName;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return AccountFragment.newInstance(this.playerName);
            case 1:
                return RoomsFragment.newInstance(this.playerName);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Profile";
            case 1:
                return "Tic-tac-toe";
            default:
                return "";
        }
    }
}
