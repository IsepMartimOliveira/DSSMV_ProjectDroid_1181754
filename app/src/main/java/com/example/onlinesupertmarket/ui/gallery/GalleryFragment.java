package com.example.onlinesupertmarket.ui.gallery;

import com.example.onlinesupertmarket.Service.GalleryViewModel;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.onlinesupertmarket.Adapter.IngredientItemAdapter;
import com.example.onlinesupertmarket.Adapter.RecipeItemAdapter;
import com.example.onlinesupertmarket.Model.Ingredients;
import com.example.onlinesupertmarket.R;
import com.example.onlinesupertmarket.databinding.FragmentGalleryBinding;

import java.util.ArrayList;
import java.util.List;

import static com.example.onlinesupertmarket.Utils.Utils.*;

public class GalleryFragment extends Fragment implements RecipeItemAdapter.OnQuestionMarkClickListener,IngredientItemAdapter.OnAddMarkClickListener {
    private GalleryViewModel galleryViewModel;
    private String username;
    private String hash;
    private String selectedRecipeTitle;
    private FragmentGalleryBinding binding;
    Spinner spinner, spinner_type,spinner_intollerances;
    Button button;
    String selectedCuisine="",selectedIntollerance="",selectedType="";
    private RecyclerView recyclerView,recyclerView2;
    private RecipeItemAdapter recipeItemAdapter;
    private  IngredientItemAdapter  ingredientAdapter;
    private LinearLayout moreOptionsLayout;
    private  ImageView hideenLinearLayout;
    private AlertDialog successAlertDialog,dialog;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        hash = sharedPreferences.getString("hash", "");
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

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
        //LiveData
        galleryViewModel.getRecipeItemsLiveData().observe(getViewLifecycleOwner(), recipeItems ->  {
                recipeItemAdapter.updateData(recipeItems);

        });

        galleryViewModel.getErrorLiveData().observe(getViewLifecycleOwner(), errorMessage -> {
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
        });
        galleryViewModel.getIngredientsLiveData().observe(getViewLifecycleOwner(), ingredients -> {
            if (ingredients != null && !ingredients.isEmpty()) {
                showIngredientsAlertDialog(ingredients);
            } else {
                Toast.makeText(getActivity(), "No ingredients found.", Toast.LENGTH_SHORT).show();
            }
        });
        galleryViewModel.getSuccessfulAdditionLiveData().observe(getViewLifecycleOwner(), success -> {
            if (success != null) {
                if (success) {
                    showSuccessAlertDialog();
                } else {
                    Toast.makeText(getActivity(), "Failed to add ingredient to basket", Toast.LENGTH_SHORT).show();
                }
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

                galleryViewModel.getRecipe(url);


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
               selectedCuisine = parentView.getItemAtPosition(position).toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        return root;


    }

    @Override
    public void onQuestionMarkClick(Integer id, String title) {
        selectedRecipeTitle = title;
        String url = apiUrl + recipeURL + id + information + api_key + includeNutrition;
        galleryViewModel.getIngredients(url);
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
                        galleryViewModel.sendIngredientToBasket(ingredientName, ingredients.size(),username,hash);
                    }
                }
            }
        });

        // Show the AlertDialog
        dialog = builder.create();
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
                successAlertDialog=builder.create();
                successAlertDialog.show();
            }
        });
    }



    @Override
    public void onAddMarkClick(String title) {
        galleryViewModel.sendIngredientToBasket(title,1,username,hash);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releaseViewReferences();
        unregisterEventListeners();
        dismissAlertDialog();
        galleryViewModel = null;
        binding = null;
    }
    private void releaseViewReferences() {
        recyclerView = null;
        recyclerView2 = null;
    }
    private void unregisterEventListeners() {
        button.setOnClickListener(null);
        hideenLinearLayout.setOnClickListener(null);
    }
    private void dismissAlertDialog(){
        if (successAlertDialog != null && successAlertDialog.isShowing()) {
            successAlertDialog.dismiss();
        }
        if(dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }
    }


}