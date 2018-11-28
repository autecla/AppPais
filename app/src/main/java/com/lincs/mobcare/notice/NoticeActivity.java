package com.lincs.mobcare.notice;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.lincs.mobcare.R;
import com.lincs.mobcare.models.Aviso;
import com.lincs.mobcare.utils.Snapshot;

import java.util.ArrayList;
import java.util.Objects;


public class NoticeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        Toolbar toolbar = findViewById(R.id.toolbar);

        TextView title = findViewById(R.id.toolbar_title);
        title.setText(R.string.activity_notice);

        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final RecyclerView rvNotices =findViewById(R.id.rvNotices);

        final Context context = this;

        ArrayList<Aviso> notices = new ArrayList<>(Snapshot.getAviso().values());
        NoticeExpandedAdapter adapter = new NoticeExpandedAdapter(notices, context);

        rvNotices.setAdapter(adapter);
        rvNotices.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}