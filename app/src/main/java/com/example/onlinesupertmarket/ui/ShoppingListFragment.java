package com.example.onlinesupertmarket.ui;

import com.example.onlinesupertmarket.Service.ShoppingListViewModel;
import android.content.Context;
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
import com.example.onlinesupertmarket.Others.ItemSpacingDecoration;
import com.example.onlinesupertmarket.Adapter.ShoopingItemAdapter;
import com.example.onlinesupertmarket.Model.CartItem;
import com.example.onlinesupertmarket.R;
import com.example.onlinesupertmarket.databinding.FragmentSlideshowBinding;
import java.util.ArrayList;
import java.util.List;


public class ShoppingListFragment extends Fragment implements ShoopingItemAdapter.OnDeleteMarkClickListener {

    private ShoppingListViewModel shoppingListViewModel;
    private String username;
    private String hash;
    private ShoopingItemAdapter shoopingItemAdapter;
    private RecyclerView recyclerView;
    private FragmentSlideshowBinding binding;
    private TextView displayTotal;
    private Button checkOut, continueShopping;
    private ImageView deleteAll;
    private double totalCost = 0.0;
    private LinearLayout emptyView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shoppingListViewModel = new ViewModelProvider(this).get(ShoppingListViewModel.class);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        hash = sharedPreferences.getString("hash", "");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        emptyView = root.findViewById(R.id.emptyView);

        displayTotal = root.findViewById(R.id.totalCost);
        deleteAll = root.findViewById(R.id.deleteAllItems);
        checkOut = root.findViewById(R.id.checkOut);
        continueShopping = root.findViewById(R.id.continueShopping);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_spacing);

        recyclerView = root.findViewById(R.id.rycicleViewShopping);
        recyclerView.addItemDecoration(new ItemSpacingDecoration(spacingInPixels));

        shoopingItemAdapter = new ShoopingItemAdapter(new ArrayList<>(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(shoopingItemAdapter);

        shoppingListViewModel.getCartItemsLiveData().observe(getViewLifecycleOwner(), cartItems -> {
            shoopingItemAdapter.updateData(cartItems);
            totalCost = calculateTotalCost(cartItems);
            updateTotalCostView(totalCost);
        });
        shoppingListViewModel.getDeletedItemLiveData().observe(getViewLifecycleOwner(), deletedItemId -> {
            handleDeletedItem(deletedItemId);
        });
        shoppingListViewModel.getErrorLiveData().observe(getViewLifecycleOwner(), errorMessage -> {
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
        });


        shoppingListViewModel.fetchShoppingCart(username, hash);

        continueShopping.setOnClickListener(view -> {
            if (getActivity() instanceof MenuPageNavActivity) {
                ((MenuPageNavActivity) requireActivity()).navigateToRecepie();
            } else {
                Toast.makeText(requireContext(), "Cannot Access", Toast.LENGTH_SHORT).show();
            }
        });

        checkOut.setOnClickListener(view -> {
            checkOutDialog();
        });

        deleteAll.setOnClickListener(view -> {
            showDeleteAllConfirmationDialog();
        });

        return root;
    }

    private double calculateTotalCost(List<CartItem> cartItems) {
        double totalCost = 0.0;
        for (CartItem cartItem : cartItems) {
            totalCost += cartItem.getCost();
        }
        return totalCost;
    }

    private void updateTotalCostView(double totalCost) {
        if (shoopingItemAdapter.getItemCount() > 0) {
            displayTotal.setVisibility(View.VISIBLE);
            displayTotal.setText("TOTAL: " + String.format("%.2f", totalCost) + " €");
        } else {
            displayTotal.setVisibility(View.GONE);
        }
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
        builder.setPositiveButton("Delete", (dialog, which) -> {
            shoppingListViewModel.deleteItem(username, hash, itemToDeleteId);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
        });
        builder.create().show();
    }

    private void showDeleteAllConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Delete All Items");
        builder.setMessage("Do you wish to delete all items?");
        builder.setPositiveButton("Delete", (dialog, which) -> {
            shoppingListViewModel.deleteAllItems(username, hash);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
        });
        builder.create().show();
    }
    private void handleDeletedItem(String deletedItemId) {
        CartItem itemToRemove = null;
        for (CartItem item : shoopingItemAdapter.getData()) {
            if (item.getId().equals(deletedItemId)) {
                itemToRemove = item;
                break;
            }
        }

        if (itemToRemove != null) {
            shoopingItemAdapter.getData().remove(itemToRemove);
            shoopingItemAdapter.notifyDataSetChanged();
            updateTotalCostView(calculateTotalCost(shoopingItemAdapter.getData()));
        }
    }

    private void checkOutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("Do you wish to end your shopping?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            showProcessingMessage();
            shoppingListViewModel.deleteAllItems(username,hash);

        });
        builder.setNegativeButton("No", (dialog, which) -> {
        });
        builder.show();
    }

    private void showProcessingMessage() {
        // Create another AlertDialog for the processing message
        AlertDialog.Builder processingBuilder = new AlertDialog.Builder(requireContext());
        processingBuilder.setMessage("Your products are being processed...");
        processingBuilder.setPositiveButton("OK", (dialog, which) -> {
            // Handle the OK button press if needed
        });
        processingBuilder.show();
    }

}
