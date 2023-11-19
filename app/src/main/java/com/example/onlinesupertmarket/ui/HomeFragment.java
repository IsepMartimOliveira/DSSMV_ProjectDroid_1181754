package com.example.onlinesupertmarket.ui;

import android.content.Context;
import com.example.onlinesupertmarket.Others.ShakeDetector;
import com.example.onlinesupertmarket.Service.HomeViewModel;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.onlinesupertmarket.R;
import com.example.onlinesupertmarket.databinding.FragmentHomeBinding;
import com.google.android.material.snackbar.Snackbar;

public class HomeFragment extends Fragment {
    private ShakeDetector shakeDetector;

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        Context context = requireActivity().getApplicationContext();
        shakeDetector = new ShakeDetector(context);


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ImageView recepieImageView = root.findViewById(R.id.recepie);
        ImageView shoopCart=root.findViewById(R.id.shoop);
        shakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake() {
                homeViewModel.getTrivia();
            }
        });
        recepieImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity() instanceof MenuPageNavActivity) {
                    ((MenuPageNavActivity) getActivity()).navigateToRecepie();
                }
                else{
                    Toast.makeText(requireContext(), "Cannot Acess", Toast.LENGTH_SHORT).show();
                }
            }

        });
    shoopCart.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(getActivity() instanceof MenuPageNavActivity) {
                ((MenuPageNavActivity) getActivity()).navigateToShop();
            }
            else{
                Toast.makeText(requireContext(), "Cannot Acess", Toast.LENGTH_SHORT).show();
            }

        }
    });
        homeViewModel.getTriviaLiveData().observe(getViewLifecycleOwner(), foodTrivia -> {
            if (foodTrivia != null) {
                Snackbar.make(requireView(), "Trivia: " + foodTrivia.getText(), Snackbar.LENGTH_LONG).show();
            }

        });

        return root;
    }
    @Override
    public void onResume() {
        super.onResume();
        shakeDetector.registerListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        shakeDetector.unregisterListener();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}