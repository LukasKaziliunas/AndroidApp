package edu.ktu.example.guessthenumber;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapter_Guess extends RecyclerView.Adapter<CustomAdapter_Guess.CustomViewHolder> {

    private List<String> text;
    private List<Integer> images;
    Context context;

    public CustomAdapter_Guess(Context ct, List<String> s, List<Integer> img)
    {
        text = s;
        images = img;
        context = ct;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.guess_row, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.textView.setText(text.get(position));
        holder.imageView.setImageResource(images.get(position));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView imageView;

        public CustomViewHolder(@NonNull View itemView)
        {
            super(itemView);
            textView = itemView.findViewById(R.id.guess_number);
            imageView = itemView.findViewById(R.id.arrow_img);
        }
    }
}
