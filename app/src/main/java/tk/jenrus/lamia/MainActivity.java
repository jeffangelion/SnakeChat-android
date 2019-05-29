package tk.jenrus.lamia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    private EditText nickname;
    private EditText address;
    private Switch secured;
    public static final String NICKNAME = "nickname";
    public static final String ADDRESS = "address";
    public static final String SECURED = "secured";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = findViewById(R.id.btnConnect);
        nickname = findViewById(R.id.nickname);
        address = findViewById(R.id.chatRoomAddress);
        secured = findViewById(R.id.secured);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((!nickname.getText().toString().isEmpty())&&(!address.getText().toString().isEmpty())){
                    Intent start  = new Intent(MainActivity.this, ChatActivity.class);
                    start.putExtra(NICKNAME,nickname.getText().toString());
                    start.putExtra(ADDRESS,address.getText().toString());
//                    start.putExtra(SECURED,secured.isChecked());
                    startActivity(start);
                }
            }
        });
    }
}
