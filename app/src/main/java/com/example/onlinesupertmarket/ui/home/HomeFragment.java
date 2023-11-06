package com.example.onlinesupertmarket.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.onlinesupertmarket.MenuPageNavActivity;
import com.example.onlinesupertmarket.R;
import com.example.onlinesupertmarket.databinding.FragmentHomeBinding;
import com.example.onlinesupertmarket.registerPage;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ImageView recepieImageView = root.findViewById(R.id.recepie);
        ImageView shoopCart=root.findViewById(R.id.shoop);

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

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}