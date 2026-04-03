package com.example.baitapnhom.ui.category;

import android.os.Bundle;
import android.view.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import com.example.baitapnhom.R;
import com.example.baitapnhom.ShoppingApplication;
import com.example.baitapnhom.databinding.FragmentCategoryBinding;
import com.example.baitapnhom.ui.home.HomeViewModel;
import com.example.baitapnhom.ui.home.HomeViewModelFactory;
import com.example.baitapnhom.utils.Constants;

public class CategoryFragment extends Fragment {

    private FragmentCategoryBinding b;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inf, ViewGroup c, Bundle s) {
        b = FragmentCategoryBinding.inflate(inf, c, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ShoppingApplication app = (ShoppingApplication) requireActivity().getApplication();
        HomeViewModel vm = new ViewModelProvider(this,
                new HomeViewModelFactory(app.getProductRepository(), app.getCategoryRepository())
        ).get(HomeViewModel.class);

        CategoryAdapter adapter = new CategoryAdapter(cat -> {
            Bundle args = new Bundle();
            args.putInt(Constants.ARG_CATEGORY_ID, cat.id);
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_categoryFragment_to_productListFragment, args);
        });

        b.rvCategories.setAdapter(adapter);
        b.rvCategories.setLayoutManager(new GridLayoutManager(getContext(), 4));

        vm.allCategories.observe(getViewLifecycleOwner(), adapter::submitList);
    }

    @Override public void onDestroyView() { super.onDestroyView(); b = null; }
}