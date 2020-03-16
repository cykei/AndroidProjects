package com.cykei.birdview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager

import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    val BASE_URL = "https://6uqljnm1pb.execute-api.ap-northeast-2.amazonaws.com/prod/"

   // var itemList = arrayListOf<Item>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadData()


    }

    private fun setAdapter(itemList:ArrayList<Item>){
        val mAdapter = ItemAdapter(this,itemList)
        mRecyclerView.adapter=mAdapter
        mRecyclerView.layoutManager = GridLayoutManager(this,2)
        mRecyclerView.setHasFixedSize(true)

    }


    private fun loadData(){
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitService = retrofit.create(RetrofitInterface::class.java)

        retrofitService.requestAllProducts().enqueue(object:Callback<ItemModel>{
            override fun onResponse(
                call: Call<ItemModel>,
                response: retrofit2.Response<ItemModel>
            ) {
                if(response.isSuccessful){
                    val body = response.body()
                    Log.d("데이타 확인: ", body.toString())
                    body?.let{
                        Log.d("데이타 확인: " , body.toString() )
                        try{
                            //val tv:TextView = findViewById(R.id.textView)
                            //tv.text = body.toString()
                        }catch(e:Exception){
                            Log.d("에러","에러")
                        }
                        setAdapter(it.body)
                    }
                }
            }

            override fun onFailure(call: Call<ItemModel>, t: Throwable) {
                Log.d("에러발생", t.message)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu?.findItem(R.id.menu_search)
        if(searchItem!=null){
            val searchView = searchItem.actionView as SearchView
            searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    //mAdapter!!.filter.filter(query)
                    Toast.makeText(applicationContext,"검색완료",Toast.LENGTH_SHORT).show()
                    return false
                }

                override fun onQueryTextChange(query: String?): Boolean {
                    //mAdapter!!.filter.filter(query)
                    Toast.makeText(applicationContext,"검색중",Toast.LENGTH_SHORT).show()

                    return false
                }
            })
            return true

            /*
            searchView.setOnQueryTextFocusChangeListener(View.OnFocusChangeListener listener : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }

            })

            */


        }
        return super.onCreateOptionsMenu(menu)
    }

}
