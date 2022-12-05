package com.paszlelab.dcroarapp.Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.paszlelab.dcroarapp.Fragments.ChatsFragment;
import com.paszlelab.dcroarapp.Fragments.CoursesFragment;
import com.paszlelab.dcroarapp.Fragments.FriendsFragment;
import com.paszlelab.dcroarapp.Fragments.ProfileFragment;
import com.paszlelab.dcroarapp.R;
import com.paszlelab.dcroarapp.databinding.ActivityMainBinding;

public class MainPage extends BaseActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new ChatsFragment());

        binding.navBar.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.chats:
                    replaceFragment(new ChatsFragment());
                    break;
                case R.id.courses:
                    replaceFragment(new CoursesFragment());
                    break;
                case R.id.friends:
                    replaceFragment(new FriendsFragment());
                    break;
                case R.id.profile:
                    replaceFragment(new ProfileFragment());
                    break;
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.rFrameLayout,fragment);
        fragmentTransaction.commit();
    }

}