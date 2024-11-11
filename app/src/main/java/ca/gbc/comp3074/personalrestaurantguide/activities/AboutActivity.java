package ca.gbc.comp3074.personalrestaurantguide.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ca.gbc.comp3074.personalrestaurantguide.R;

public class AboutActivity extends AppCompatActivity {

    private TextView teamMembersTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        teamMembersTextView = findViewById(R.id.teamMembersTextView);


        String teamMembers = "Members:\n\n" +
                "1. Ali Cebe\n" +
                "2. Will\n" +
                "3. Ryan\n" +
                "4. Rathish";

        teamMembersTextView.setText(teamMembers);
    }
}
