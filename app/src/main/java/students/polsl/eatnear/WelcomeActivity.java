package students.polsl.eatnear;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {
    private Button mEnterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mEnterButton = findViewById(R.id.enter_button);

        mEnterButton.setOnClickListener(view -> {
            Intent startMainActivity = new Intent(this, MainActivity.class);
            startActivity(startMainActivity);
        });
    }
}
