package com.mully.prolificsoccerpredictions;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DailyTips extends Fragment {
    View view;
    RecyclerView mrecycler;
    LinearLayoutManager mlinearlayout;
    TextView loading;
    DatabaseReference mdatabaseRef;
    AdView ban_adView1;

    FirebaseRecyclerAdapter<Tips, ViewHolder> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Tips> select;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dailytips, container, false);

        mrecycler = view.findViewById(R.id.recycler1);
        mrecycler.setHasFixedSize(false);
        mlinearlayout = new LinearLayoutManager(getContext());
        mlinearlayout.setReverseLayout(true);
        mlinearlayout.setStackFromEnd(true);
        mrecycler.setLayoutManager(mlinearlayout);
        mdatabaseRef = FirebaseDatabase.getInstance().getReference().child("Tips1");
        loading = view.findViewById(R.id.loadwait);
        select = new FirebaseRecyclerOptions.Builder<Tips>().setQuery(mdatabaseRef, Tips.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Tips, ViewHolder>(select) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Tips model) {
                final String item_key = getRef(position).getKey();
                holder.setTitle(model.getTipsTitle());
                holder.setDetails(model.getTipsDetails());
                holder.setTime(model.getTipsDate());
                loading.setVisibility(View.GONE);
                holder.setOnClickListener(new ViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent adDetails = new Intent(view.getContext(), PostDetails.class);
                        adDetails.putExtra("postkey", item_key);
                        adDetails.putExtra("selection", "Tips1");
                        startActivity(adDetails);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listviewcard, parent, false);
                ViewHolder viewHolder = new ViewHolder(itemView);
                return viewHolder;

            }
        };
        mrecycler.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
        return view;
    }
    private void showBaners() {
        ban_adView1 = new com.facebook.ads.AdView(getContext(), "245389397006094_245403817004652", AdSize.BANNER_HEIGHT_50);
        LinearLayout adContainer = view.findViewById(R.id.banner_container1);
        adContainer.addView(ban_adView1);

        ban_adView1.loadAd();
    }

        @Override
    public void onStart() {
            super.onStart();

    }


}
