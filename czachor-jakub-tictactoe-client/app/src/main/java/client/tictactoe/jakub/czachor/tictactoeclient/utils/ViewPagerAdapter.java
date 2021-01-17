package client.tictactoe.jakub.czachor.tictactoeclient.utils;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import client.tictactoe.jakub.czachor.tictactoeclient.fragments.AccountFragment;
import client.tictactoe.jakub.czachor.tictactoeclient.fragments.RoomsFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new RoomsFragment();
        } else {
            return new AccountFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Tic-tac-toe";
        } else {
            return "Profile";
        }
    }
}
