package org.ql.chess;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.litepal.crud.DataSupport;
import org.ql.chess.db.Role;
import org.ql.chess.db.Role_Score;
import org.ql.chess.db.Score;

import java.util.List;
import java.util.function.Function;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, FunctionActivity.class);
        startActivity(intent);
        finish();
    }
}
