package com.shopify.canna.view.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shopify.buy3.Storefront;
import com.shopify.canna.R;
import com.shopify.canna.util.Prefs;
import com.shopify.canna.view.base.AccountDesignRecyclerViewAdapter;
import com.shopify.canna.view.base.SingleImageRecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView textViewName, textViewEmail;
    private Storefront.Customer customer = Prefs.INSTANCE.fetchCustomerDetails();
    RecyclerView recyclerView;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=view.findViewById(R.id.recyclerView);
        textViewName = view.findViewById(R.id.text_name);
        textViewEmail = view.findViewById(R.id.text_email);

        ((HomeActivity) getContext()).changeTitleByActivity(getContext(),false,"Account",false);

        textViewName.setText(String.format("%s %s", customer.getFirstName(), customer.getLastName()));
        textViewEmail.setText(customer.getEmail());

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);

        try {
            JSONArray jsonArray  = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("image","my_order");
            jsonObject.put("heading","My Order");
            jsonObject.put("desc","Check My Order Status");
            jsonArray.put(jsonObject);



            jsonObject = new JSONObject();
            jsonObject.put("image","address");
            jsonObject.put("heading","Saved Address");
            jsonObject.put("desc","Add a delivery address for faster checkout");
            jsonArray.put(jsonObject);


            jsonObject = new JSONObject();
            jsonObject.put("image","help_center");
            jsonObject.put("heading","Help Center");
            jsonObject.put("desc","Find Help Here");
            jsonArray.put(jsonObject);


            jsonObject = new JSONObject();
            jsonObject.put("image","feedback");
            jsonObject.put("heading","Share Feedback");
            jsonObject.put("desc","Share your Feedback");
            jsonArray.put(jsonObject);


            jsonObject = new JSONObject();
            jsonObject.put("image","share_app");
            jsonObject.put("heading","Share App");
            jsonObject.put("desc","Share app with your friends");
            jsonArray.put(jsonObject);

            jsonObject = new JSONObject();
            jsonObject.put("image","call_us");
            jsonObject.put("heading","Call Us");
            jsonObject.put("desc","+91-8447826306");
            jsonArray.put(jsonObject);


            jsonObject = new JSONObject();
            jsonObject.put("image","logout");
            jsonObject.put("heading","Logout");
            jsonObject.put("desc","Logout");
            jsonArray.put(jsonObject);

            AccountDesignRecyclerViewAdapter accountDesignRecyclerViewAdapter=new AccountDesignRecyclerViewAdapter(getContext(), jsonArray,R.layout.account_desing);
            recyclerView.setAdapter(accountDesignRecyclerViewAdapter);
            recyclerView.getAdapter().notifyDataSetChanged();
        }catch (Exception e){

        }

    }
}