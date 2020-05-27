package edu.ktu.example.guessthenumber;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// žaidimo rezultato adapter
public class CustomAdapter_Score extends RecyclerView.Adapter<CustomAdapter_Score.CustomViewHolder2> {

    //tam kad recyclerView'e būtų galima paspausti ant elemento reikia clickListener interface šitam adaptoriui
    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mListener = listener;
    }

    private OnItemClickListener mListener;
    private List<ScoreEntry> allEntrys; // visi žaidimų rezultatai
    Context context;
    String[] difficulties;

    public CustomAdapter_Score(Context ct, List<ScoreEntry> e)
    {
        allEntrys = e;
        context = ct;
        difficulties = context.getResources().getStringArray(R.array.difficulty_values);
    }

    @NonNull
    @Override
    public CustomViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.scoreboard_row, parent, false);
        return new CustomViewHolder2(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder2 holder, int position) {

        holder.score_id_text.setText(difficulties[allEntrys.get(position).getDifficulty()]);
        holder.score_name_text.setText(allEntrys.get(position).getName());
        holder.score_amount_text.setText(String.valueOf(allEntrys.get(position).getScore()));
    }


    @Override
    public int getItemCount() {
        return allEntrys.size();
    }

    public class CustomViewHolder2 extends RecyclerView.ViewHolder {

        TextView score_id_text;
        TextView score_name_text;
        TextView score_amount_text;


        public CustomViewHolder2(@NonNull View itemView, final OnItemClickListener listener)
        {
            super(itemView);
            score_id_text = itemView.findViewById(R.id.score_id);
            score_name_text = itemView.findViewById(R.id.score_name);
            score_amount_text = itemView.findViewById(R.id.score_amount);

            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}