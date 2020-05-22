package com.crazynnc.agoravai;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class AdaptadorPage extends FragmentStatePagerAdapter {
    private final int pageCount = 3;
    private String[]tabtitles = new String[]{"DEFINIR","CONTATOS","CHAMADAS"};

    public AdaptadorPage(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return new TlpDefinirCasa();
            case 1:
                return new TlpContatos();
            case 2:
                return new TlpOnline();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return pageCount;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabtitles[position];
    }
}
