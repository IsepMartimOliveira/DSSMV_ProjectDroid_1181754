package com.example.onlinesupertmarket.ui.gallery;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.onlinesupertmarket.Adapter.RecipeItemAdapter;
import com.example.onlinesupertmarket.DTO.IngredientDTO;
import com.example.onlinesupertmarket.DTO.IngredientsDTO;
import com.example.onlinesupertmarket.DTO.RecipeDTO;
import com.example.onlinesupertmarket.DTO.RecipeItemDTO;
import com.example.onlinesupertmarket.Mapper.Convert;
import com.example.onlinesupertmarket.Mapper.DTOMapper;
import com.example.onlinesupertmarket.Model.Ingredient;
import com.example.onlinesupertmarket.Model.RecipeItem;
import com.example.onlinesupertmarket.Network.HttpClient;
import com.example.onlinesupertmarket.R;
import com.example.onlinesupertmarket.databinding.FragmentGalleryBinding;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.onlinesupertmarket.Network.Utils.*;

public class GalleryFragment extends Fragment implements RecipeItemAdapter.OnQuestionMarkClickListener {
    private FragmentGalleryBinding binding;
    Spinner spinner;
    Button button;
    String selectedCuisine="";

    private RecyclerView recyclerView;
    private RecipeItemAdapter recipeItemAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        spinner = root.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(), R.array.spinner_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        button=root.findViewById(R.id.getCuisine);

        recyclerView = root.findViewById(R.id.recyclerView);
        recipeItemAdapter = new RecipeItemAdapter(new ArrayList<>(),this);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(recipeItemAdapter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = apiUrl+recipe+api_key+cuisine+selectedCuisine;
                getRecepie(url);


            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected item as a String
               selectedCuisine = parentView.getItemAtPosition(position).toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle the case when nothing is selected
            }
        });
        return root;


    }




    public void getRecepie(String urlApi) {
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
                        Log.d("API Response", jsonResponse);

                        RecipeDTO recipeDTO = Convert.convertFromJson(jsonResponse, RecipeDTO.class);

                        if (recipeDTO.getResults() != null && !recipeDTO.getResults().isEmpty()) {
                            List<RecipeItemDTO> recipeItemDTOS = recipeDTO.getResults();

                            // Process the ResultsDTO objects and create Recipe objects
                            DTOMapper<RecipeItemDTO, RecipeItem> recipeItemMapper = new DTOMapper<>(dto -> new RecipeItem(dto.getTitle(), dto.getImage()));
                            List<RecipeItem> recipes = recipeItemMapper.mapList(recipeItemDTOS);

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    recipeItemAdapter.updateData(recipes);
                                }
                            });
                        } else {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "No recipes found.", Toast.LENGTH_SHORT).show();
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
    public void onQuestionMarkClick(String title) {
        String url = apiUrl + searchIngredient + api_key +"&q=" +title;
        getIngridients(url);
    }

    public void getIngridients(String url) {
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

                        IngredientsDTO ingredientsDTO = Convert.convertFromJson(jsonResponse, IngredientsDTO.class);

                        if (ingredientsDTO != null && ingredientsDTO.getIngredientsListDTOS() != null && !ingredientsDTO.getIngredientsListDTOS().isEmpty()) {
                            List<IngredientDTO> ingredientDTOs = ingredientsDTO.getIngredientsListDTOS();

                            // Process the IngredientDTO objects and create Ingredient objects
                            DTOMapper<IngredientDTO, Ingredient> ingredientMapper = new DTOMapper<>(dto -> new Ingredient(dto.getName(), dto.isInclude(), dto.getImage()));
                            List<Ingredient> ingredients = ingredientMapper.mapList(ingredientDTOs);
                            Log.d("Ingredients List Size", String.valueOf(ingredients.size()));



                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    for (Ingredient ingredient : ingredients) {
                                        String ingredientName = ingredient.getName();
                                        Log.d("Ingredient Name", ingredientName);
                                        Toast.makeText(getActivity(), "IG:"+ingredientName, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "No ingredients found.", Toast.LENGTH_SHORT).show();
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
}