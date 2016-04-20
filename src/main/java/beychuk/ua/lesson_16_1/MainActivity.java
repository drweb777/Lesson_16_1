package beychuk.ua.lesson_16_1;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

/**
 * Тема: Многотабличные БД
 */
public class MainActivity extends AppCompatActivity
{
    private MySQLiteOpenHelper dbHelper;
    private ResourceCursorAdapter RCA;
    private ResourceCursorAdapter rcaFaculties;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //--- Создаем Helper -------------------------------------------------------
        dbHelper = new MySQLiteOpenHelper(this);
        //--- Подключаемся к БД ----------------------------------------------------
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //--- Делаем выборку из таблиц ---------------------------------------------
        Cursor cursor = db.rawQuery("SELECT groups._id AS _id, " +
                                    "groups.name AS gname, " +
                                    "faculties.name AS fname " +
                                    "FROM groups, faculties " +
                                    "WHERE groups.id_faculty = faculties._id", null);
        //--- Создаем адаптер ------------------------------------------------------
        RCA = new ResourceCursorAdapter(this, R.layout.group_item, cursor, false)
        {
            //--- Переопределяем метод ---------------------------------------------
            @Override
            public void bindView(View view, Context context, Cursor cursor)
            {
                //--- Находим виджеты ----------------------------------------------
                TextView tvGName = (TextView)view.findViewById(R.id.tvGName);
                TextView tvFName = (TextView)view.findViewById(R.id.tvFName);
                //--- ID столбцов (НО ПРОЩЕ НАЙТИ ИХ ОДИН РАЗ В onCreate()) --------
                int indexGName = cursor.getColumnIndex("gname");
                int indexFName = cursor.getColumnIndex("fname");
                //--- Заполняем виджеты данными ------------------------------------
                tvGName.setText(cursor.getString(indexGName));
                tvFName.setText(cursor.getString(indexFName));
            }
        };
        //--- Получаем ссылку на список ----------------------------------------------
        ListView lvGroups = (ListView)findViewById(R.id.lvGroups);
        //--- Назначаем адаптер ------------------------------------------------------
        lvGroups.setAdapter(RCA);

        //============================================================================

        //--- Делаем выборку данных --------------------------------------------------
        Cursor cFaculties = db.query("Faculties", null, null, null, null, null, null);
        //--- Инициализируем адаптер для спиннера ------------------------------------
        rcaFaculties = new ResourceCursorAdapter(this, android.R.layout.simple_spinner_item, cFaculties, false)
        {
            @Override
            public void bindView(View view, Context context, Cursor cursor)
            {
                //--- Находим виджет в стандартном макете -------------------------------
                TextView tv = (TextView)view.findViewById(android.R.id.text1);
                //--- Заполняем данными -------------------------------------------------
                tv.setText(cursor.getString(cursor.getColumnIndex("name")));
                //=== !!! ДОБАВЛЯЕМ ЭТОТ АДАПТЕР СПИНЕРУ В ДИАЛОГОВОМ ОКНЕ =======================
            }
        };
        rcaFaculties.setDropDownViewResource(android.R.layout.simple_spinner_item);
    }

    /**
     * Метод обработки кнопок
     */
    public void btnClick(View view)
    {
        switch (view.getId())
        {
            //G
            case R.id.btnAddG:
                break;
            case R.id.btnUpdateG:
                break;
            case R.id.btnDeleteG:
                break;
            //F
            case R.id.btnAddF:
                break;
            case R.id.btnUpdateF:
                break;
            case R.id.btnDeleteF:
                break;
        }
    }
}

class MySQLiteOpenHelper extends SQLiteOpenHelper
{

    public MySQLiteOpenHelper(Context context)
    {
        super(context, "ThirdDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //--- Создание таблицы "Факультет" ---------------------
        String queryFacult = "CREATE TABLE Faculties(" +
                "_id integer not null primary key autoincrement," +
                "name text)";
        db.execSQL(queryFacult);
        //--- Добавляем факультеты -----------------------
        db.execSQL("INSERT INTO Faculties VALUES (1, 'Программирования')");
        db.execSQL("INSERT INTO Faculties VALUES (2, 'Веб-дизайна')");
        db.execSQL("INSERT INTO Faculties VALUES (3, 'Администрирования')");
        //====================================================================
        //--- Создание таблицы "Группы" ---------------------
        String queryGroups = "CREATE TABLE Groups(" +
                "_id integer not null primary key," +
                "name text," +
                "id_faculty integer)";
        db.execSQL(queryGroups);
        //--- Добавляем группы -----------------------
        db.execSQL("INSERT INTO Groups VALUES (2,'9П1', 1)");
        db.execSQL("INSERT INTO Groups VALUES (3,'9П2', 1)");
        db.execSQL("INSERT INTO Groups VALUES (4,'9А', 3)");
        db.execSQL("INSERT INTO Groups VALUES (5,'9Д', 2)");
        db.execSQL("INSERT INTO Groups VALUES (6,'14А', 3)");
        db.execSQL("INSERT INTO Groups VALUES (7,'19П1', 1)");
        db.execSQL("INSERT INTO Groups VALUES (8,'18П2', 1)");
        db.execSQL("INSERT INTO Groups VALUES (9,'18А', 3)");
        db.execSQL("INSERT INTO Groups VALUES (10,'19Д', 2)");
    }

//    @Override
//    public void onOpen(SQLiteDatabase db)
//    {
//        db.execSQL("DROP TABLE IF EXISTS Tovars");
//        String query = "CREATE TABLE Tovars(" +
//                "_id integer primary key autoincrement," +
//                "name varchar(64)," +
//                "price double," +
//                "weight integer)";
//        db.execSQL(query);
//
//    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}
