package com.bahri.simaling;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bahri.simaling._slider.FragmentSlider;
import com.bahri.simaling._slider.SliderIndicator;
import com.bahri.simaling._slider.SliderPagerAdapter;
import com.bahri.simaling._slider.SliderView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private SliderPagerAdapter mAdapter;
    private SliderIndicator mIndicator;

    private SliderView sliderView;
    private LinearLayout mLinearLayout;
    private RelativeLayout menu1;

    //Data Baru
    RelativeLayout menuProfile;
    RelativeLayout menuLapor;
    RelativeLayout menuInformasi;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment
        sliderView = (SliderView) rootView.findViewById(R.id.sliderView);
        mLinearLayout = (LinearLayout) rootView.findViewById(R.id.pagesContainer);
//        menu1 = (RelativeLayout) rootView.findViewById(R.id.menu1);

        //Insialisasi Menu Profile
        menuProfile = (RelativeLayout) rootView.findViewById(R.id.menu4);
        menuLapor = (RelativeLayout) rootView.findViewById(R.id.menu2);
        menuInformasi = (RelativeLayout) rootView.findViewById(R.id.menu1);

//        menu1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //     Toast.makeText(getActivity(), "kllik me", Toast.LENGTH_SHORT).show();
//                //     Intent goPindah = new Intent(getActivity(), AboutActivity.class);
//                //    startActivity(goPindah);
//            }
//        });
        //Intent to Menu 1 atau Informasi
        menuInformasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = null;
                fragment = new InformasiFragment();
                replaceFragment(fragment);
            }

            private void replaceFragment(Fragment fragment) {
                InformasiFragment informasiFragment = new InformasiFragment();
                FragmentTransaction menu1fragmentTransaction = getFragmentManager().beginTransaction();
                menu1fragmentTransaction.replace(R.id.content,informasiFragment);
                menu1fragmentTransaction.addToBackStack(null);
                menu1fragmentTransaction.commit();
            }
        });

        //Intent to Menu 2 atau Lapor
        menuLapor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = null;
                fragment = new LaporanFragment();
                replaceFragment(fragment);
            }

            private void replaceFragment(Fragment fragment) {
                LaporanFragment laporanFragment = new LaporanFragment();
                FragmentTransaction menu2fragmentTransaction = getFragmentManager().beginTransaction();
                menu2fragmentTransaction.replace(R.id.content, laporanFragment);
                menu2fragmentTransaction.addToBackStack(null);
                menu2fragmentTransaction.commit();
            }
        });

        //Intent to Menu 4 atau Profile
        menuProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = null;
                fragment = new ProfileFragment();
                replaceFragment(fragment);
            }

            private void replaceFragment(Fragment fragment) {
                ProfileFragment profileFragment = new ProfileFragment();
                FragmentTransaction menu4fragmentTransaction = getFragmentManager().beginTransaction();
                menu4fragmentTransaction.replace(R.id.content, profileFragment);
                menu4fragmentTransaction.addToBackStack(null);
                menu4fragmentTransaction.commit();
            }
        });


        // jika internet aktfif
        setupSlider();

        return rootView;
    }

    private void setupSlider() {
        sliderView.setDurationScroll(800);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(FragmentSlider.newInstance("http://www.menucool.com/slider/prod/image-slider-1.jpg"));
        fragments.add(FragmentSlider.newInstance("http://www.menucool.com/slider/prod/image-slider-2.jpg"));
        fragments.add(FragmentSlider.newInstance("http://www.menucool.com/slider/prod/image-slider-3.jpg"));
        fragments.add(FragmentSlider.newInstance("http://www.menucool.com/slider/prod/image-slider-4.jpg"));

        mAdapter = new SliderPagerAdapter(getFragmentManager(), fragments);
        sliderView.setAdapter(mAdapter);
        mIndicator = new SliderIndicator(getActivity(), mLinearLayout, sliderView, R.drawable.indicator_circle);
        mIndicator.setPageCount(fragments.size());
        mIndicator.show();
    }

    private void setupSliderOfline() {
        sliderView.setDurationScroll(800);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(FragmentSlider.newInstance("image_slider_1"));
        fragments.add(FragmentSlider.newInstance("image_slider_2"));
        fragments.add(FragmentSlider.newInstance("image_slider_1"));
        fragments.add(FragmentSlider.newInstance("image_slider_2"));

        mAdapter = new SliderPagerAdapter(getFragmentManager(), fragments);
        sliderView.setAdapter(mAdapter);
        mIndicator = new SliderIndicator(getActivity(), mLinearLayout, sliderView, R.drawable.indicator_circle);
        mIndicator.setPageCount(fragments.size());
        mIndicator.show();
    }

}
