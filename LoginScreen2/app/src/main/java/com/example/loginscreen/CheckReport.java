package com.example.loginscreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CheckReport extends AppCompatActivity {

    private List<Chat> ReportList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FloatingActionButton BackFab;

    public void populateReports(RecyclerView ChatView){
        ReportList = new ArrayList<>();
        db.collection("Reports").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Chat> chats = new ArrayList<>();
                List<DocumentSnapshot> D = task.getResult().getDocuments();
                for(DocumentSnapshot d: D){
                    if (!d.getId().equals("Null Report")){
                        String s = d.getId();
                        ReportList.add(new Chat(s,s));
                    }
                }
                ChatListAdapter adapter = new ChatListAdapter(ReportList, chat -> {
                    Intent intent = new Intent(CheckReport.this, ChatActivity.class);
                    intent.putExtra("Reported", chat.getChatName());
                    startActivity(intent);
                });
                ChatView.setAdapter(adapter);
            }
        });
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_report);
        RecyclerView ReportListRecyclerView = findViewById(R.id.report_list_recycler_view);
        ReportListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        populateReports(ReportListRecyclerView);
        BackFab = findViewById(R.id.BackFABReport);
        BackFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckReport.this, SelectChat.class);
                startActivity(intent);
            }
        });

    }

}
