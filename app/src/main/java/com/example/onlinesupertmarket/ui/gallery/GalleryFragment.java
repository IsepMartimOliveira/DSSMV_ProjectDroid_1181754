package com.example.onlinesupertmarket.ui.gallery;

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
import com.example.onlinesupertmarket.Adapter.IngredientItemAdapter;
import com.example.onlinesupertmarket.Adapter.RecipeItemAdapter;
import com.example.onlinesupertmarket.DTO.*;
import com.example.onlinesupertmarket.Mapper.Convert;
import com.example.onlinesupertmarket.Mapper.DTOMapper;
import com.example.onlinesupertmarket.Model.Ingredients;
import com.example.onlinesupertmarket.Model.RecipeItem;
import com.example.onlinesupertmarket.Model.ShoppingCart;
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

public class GalleryFragment extends Fragment implements RecipeItemAdapter.OnQuestionMarkClickListener,IngredientItemAdapter.OnAddMarkClickListener {
    private String username;
    private String hash;
    private String selectedRecipeTitle;

    private FragmentGalleryBinding binding;
    Spinner spinner;

    Spinner spinner_intollerances;
    Spinner spinner_type;
    Button button;
    String selectedCuisine="";
    String selectedIntollerance="";
    String selectedType="";

    private RecyclerView recyclerView,recyclerView2;
    private RecipeItemAdapter recipeItemAdapter;

    private  IngredientItemAdapter  ingredientAdapter;
    private LinearLayout moreOptionsLayout;

    private  ImageView hideenLinearLayout;
    private int successfulAdditions = 0;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        hash = sharedPreferences.getString("hash", "");
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        spinner = root.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(), R.array.spinner_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);

        spinner_intollerances=root.findViewById(R.id.spinnerIntolerances);
        ArrayAdapter<CharSequence> adapter_intolerances = ArrayAdapter.createFromResource(requireContext(), R.array.intolerence, android.R.layout.simple_spinner_item);
        adapter_intolerances.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_intollerances.setAdapter(adapter_intolerances);
        spinner_intollerances.setSelection(0);

        spinner_type=root.findViewById(R.id.spinnerType);
        ArrayAdapter<CharSequence> adapter_type = ArrayAdapter.createFromResource(requireContext(), R.array.type, android.R.layout.simple_spinner_item);
        adapter_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_type.setAdapter(adapter_type);
        spinner_type.setSelection(0);

        button=root.findViewById(R.id.getCuisine);
        hideenLinearLayout=root.findViewById(R.id.showOptionsButton);
        recyclerView = root.findViewById(R.id.recyclerView);
        moreOptionsLayout = root.findViewById(R.id.moreOptionsLayout);
        recipeItemAdapter = new RecipeItemAdapter(new ArrayList<>(),this);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(recipeItemAdapter);
        hideenLinearLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onShowOptionsClick(view);

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = apiUrl+recipe+api_key;
                if(!selectedCuisine.equals("None")&& !selectedCuisine.isEmpty()){
                    url +="&cuisine="+selectedCuisine;
                }
                if (!selectedIntollerance.isEmpty() && !selectedIntollerance.equals("None")) {
                    url += "&intolerances=" + selectedIntollerance;
                }


                if (!selectedType.isEmpty() && !selectedType.equals("None")) {
                    url += "&type=" + selectedType;
                }

                getRecepie(url);


            }
        });
        spinner_intollerances.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                 selectedIntollerance=adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                 selectedType=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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

                            DTOMapper<RecipeItemDTO, RecipeItem> recipeItemMapper = new DTOMapper<>(dto -> {
                                RecipeItem recipeItem = new RecipeItem(dto.getId(), dto.getTitle(), dto.getImage());
                                recipeItem.setId(dto.getId());
                                recipeItem.setTitle(dto.getTitle());
                                recipeItem.setImage(dto.getImage());
                                return recipeItem;
                            });
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
    public void onQuestionMarkClick(Integer id,String title) {
        selectedRecipeTitle = title;
        String url = apiUrl +recipeURL+id+information + api_key +includeNutrition;
        getIngridients(url);
    }




    private void sendIngredientToBasket(String ingredientName,final int totalIngredients) {
        String shoppingUrl=apiUrl+mealPlaner+username+shoopingList+api_key+hashURL+hash;
        ShoppingCart shoppingCart = new ShoppingCart(ingredientName);
        String ingredientJson = Convert.convertToJson(shoppingCart);
        successfulAdditions++;
        HttpClient.postRequest(shoppingUrl, ingredientJson, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseBody = response.body().string();
                if (response.isSuccessful()) {

                    if (successfulAdditions == totalIngredients) {
                        showSuccessAlertDialog();
                        successfulAdditions=0;
                    }
                }

            }
        });
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

                        RecepieInformationDTO recepieInformationDTO = Convert.convertFromJson(jsonResponse, RecepieInformationDTO.class);

                        if (recepieInformationDTO != null && recepieInformationDTO.getExtendedIngredients() != null && !recepieInformationDTO.getExtendedIngredients().isEmpty()) {
                            List<ExtendedIngridientsDTO> extendedIngridientsDTOS = recepieInformationDTO.getExtendedIngredients();

                            DTOMapper<ExtendedIngridientsDTO, Ingredients> ingredientMapper = new DTOMapper<>(dto -> new Ingredients(dto.getName(),dto.getImage()));
                            List<Ingredients> ingredients = ingredientMapper.mapList(extendedIngridientsDTOS);
                            Log.d("Ingredients List Size", String.valueOf(ingredients.size()));



                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showIngredientsAlertDialog(ingredients);
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

    private void showIngredientsAlertDialog(List<Ingredients> ingredients) {
        View alertDialogView = getLayoutInflater().inflate(R.layout.alert_dialog_ingridients, null);
        recyclerView2 = alertDialogView.findViewById(R.id.recyclerView2);


        ingredientAdapter=new IngredientItemAdapter(new ArrayList<>(),this);
        recyclerView2.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView2.setAdapter(ingredientAdapter);

        ingredientAdapter.updateData(ingredients);


        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(alertDialogView);
        builder.setTitle("Ingredients for " + selectedRecipeTitle);
        builder.setNegativeButton("Cancell", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                List<Ingredients> ingredients = ingredientAdapter.getIngredientList();

                if (ingredients != null && !ingredients.isEmpty()) {

                    for (Ingredients ingredient : ingredients) {
                        String ingredientName = ingredient.getName();

                        sendIngredientToBasket(ingredientName,ingredients.size());
                    }
                }
            }

        });

        // Show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void onShowOptionsClick(View view) {

        if (moreOptionsLayout.getVisibility() == View.VISIBLE) {
            moreOptionsLayout.setVisibility(View.GONE);
        } else {
            moreOptionsLayout.setVisibility(View.VISIBLE);
        }
    }
    private void showSuccessAlertDialog() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Success");
                builder.setMessage("The ingredient/ingredients have been added to your shopping list.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
    }



    @Override
    public void onAddMarkClick(String title) {
        sendIngredientToBasket(title,1);
    }
}