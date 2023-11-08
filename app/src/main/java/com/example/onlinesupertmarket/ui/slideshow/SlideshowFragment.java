package com.example.onlinesupertmarket.ui.slideshow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.onlinesupertmarket.Adapter.RecipeItemAdapter;
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
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.onlinesupertmarket.Network.Utils.*;

public class SlideshowFragment extends Fragment implements ShoopingItemAdapter.OnDeleteMarkClickListener {
    private String username;
    private String hash;
    private ShoopingItemAdapter shoopingItemAdapter;
    private RecyclerView recyclerView;
    private FragmentSlideshowBinding binding;
    private TextView displayTotal;
    private List<CartItem> cartItems=new ArrayList<>();
    private Button checkOut,continueShopping;

    private ImageView deleteAll;
    private List<String> allItemIds = new ArrayList<>();

    private double totalCost = 0.0;
    private LinearLayout emptyView ;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        hash = sharedPreferences.getString("hash", "");
        View root = binding.getRoot();
        emptyView = root.findViewById(R.id.emptyView);

        displayTotal=root.findViewById(R.id.totalCost);
        deleteAll=root.findViewById(R.id.deleteAllItems);
        checkOut=root.findViewById(R.id.checkOut);
        continueShopping=root.findViewById(R.id.continueShopping);

        recyclerView=root.findViewById(R.id.rycicleViewShopping);
        shoopingItemAdapter=new ShoopingItemAdapter(new ArrayList<>(),this);
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

    deleteAll.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showDeleteAllConfirmationDialog();

        }
    });

        return root;
    }
private void getShoopingCart(String url){
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
                        cartItems = shoppingInfoToCartItemDTOMapper.mapList(shoppingInfoDTOs);
                        totalCost = 0.0;
                        for (CartItem cartItem : cartItems) {
                            totalCost += cartItem.getCost();
                        }
                        for (CartItem cartItem : cartItems) {
                            allItemIds.add(cartItem.getId());
                        }


                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                shoopingItemAdapter.updateData(cartItems);
                                if (!cartItems.isEmpty()) {
                                    displayTotal.setVisibility(View.VISIBLE);
                                    displayTotal.setText("TOTAL: " + String.format("%.2f", totalCost) + " €");
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

    @Override
    public void onDeleteMarkClick(String id) {

         showDeleteConfirmationDialog(id);


    }
    private void showDeleteConfirmationDialog(String itemToDeleteId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Delete Item");
        builder.setMessage("Do you wish to delete the item?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String url = apiUrl + mealPlaner + username + shoopingList + "/" + itemToDeleteId + api_key + hashURL + hash;
                deleteItem(url,itemToDeleteId);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }


    private void deleteItem(String url, String itemToDeleteId) {
        HttpClient.deleteRequest(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            CartItem itemToRemove = null;
                            for (CartItem item : cartItems) {
                                if (item.getId().equals(itemToDeleteId)) {
                                    itemToRemove = item;
                                    break;
                                }
                            }

                            if (itemToRemove != null) {
                                cartItems.remove(itemToRemove);
                                shoopingItemAdapter.notifyDataSetChanged();
                                if(cartItems.isEmpty()){
                                    shoopingItemAdapter.clear();
                                    displayTotal.setVisibility(View.GONE);
                                }
                                String shoopingListURL = apiUrl + mealPlaner + username + shoopingList2 + api_key + hashURL + hash;
                                getShoopingCart(shoopingListURL);
                            }



                        }
                    });
                }
            }
        });
    }


   private void deleteAllItems() {
        for (String itemId : allItemIds) {
            String url = apiUrl + mealPlaner + username + shoopingList + "/" + itemId + api_key + hashURL + hash;
            deleteItem(url, itemId);
        }
        allItemIds.clear();
       displayTotal.setVisibility(View.GONE);
       shoopingItemAdapter.clear();

   }
    private void showDeleteAllConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Delete All Items");
        builder.setMessage("Do you wish to delete all items?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAllItems();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }



}