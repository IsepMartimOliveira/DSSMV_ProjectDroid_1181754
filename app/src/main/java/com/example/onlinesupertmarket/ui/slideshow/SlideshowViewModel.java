package com.example.onlinesupertmarket.ui.slideshow;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.onlinesupertmarket.DTO.AisleDTO;
import com.example.onlinesupertmarket.DTO.CartItemResponseDTO;
import com.example.onlinesupertmarket.DTO.ShoppingInfoDTO;
import com.example.onlinesupertmarket.Mapper.Convert;
import com.example.onlinesupertmarket.Mapper.DTOMapper;
import com.example.onlinesupertmarket.Model.CartItem;
import com.example.onlinesupertmarket.Network.HttpClient;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.example.onlinesupertmarket.Network.Utils.*;

public class SlideshowViewModel extends ViewModel {

    private final MutableLiveData<List<CartItem>> cartItemsLiveData = new MutableLiveData<>();
    private MutableLiveData<String> deletedItemLiveData = new MutableLiveData<>();
    private List<CartItem> cartItems=new ArrayList<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public LiveData<List<CartItem>> getCartItemsLiveData() {
        return cartItemsLiveData;
    }
    public LiveData<String> getDeletedItemLiveData() {
        return deletedItemLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public void fetchShoppingCart(String username, String hash) {
        String shoopingListURL = apiUrl + mealPlaner + username + shoopingList2 + api_key + hashURL + hash;

        HttpClient.getRequest(shoopingListURL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                errorLiveData.postValue("Network request failed.");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String jsonResponse = response.body().string();
                        CartItemResponseDTO cartItemResponseDTO = Convert.convertFromJson(jsonResponse, CartItemResponseDTO.class);

                        if (cartItemResponseDTO.getAisles() != null && !cartItemResponseDTO.getAisles().isEmpty()) {
                            List<ShoppingInfoDTO> shoppingInfoDTOs = new ArrayList<>();
                            for (AisleDTO aisleDTO : cartItemResponseDTO.getAisles()) {
                                shoppingInfoDTOs.addAll(aisleDTO.getItems());
                            }

                            DTOMapper<ShoppingInfoDTO, CartItem> shoppingInfoToCartItemDTOMapper = new DTOMapper<>(
                                    shoppingInfoDTO -> new CartItem(shoppingInfoDTO.getId(), shoppingInfoDTO.getName(), shoppingInfoDTO.getCost())
                            );

                            cartItems = shoppingInfoToCartItemDTOMapper.mapList(shoppingInfoDTOs);
                            cartItemsLiveData.postValue(cartItems);
                        } else {
                            errorLiveData.postValue("No items found.");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        errorLiveData.postValue("Error processing response.");
                    }
                } else {
                    errorLiveData.postValue("Unsuccessful response from the server.");
                }
            }
        });
    }

    public void deleteItem(String username, String hash, String itemId) {
        String url = apiUrl + mealPlaner + username + shoopingList + "/" + itemId + api_key + hashURL + hash;

        HttpClient.deleteRequest(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                // Ignore failure if the item is not found
                if (e.getMessage().contains("HTTP 404 Not Found")) {
                    deletedItemLiveData.postValue(itemId);
                } else {
                    errorLiveData.postValue("Failed to delete item.");
                }
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    deletedItemLiveData.postValue(itemId);
                } else {
                    // Ignore failure if the item is not found
                    if (response.code() == 404) {
                        deletedItemLiveData.postValue(itemId);
                    } else {
                        errorLiveData.postValue("Failed to delete item. Server response was not successful.");
                    }
                }
            }
        });
    }


    public void deleteAllItems(String username, String hash) {
        for (CartItem cartItem : cartItems) {
            String id = cartItem.getId();
            deleteItem(username, hash, id);
        }
    }


}
