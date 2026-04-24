package com.example.hw_;

import android.os.Bundle;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.*;
import androidx.recyclerview.widget.*;
import androidx.room.*;

import java.util.*;

// ======================== ROOM КЛАСИ ========================

@Entity(tableName = "students")
class Student {
    @PrimaryKey(autoGenerate = true)
    public long _id;

    public String firstName;
    public String lastName;
    public int age;

    public Student() {}

    public Student(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    @NonNull
    @Override
    public String toString() {
        return firstName + " " + lastName + ", вік: " + age;
    }
}

@Dao
interface StudentDao {
    @Insert
    void insert(Student student);

    @Delete
    void delete(Student student);

    @Update
    void update(Student student);

    @Query("SELECT * FROM students ORDER BY lastName")
    LiveData<List<Student>> getAllStudents();

    @Query("DELETE FROM students")
    void deleteAll();
}

@Database(entities = {Student.class}, version = 1, exportSchema = false)
abstract class AppDatabase extends RoomDatabase {
    abstract StudentDao studentDao();

    private static volatile AppDatabase INSTANCE;

    static AppDatabase getDatabase(final android.content.Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "student_database"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}

class StudentRepository {
    private final StudentDao dao;
    private final LiveData<List<Student>> allStudents;

    StudentRepository(android.app.Application app) {
        AppDatabase db = AppDatabase.getDatabase(app);
        dao = db.studentDao();
        allStudents = dao.getAllStudents();
    }

    LiveData<List<Student>> getAllStudents() {
        return allStudents;
    }

    void insert(Student s) {
        new Thread(() -> dao.insert(s)).start();
    }

    void delete(Student s) {
        new Thread(() -> dao.delete(s)).start();
    }

    void update(Student s) {
        new Thread(() -> dao.update(s)).start();
    }

    void deleteAll() {
        new Thread(dao::deleteAll).start();
    }
}

// ======================== ADAPTER ========================

class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.VH> {
    private List<Student> data = new ArrayList<>();

    interface OnStudentClickListener {
        void onStudentClick(Student student);
    }

    private OnStudentClickListener listener;

    void setOnStudentClickListener(OnStudentClickListener listener) {
        this.listener = listener;
    }

    void setStudents(List<Student> list) {
        data = list;
        notifyDataSetChanged();
    }

    Student getStudentAt(int position) {
        return data.get(position);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull android.view.ViewGroup parent, int viewType) {
        android.view.View v = android.view.LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int pos) {
        Student student = data.get(pos);
        holder.tv.setText(student.toString());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onStudentClick(student);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tv;

        VH(android.view.View v) {
            super(v);
            tv = v.findViewById(android.R.id.text1);
        }
    }
}

// ======================== MAIN ACTIVITY ========================

public class MainActivity extends AppCompatActivity {

    public static class StudentViewModel extends AndroidViewModel {
        private final StudentRepository repo;
        private final LiveData<List<Student>> allStudents;

        public StudentViewModel(android.app.Application app) {
            super(app);
            repo = new StudentRepository(app);
            allStudents = repo.getAllStudents();
        }

        public LiveData<List<Student>> getAllStudents() {
            return allStudents;
        }

        public void insert(Student s) {
            repo.insert(s);
        }

        public void delete(Student s) {
            repo.delete(s);
        }

        public void update(Student s) {
            repo.update(s);
        }

        public void deleteAll() {
            repo.deleteAll();
        }
    }

    private StudentViewModel viewModel;
    private final StudentAdapter adapter = new StudentAdapter();

    private Student selectedStudent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText etFirstName = findViewById(R.id.etFirstName);
        EditText etLastName  = findViewById(R.id.etLastName);
        EditText etAge       = findViewById(R.id.etAge);
        Button btnAdd        = findViewById(R.id.btnAdd);
        Button btnClear      = findViewById(R.id.btnClear);
        RecyclerView rv      = findViewById(R.id.recyclerView);

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        viewModel = new ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())
        ).get(StudentViewModel.class);

        viewModel.getAllStudents().observe(this, adapter::setStudents);

        // Клік по студенту — редагування
        adapter.setOnStudentClickListener(student -> {
            selectedStudent = student;

            etFirstName.setText(student.firstName);
            etLastName.setText(student.lastName);
            etAge.setText(String.valueOf(student.age));

            btnAdd.setText("Зберегти");
        });

        // Додавання або збереження змін
        btnAdd.setOnClickListener(v -> {
            String fn = etFirstName.getText().toString().trim();
            String ln = etLastName.getText().toString().trim();
            String ageStr = etAge.getText().toString().trim();

            if (fn.isEmpty() || ln.isEmpty() || ageStr.isEmpty()) {
                Toast.makeText(this, "Заповніть всі поля", Toast.LENGTH_SHORT).show();
                return;
            }

            int age = Integer.parseInt(ageStr);

            if (selectedStudent == null) {
                viewModel.insert(new Student(fn, ln, age));
                Toast.makeText(this, "Студента додано", Toast.LENGTH_SHORT).show();
            } else {
                selectedStudent.firstName = fn;
                selectedStudent.lastName = ln;
                selectedStudent.age = age;

                viewModel.update(selectedStudent);

                selectedStudent = null;
                btnAdd.setText("Додати");

                Toast.makeText(this, "Дані студента оновлено", Toast.LENGTH_SHORT).show();
            }

            etFirstName.setText("");
            etLastName.setText("");
            etAge.setText("");
        });

        // Очистити всіх студентів
        btnClear.setOnClickListener(v -> {
            viewModel.deleteAll();
            selectedStudent = null;
            btnAdd.setText("Додати");

            etFirstName.setText("");
            etLastName.setText("");
            etAge.setText("");

            Toast.makeText(this, "Список очищено", Toast.LENGTH_SHORT).show();
        });

        // Свайп вліво або вправо — видалення одного студента
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(
                        0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
                ) {
                    @Override
                    public boolean onMove(
                            @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            @NonNull RecyclerView.ViewHolder target
                    ) {
                        return false;
                    }

                    @Override
                    public void onSwiped(
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            int direction
                    ) {
                        int position = viewHolder.getAdapterPosition();
                        Student student = adapter.getStudentAt(position);

                        viewModel.delete(student);

                        if (selectedStudent != null && selectedStudent._id == student._id) {
                            selectedStudent = null;
                            btnAdd.setText("Додати");

                            etFirstName.setText("");
                            etLastName.setText("");
                            etAge.setText("");
                        }

                        Toast.makeText(MainActivity.this, "Студента видалено", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        itemTouchHelper.attachToRecyclerView(rv);
    }
}