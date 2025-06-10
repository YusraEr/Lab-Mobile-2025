package com.example.praktikum8;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.praktikum8.database.MappingHelper;
import com.example.praktikum8.database.StudentHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import androidx.appcompat.widget.SearchView;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvStudents;
    private StudentAdapter adapter;
    private StudentHelper studentHelper;
    private TextView noData;
    private SearchView searchView;

    private final int REQUEST_ADD = 100;
    private final int REQUEST_UPDATE = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Student List");
        }

        rvStudents = findViewById(R.id.rv_students);
        FloatingActionButton fabAdd = findViewById(R.id.fab_add);

        noData = findViewById(R.id.noData);

        rvStudents.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StudentAdapter(this);
        rvStudents.setAdapter(adapter);
        searchView = findViewById(R.id.search);

        studentHelper = StudentHelper.getInstance(getApplicationContext());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchStudents(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchStudents(newText);
                return true;
            }
        });

        fabAdd.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, FormActivity.class);
            startActivityForResult(intent, REQUEST_ADD);
        });

        loadStudents();
    }
    private void loadStudents() {
        new LoadStudentsAsync(this, students -> {
            if (students.isEmpty()) {
                adapter.setStudents(new ArrayList<>());
                noData.setVisibility(View.VISIBLE);
                showToast("No data available");
            } else {
                adapter.setStudents(students);
                noData.setVisibility(View.GONE);
            }
        }).execute();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ADD) {
            if (resultCode == FormActivity.RESULT_ADD) {
                showToast("Student added successfully");
                loadStudents();
            }
        } else if (requestCode == REQUEST_UPDATE) {
            if (resultCode == FormActivity.RESULT_UPDATE) {
                showToast("Student updated successfully");
                loadStudents();
            } else if (resultCode == FormActivity.RESULT_DELETE) {
                showToast("Student deleted successfully");
                loadStudents();
            }
        }
        loadStudents();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (studentHelper != null) {
            studentHelper.close();
        }
    }

    private static class LoadStudentsAsync {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadStudentsCallback> weakCallback;

        private LoadStudentsAsync(Context context, LoadStudentsCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        void execute() {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());

            executor.execute(() -> {
                Context context = weakContext.get();
                if (context != null) {
                    StudentHelper studentHelper = StudentHelper.getInstance(context);
                    studentHelper.open();

                    Cursor studentsCursor = studentHelper.queryAll();
                    ArrayList<Student> students = MappingHelper.mapCursorToArrayList(studentsCursor);

                    studentsCursor.close();

                    handler.post(() -> {
                        LoadStudentsCallback callback = weakCallback.get();
                        if (callback != null) {
                            callback.postExecute(students);
                        }
                    });
                }
            });
        }

    }
    private void searchStudents(String keyword) {
        new Thread(() -> {
            studentHelper.open();
            Cursor cursor = studentHelper.queryByTitle(keyword);
            ArrayList<Student> students = MappingHelper.mapCursorToArrayList(cursor);
            cursor.close();
            runOnUiThread(() -> adapter.setStudents(students));
        }).start();
    }
    interface LoadStudentsCallback {
        void postExecute(ArrayList<Student> students);
    }
}