package com.shopify.canna.view.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shopify.canna.R;
import com.shopify.canna.view.base.AccountDesignRecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HelpCenterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HelpCenterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;


    public HelpCenterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HelpCenterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HelpCenterFragment newInstance(String param1, String param2) {
        HelpCenterFragment fragment = new HelpCenterFragment();
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
        return inflater.inflate(R.layout.fragment_help_center, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=view.findViewById(R.id.recyclerView);
        ((HomeActivity) getContext()).changeTitleByActivity(getContext(),false,"Help",true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);

        try {
            JSONArray jsonArray  = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("image","faq");
            jsonObject.put("heading","Frequently Asked Questions");
            jsonObject.put("desc","Frequently Asked Questions");
            jsonArray.put(jsonObject);



            jsonObject = new JSONObject();
            jsonObject.put("image","about_us");
            jsonObject.put("heading","About Us");
            jsonObject.put("desc","About Us");
            jsonArray.put(jsonObject);


            jsonObject = new JSONObject();
            jsonObject.put("image","retruns");
            jsonObject.put("heading","Refund");
            jsonObject.put("desc","Refund");
            jsonArray.put(jsonObject);


            jsonObject = new JSONObject();
            jsonObject.put("image","shipping");
            jsonObject.put("heading","Shipping");
            jsonObject.put("desc","Shipping");
            jsonArray.put(jsonObject);


            jsonObject = new JSONObject();
            jsonObject.put("image","privacy_policy");
            jsonObject.put("heading","Privacy Policy");
            jsonObject.put("desc","Privacy Policy");
            jsonArray.put(jsonObject);

            jsonObject = new JSONObject();
            jsonObject.put("image","terms_and_condition");
            jsonObject.put("heading","Terms And Condition");
            jsonObject.put("desc","Terms And Condition");
            jsonArray.put(jsonObject);




            AccountDesignRecyclerViewAdapter accountDesignRecyclerViewAdapter=new AccountDesignRecyclerViewAdapter(getContext(), jsonArray,R.layout.account_desing);
            recyclerView.setAdapter(accountDesignRecyclerViewAdapter);
            recyclerView.getAdapter().notifyDataSetChanged();
        }catch (Exception e){
            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}