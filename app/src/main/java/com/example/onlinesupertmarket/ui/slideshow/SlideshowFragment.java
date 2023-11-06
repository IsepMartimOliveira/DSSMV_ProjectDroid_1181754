package com.example.onlinesupertmarket.ui.slideshow;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.onlinesupertmarket.Adapter.ShoopingItemAdapter;
import com.example.onlinesupertmarket.DTO.*;
import com.example.onlinesupertmarket.Mapper.Convert;
import com.example.onlinesupertmarket.Mapper.DTOMapper;
import com.example.onlinesupertmarket.MenuPageNavActivity;
import com.example.onlinesupertmarket.Model.CartItem;
import com.example.onlinesupertmarket.Network.HttpClient;
import com.example.onlinesupertmarket.R;
import com.example.onlinesupertmarket.databinding.FragmentSlideshowBinding;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.onlinesupertmarket.Network.Utils.*;

public class SlideshowFragment extends Fragment {
    private String username;
    private String hash;
    private ShoopingItemAdapter shoopingItemAdapter;
    private RecyclerView recyclerView;
    private FragmentSlideshowBinding binding;
    private TextView displayTotal;

    private Button checkOut,continueShopping;

    private double totalCost = 0.0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        hash = sharedPreferences.getString("hash", "");
        View root = binding.getRoot();

        displayTotal=root.findViewById(R.id.totalCost);

        checkOut=root.findViewById(R.id.checkOut);
        continueShopping=root.findViewById(R.id.continueShopping);

        recyclerView=root.findViewById(R.id.rycicleViewShopping);
        shoopingItemAdapter=new ShoopingItemAdapter(new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(shoopingItemAdapter);
        String shoopingListURL=apiUrl+mealPlaner+username+shoopingList2+api_key+hashURL+hash;
        getShoopingCart(shoopingListURL);

        continueShopping.setOnClickListener(new View.OnClickListener() {
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
        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



        return root;
    }
public void getShoopingCart(String url){
    HttpClient.getRequest(url, new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            e.printStackTrace();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response.isSuccessful()) {
                try {
                    String jsonResponse = response.body().string();
                    Log.d("API Response", jsonResponse);

                    CartItemResponseDTO cartItemResponseDTO = Convert.convertFromJson(jsonResponse, CartItemResponseDTO.class);

                    if (cartItemResponseDTO.getAisles() != null && !cartItemResponseDTO.getAisles().isEmpty()) {
                        List<ShoppingInfoDTO> shoppingInfoDTOs = new ArrayList<>();
                        for (AisleDTO aisleDTO : cartItemResponseDTO.getAisles()) {
                            shoppingInfoDTOs.addAll(aisleDTO.getItems());
                        }

                        DTOMapper<ShoppingInfoDTO, CartItem> shoppingInfoToCartItemDTOMapper = new DTOMapper<>(
                                shoppingInfoDTO -> {
                                    String name = shoppingInfoDTO.getName();
                                    String id = shoppingInfoDTO.getId();
                                    Double cost = shoppingInfoDTO.getCost();
                                    return new CartItem(id, name, cost);
                                }
                        );
                        List<CartItem> cartItems = shoppingInfoToCartItemDTOMapper.mapList(shoppingInfoDTOs);
                        totalCost = 0.0;
                        for (CartItem cartItem : cartItems) {
                            totalCost += cartItem.getCost();
                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                shoopingItemAdapter.updateData(cartItems);
                                if (!cartItems.isEmpty()) {
                                    displayTotal.setVisibility(View.VISIBLE);
                                    displayTotal.setText("Total Price: " + String.format("%.2f", totalCost) + " €");
                                } else {
                                    displayTotal.setVisibility(View.GONE);
                                }

                            }


                        });
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "No items found.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // Handle the response when it's not successful
            }
        }
    });


}
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;

    }

}