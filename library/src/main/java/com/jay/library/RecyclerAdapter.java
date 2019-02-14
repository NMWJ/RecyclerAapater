package com.jay.library;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> implements Filterable {

    private int layoutId;
    private List<T> data;
    private BindView<T> bindView;
    private ArrayList<T> tempList;
    private ArrayFilter mFilter;
    private Animation animation;
    private static final int EMPTY_VIEW = 1;

    private boolean isEmpty = true;

    public RecyclerAdapter(Context context,int layoutId, List<T> data, BindView<T> bindView) {
        this.layoutId = layoutId;
        this.data = data;
        this.bindView = bindView;
        animation = AnimationUtils.loadAnimation(context, R.anim.alpha_in);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==EMPTY_VIEW){

            FrameLayout frameLayout = new FrameLayout(parent.getContext());
            frameLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
            if(isEmpty){

                TextView textView = new TextView(parent.getContext());
                textView.setText("暂时没有数据内容");
                textView.setTextColor(Color.GRAY);
                textView.setTextSize(24);
                textView.setGravity(Gravity.CENTER);
                textView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
                frameLayout.addView(textView);
                return new ViewHolder(frameLayout);
            } else {
                return new ViewHolder(frameLayout);
            }
        }else {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if(data.size()>0) bindView.bindView(holder,data.get(position),position);

    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.itemView.setAnimation(animation);
    }

    @Override
    public int getItemCount() {

        if(data.size()==0){
            return 1;
        }

        return data.size();
    }


    @Override
    public int getItemViewType(int position) {
        if(data==null || data.size()==0){
            return EMPTY_VIEW;
        }
        return super.getItemViewType(position);
    }

    public void setEmptyView(boolean isEmpty){
        this.isEmpty = isEmpty;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private View itemView;

        private ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }

        public void setText(int id,CharSequence sequence){
            TextView textView = itemView.findViewById(id);
            if(textView!=null) textView.setText(sequence);
        }

        public void setChildViewOnClick(int id, View.OnClickListener onClickListener){
            View view = itemView.findViewById(id);
            if(view!=null) view.setOnClickListener(onClickListener);
        }

        public void setOnItemClick(View.OnClickListener onClickListener){
            itemView.setOnClickListener(onClickListener);
        }

        public void setImageResources(int id,String path){
            ImageView imageView = itemView.findViewById(id);
            if(imageView!=null) Glide.with(itemView).load(path).into(imageView);
        }

        public void setVisibility(int id,boolean isVisibility){
            View view = itemView.findViewById(id);
            if(view!=null) {
                if(isVisibility){
                    view.setVisibility(View.VISIBLE);
                }else {
                    view.setVisibility(View.GONE);
                }
            }
        }

        public void setProgerss(int id,int progress){
            ProgressBar progressBar = itemView.findViewById(id);
            if( progressBar!=null ) progressBar.setProgress(progress);
        }

        public void checked(int id, boolean check, CompoundButton.OnCheckedChangeListener clickListener){
            CheckBox checkBox = itemView.findViewById(id);
            if(checkBox!=null){
                checkBox.setOnCheckedChangeListener(clickListener);
                checkBox.setChecked(check);
            }
        }

    }

    public interface BindView<T>{
        void bindView(ViewHolder holder, T obj, int position);
    }

    private final Object lock = new Object();

    private class ArrayFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if (tempList == null) {
                synchronized (lock) {
                    tempList = new ArrayList<>(data);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                ArrayList<T> list;
                synchronized (lock) {
                    list = new ArrayList<>(tempList);
                }
                results.values = list;
                results.count = list.size();
            } else {
                String prefixString = prefix.toString().toLowerCase();

                ArrayList<T> values;
                synchronized (lock) {
                    values = new ArrayList<>(tempList);
                }
                final int count = values.size();
                final ArrayList<T> newValues = new ArrayList<>();

                for (int i = 0; i < count; i++) {
                    T t = values.get(i);
                    final String valueText =t.toString().toLowerCase();
                    if (valueText.startsWith(prefixString) || valueText.contains(prefixString)) {
                        newValues.add(t);
                    } else {
                        final String[] words = valueText.split(" ");
                        for (String word : words) {
                            if (word.startsWith(prefixString)) {
                                newValues.add(t);
                                break;
                            }
                        }
                    }
                }
                results.values = newValues;
                results.count = newValues.size();
            }
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence prefix, FilterResults results) {
            data = (List<T>) results.values;
            notifyDataSetChanged();
        }
    }
}
