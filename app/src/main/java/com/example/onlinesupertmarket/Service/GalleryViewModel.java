package com.example.onlinesupertmarket.Service;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.onlinesupertmarket.DTO.ExtendedIngridientsDTO;
import com.example.onlinesupertmarket.DTO.RecepieInformationDTO;
import com.example.onlinesupertmarket.DTO.RecipeDTO;
import com.example.onlinesupertmarket.DTO.RecipeItemDTO;
import com.example.onlinesupertmarket.Mapper.Convert;
import com.example.onlinesupertmarket.Mapper.DTOMapper;
import com.example.onlinesupertmarket.Model.Ingredients;
import com.example.onlinesupertmarket.Model.RecipeItem;
import com.example.onlinesupertmarket.Model.ShoppingCart;
import com.example.onlinesupertmarket.Network.HttpClient;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.onlinesupertmarket.Utils.Utils.*;

public class GalleryViewModel extends ViewModel {
    private int successfulAdditions = 0;

    private MutableLiveData<List<RecipeItem>> recipeItemsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Ingredients>> ingredientsLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> successfulAdditionLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();


    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public LiveData<List<Ingredients>> getIngredientsLiveData() {
        return ingredientsLiveData;
    }

    public LiveData<List<RecipeItem>> getRecipeItemsLiveData() {
        return recipeItemsLiveData;
    }
    public LiveData<Boolean> getSuccessfulAdditionLiveData() {return successfulAdditionLiveData;}


    public void getRecipe(String urlApi) {
        HttpClient.getRequest(urlApi, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String jsonResponse = response.body().string();

                        RecipeDTO recipeDTO = Convert.convertFromJson(jsonResponse, RecipeDTO.class);
                        List<RecipeItem> recipes = new ArrayList<>();

                        if (recipeDTO.getResults() != null && !recipeDTO.getResults().isEmpty()) {
                            List<RecipeItemDTO> recipeItemDTOS = recipeDTO.getResults();

                            DTOMapper<RecipeItemDTO, RecipeItem> recipeItemMapper = new DTOMapper<>(dto -> {
                                RecipeItem recipeItem = new RecipeItem(dto.getId(), dto.getTitle(), dto.getImage());
                                recipeItem.setId(dto.getId());
                                recipeItem.setTitle(dto.getTitle());
                                recipeItem.setImage(dto.getImage());
                                return recipeItem;
                            });

                            recipes = recipeItemMapper.mapList(recipeItemDTOS);
                        }

                        recipeItemsLiveData.postValue(recipes);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                }
            }
        });
    }
    public void getIngredients(String url) {
        HttpClient.getRequest(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String jsonResponse = response.body().string();
                        Log.d("API Response", jsonResponse);

                        RecepieInformationDTO recepieInformationDTO = Convert.convertFromJson(jsonResponse, RecepieInformationDTO.class);

                        if (recepieInformationDTO != null && recepieInformationDTO.getExtendedIngredients() != null && !recepieInformationDTO.getExtendedIngredients().isEmpty()) {
                            List<ExtendedIngridientsDTO> extendedIngridientsDTOS = recepieInformationDTO.getExtendedIngredients();

                            DTOMapper<ExtendedIngridientsDTO, Ingredients> ingredientMapper = new DTOMapper<>(dto -> new Ingredients(dto.getName(), dto.getImage()));
                            List<Ingredients> ingredients = ingredientMapper.mapList(extendedIngridientsDTOS);
                            Log.d("Ingredients List Size", String.valueOf(ingredients.size()));

                            ingredientsLiveData.postValue(ingredients);
                        } else {
                            ingredientsLiveData.postValue(new ArrayList<>());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }
        });
    }
    public void sendIngredientToBasket(String ingredientName, final int totalIngredients,String username,String hash) {
        String shoppingUrl = apiUrl + mealPlaner + username + shoopingList + api_key + hashURL + hash;
        ShoppingCart shoppingCart = new ShoppingCart(ingredientName);
        String ingredientJson = Convert.convertToJson(shoppingCart);

        HttpClient.postRequest(shoppingUrl, ingredientJson, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                successfulAdditionLiveData.postValue(false);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseBody = response.body().string();
                if (response.isSuccessful()) {
                    successfulAdditions++;
                    if (successfulAdditions == totalIngredients) {
                        successfulAdditionLiveData.postValue(true);
                        successfulAdditions = 0;
                    }
                } else {
                    successfulAdditionLiveData.postValue(false);
                }
            }
        });
    }

}