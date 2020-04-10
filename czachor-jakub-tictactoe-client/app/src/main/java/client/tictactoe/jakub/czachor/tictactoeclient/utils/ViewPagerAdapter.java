package client.tictactoe.jakub.czachor.tictactoeclient.utils;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

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
