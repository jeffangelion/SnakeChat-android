package tk.jenrus.lamia;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class ChatBoxActivity extends AppCompatActivity {
    public RecyclerView myRecyclerView;
    public List<Message> messageList;
    public ChatBoxAdapter chatBoxAdapter;
    private EditText messageText;
    private Socket socket;
    private String nickname;
    private int bubble = R.layout.message_received;


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_box);
        messageText = findViewById(R.id.message);
        ImageButton send = findViewById(R.id.send);
        //Подключение к серверу
        setNickname(getIntent().getExtras().getString(MainActivity.NICKNAME));
        String address = getIntent().getExtras().getString(MainActivity.ADDRESS);
        boolean secured = getIntent().getExtras().getBoolean(MainActivity.SECURED);
        String protocol = "http://";
        //TODO: Реализовать работу через HTTPS
//        if (secured) {
//            protocol = "https://";
//        }
        try {
            socket = IO.socket(protocol + address +":3000");
            socket.connect();
            socket.emit("join", nickname);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        //Всё необходимое для RecyclerView
        messageList = new ArrayList<>();
        myRecyclerView = findViewById(R.id.messagelist);
        RecyclerView.LayoutManager myLayoutManager = new LinearLayoutManager(getApplicationContext());
        myRecyclerView.setLayoutManager(myLayoutManager);
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //Отправка сообщения
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!messageText.getText().toString().isEmpty()){
                    socket.emit("sendMessage", nickname, messageText.getText().toString());
                    messageText.setText("");
                }
            }
        });
        //Уведомление о подключении
        socket.on("userJoin", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String data = (String) args[0];
                        Toast.makeText(ChatBoxActivity.this,data+" has joined the chat", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        //Уведомление об отключении
        socket.on("userExit", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String data = (String) args[0];
                        Toast.makeText(ChatBoxActivity.this,data+" has left the chat", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        //Получение сообщения
        socket.on("message", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        try {
                            //TODO: Вывод системных ссобщений вместе с обычными
                            //Извлечение данных из JSON
                            String nickname = data.getString("username");
                            String message = data.getString("message");
//                            boolean system = data.getBoolean("system");

                            //Создаём сообщение
                            Message m = new Message(nickname,message);
//                            if (system) {
//                                bubble = R.layout.message_system;
//                            }
                            if (m.getNickname().equals(getNickname())) {
                                bubble = R.layout.message_sent;
                            }
                            //Добавляем сообщение в messageList (список сообщений)
                            messageList.add(m);

                            //Подключение messageList к chatBoxAdapter
                            chatBoxAdapter = new ChatBoxAdapter(messageList, bubble);

                            //Обновление chatBoxAdapter и прокрутка к последнему сообщению
                            chatBoxAdapter.notifyDataSetChanged();
                            myRecyclerView.scrollToPosition(messageList.size()-1);

                            //Подключение chatBoxAdapter к RecyclerView
                            myRecyclerView.setAdapter(chatBoxAdapter);

                            //Восстановление фона сообщения по умолчанию
                            bubble = R.layout.message_received;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.emit("exit", nickname);
        socket.disconnect();
  }
}
