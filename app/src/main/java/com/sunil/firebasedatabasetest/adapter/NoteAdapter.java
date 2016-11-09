package com.sunil.firebasedatabasetest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunil.firebasedatabasetest.R;
import com.sunil.firebasedatabasetest.model.NotesModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sunil on 11/8/16.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private List<NotesModel> noteList;
    private Context context;

    public NoteAdapter(Context context, List<NotesModel> notes) {
        this.noteList = notes;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    @Override
    public void onBindViewHolder(NoteViewHolder noteViewHolder, int i) {
        NotesModel note = noteList.get(i);
        noteViewHolder.title.setText(note.getTitle());
        noteViewHolder.description.setText(note.getDescription());
        noteViewHolder.user.setText(note.getUser());
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_notes, viewGroup, false);
        return new NoteViewHolder(itemView);
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.description)
        TextView description;
        @BindView(R.id.user)
        TextView user;


        public NoteViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
