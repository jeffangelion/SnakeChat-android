package tk.jenrus.lamia;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ChatBoxAdapter extends RecyclerView.Adapter<ChatBoxAdapter.MyViewHolder> {
    private List<Message> MessageList;
    private int bubble;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nickname;
        TextView message;

        MyViewHolder(View view) {
            super(view);
            nickname =  view.findViewById(R.id.nickname);
            message =  view.findViewById(R.id.message);
        }
    }
    ChatBoxAdapter(List<Message> MessageList, int bubble) {
        this.MessageList = MessageList;
        this.bubble = bubble;
    }

    @Override
    public int getItemCount() {
        return MessageList.size();
    }
    @Override
    public ChatBoxAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(bubble, parent, false);
        return new ChatBoxAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ChatBoxAdapter.MyViewHolder holder, final int position) {
        final Message m = MessageList.get(position);
        holder.nickname.setText(m.getNickname());
        holder.message.setText(m.getMessage());
    }
}