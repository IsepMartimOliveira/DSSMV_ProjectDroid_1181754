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
import com.example.onlinesupertmarket.DTO.RecipeDTO;
import com.example.onlinesupertmarket.DTO.RecipeItemDTO;
import com.example.onlinesupertmarket.Mapper.Convert;
import com.example.onlinesupertmarket.Network.HttpClient;
import com.example.onlinesupertmarket.R;
import com.example.onlinesupertmarket.databinding.FragmentGalleryBinding;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.onlinesupertmarket.Network.Utils.*;

public class GalleryFragment extends Fragment {
    private FragmentGalleryBinding binding;
    Spinner spinner;
    Button button;
    String selectedCuisine="";

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
                                List<RecipeItemDTO> recipes = new ArrayList<>();
                                for (RecipeItemDTO recipeItemDTO : recipeItemDTOS) {
                                    String title = recipeItemDTO.getTitle();
                                    String image = recipeItemDTO.getImage();
                                    String imageType=recipeItemDTO.getImageType();
                                    Integer id=recipeItemDTO.getId();
                                    RecipeItemDTO recipe = new RecipeItemDTO(id,title,image,imageType);
                                    recipes.add(recipe);


                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getActivity(), "Title: " + title + "\nImage: " + image, Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }


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