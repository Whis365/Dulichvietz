package com.viettravel.dulichviet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerview,recyclerview2;

    public SQLiteDatabase database;
    public String DATABASE_NAME ="dulichviets.sqlite" ;
    List<DiaDanh_Modles> listDiaDanh_noitieng,listDiaDanh_phobien;
    public String biendao = "SELECT * FROM diadanh_biendao";
    public String noitieng = "SELECT * FROM diadanh_noitieng";
    EditText Search;
    ImageButton btnSearch;
    public RelativeLayout xemtatca;
//    ViewFlipper flipper;

//    public String DDanh[] = {"Hồ Núi Cốc","Bà Nà","Bãi Dài","Biển Bình Tiên","Côn Đảo","Cù Lao Chàm","Cù Lao Xanh","Đảo Phú Quốc","Đồng Tháp","Vịnh Hạ Long","Vũng Tàu","Vịnh Lan Hạ","Phong Nha-Kẻ Bàng","Cao nguyên đá Đồng Văn","Hang SonDoong","Ghềnh đá dĩa","Làng chài An Bằng","Thác Bản Giốc","Đèo Hải Vân","Mù Cang Chải","Mộc Châu","Đà Lạt","Sa Pa","Kinh Đô Huế","Tràng An","Phố cổ Hội An","Cáp treo Vinpearl Nha Trang","Nha Trang"};

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            Search = findViewById(R.id.editext_search);
            btnSearch = findViewById(R.id.btnsearch);
            recyclerview = findViewById(R.id.recyclerview1);
            recyclerview2 = findViewById(R.id.recyclerview2);
//            flipper = findViewById(R.id.flipper);

            xemtatca = findViewById(R.id.xemtatca);

            Funtion_Search();
            ListNoiTieng(noitieng);
            ListBiendao(biendao);
            xemAll();

//            int image[] = {R.drawable.bana1,R.drawable.bana,R.drawable.banner1};
//            for (int i = 0 ; i < image.length ; i++)
//            {
//                flip_image(image[i]);
//            }

        }
//    private void flip_image(int s) {
//
//        ImageView imageView = new ImageView(this);
//        imageView.setBackgroundResource(s);
//        flipper.addView(imageView);
//        flipper.setFlipInterval(4000);
//        flipper.setAutoStart(true);
//
//        flipper.setInAnimation(this,android.R.anim.slide_in_left);
//        flipper.setOutAnimation(this,android.R.anim.slide_out_right);
//
//    }

    public void xemAll()
        {
            xemtatca.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this,Xemtatca.class);
                    startActivity(intent);
                }
            });
        }
        private void Funtion_Search()
        {
            btnSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id =-1 ;
                    String search = Search.getText().toString().trim();

                    database = Database.initDatabase(MainActivity.this,"dulichviets.sqlite");

                    if (!search.equalsIgnoreCase("")) {
                        Cursor cursor = database.rawQuery("Select id from  chitietdiadanh where ten like '%" + search + "%'", null);
                        cursor.moveToFirst();

                        if (cursor.moveToFirst()==true)
                        {
                            id = cursor.getInt(0);
                            Log.e("search", String.valueOf(id));
                        }
                        else id = -1;
                    }
//                id = getIndexOf(search,DDanh);
                    if (id!=-1)
                    {
                        Intent intent = new Intent(MainActivity.this, Chitietdiadanh.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("id",id);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(MainActivity.this,"Không hợp lệ vui lòng nhập lại!",Toast.LENGTH_LONG).show();
                    }
                    database.close();
                }
            });
        }


        public void ListNoiTieng (String noitieng){
            listDiaDanh_noitieng = new ArrayList<>();
            loadData(noitieng, listDiaDanh_noitieng);
            LinearLayoutManager layoutManager2 = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
            recyclerview.setLayoutManager(layoutManager2);
            recyclerview.setHasFixedSize(true);
            recyclerview.setAdapter(new RecyclerDataAdapter(this, listDiaDanh_noitieng));
        }
        public void ListBiendao (String biendao){
            listDiaDanh_phobien = new ArrayList<>();
            loadData(biendao, listDiaDanh_phobien);
            LinearLayoutManager _layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
            recyclerview2.setLayoutManager(_layoutManager);
            recyclerview2.setHasFixedSize(true);
            recyclerview2.setAdapter(new RecyclerDataAdapter(this, listDiaDanh_phobien));
        }


    public void loadData (String str, List < DiaDanh_Modles > listDiaDanh){

        database = Database.initDatabase(this,DATABASE_NAME);
        Cursor cursor = database.rawQuery(str, null);
        while (cursor.moveToNext()) {
            listDiaDanh.add(new DiaDanh_Modles(
                    cursor.getInt(0),
                    cursor.getBlob(2),
                    cursor.getString(1)
            ));
        }
        database.close();
        cursor.close();
    }
}

