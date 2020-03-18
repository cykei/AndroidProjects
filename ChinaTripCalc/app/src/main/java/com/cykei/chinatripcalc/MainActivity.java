package com.cykei.chinatripcalc;

import androidx.appcompat.app.AppCompatActivity;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private TextView textView_rate = null;
    private TextView textView_date =null;
    private EditText editText_rate =null;
    private Button button_set =null;
    private int myRate = 0;
    private int realRate=-1;

    private EditText editText_price=null;
    private TextView textView_calc =null;
    private Button button_calc =null;


    private EditText editText_people=null;
    private TextView textView_distribute =null;
    private Button button_distribute =null;
    float price =0;
    int people=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView_rate = (TextView)findViewById(R.id.rate);
        textView_date = (TextView)findViewById(R.id.date);
        editText_rate = (EditText) findViewById(R.id.editText_rate);
        button_set = (Button) findViewById(R.id.button_set_rate);

        editText_price = (EditText) findViewById(R.id.editText_price);
        textView_calc = (TextView) findViewById(R.id.textView_calc);
        button_calc = (Button) findViewById(R.id.button_calc);

        editText_people = (EditText) findViewById(R.id.editText_people);
        textView_distribute = (TextView) findViewById(R.id.textView_distribute);
        button_distribute = (Button) findViewById(R.id.button_distribute);

        //간단한 정보 저장
        final SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        myRate = pref.getInt("myRate",0);
        editText_rate.setText(String.valueOf(myRate));

        String resultText = "[{\"date\" : \"날짜\", \"rate\" : -1}]";

        //환율 구하기

        if(NetworkStatus.getConnectivityStatus(this)==3) {
            textView_date.setText("날짜");
            textView_rate.setText("인터넷 연결해주세요");
        } else {
            try {
                resultText = new ExchangeTask().execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            // [{}] 형태라서 앞,뒤 한글자 씩 자름
            resultText = resultText.substring(1,resultText.length()-1);

            // json 파싱
            Pair<String, Integer> exchangeInfo = jsonParser(resultText);
            Log.d("exhangeInfo : ", exchangeInfo.first + "," + exchangeInfo.second);

            // 화면에 뿌리기
            textView_date.setText(""+exchangeInfo.first);
            textView_rate.setText(""+exchangeInfo.second +" 원");
            realRate = exchangeInfo.second;
        }

        // 직접설정
        button_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pRate= Integer.parseInt(editText_rate.getText().toString());
                myRate = pRate;

                //db에 저장
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("myRate",myRate);
                editor.commit();

            }
        });

        //물건값 계산하기
        button_calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    float result = 0;
                    String sPrice = editText_price.getText().toString();
                    if(sPrice.getBytes().length <= 0 ) {
                        Toast.makeText(getApplicationContext(), "물건 값을 입력하세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        price = Float.parseFloat(editText_price.getText().toString());
                    }

                    if (myRate != 0) { //환율 직접 입력했으면
                        result = price * myRate;
                        textView_calc.setText("한국 돈으로 " + result + "원 정도 군요.");
                    } else if (realRate != -1) { //환율 직접 입력 안했으면
                        result = price * realRate;
                        textView_calc.setText("한국 돈으로 " + result + "원 정도 군요.");
                    } else {
                        textView_calc.setText("인터넷 연결 또는 환율을 직접 입력해주세요.");
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "에러발생", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //더치페이 계산하기
        button_distribute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String sPrice = editText_price.getText().toString();
                    String sPeople = editText_people.getText().toString();

                    // 스페이스바만 쓰는거 방지
                    sPrice = sPrice.trim();
                    sPeople = sPeople.trim();

                    // 아무것도 안적었을때
                    if (sPeople.getBytes().length <= 0) {
                        Toast.makeText(getApplicationContext(), "값을 입력하세요.", Toast.LENGTH_SHORT).show();
                    } else if(sPrice.getBytes().length <= 0 ) {
                        Toast.makeText(getApplicationContext(), "물건 값을 입력하세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        price = Float.parseFloat(sPrice);
                        people = Integer.parseInt(sPeople);
                    }

                    // 뭔가 적었으면
                    float result;
                    if (price > 0) {
                        if (people > 1) {
                            result = price / people;
                            textView_distribute.setText("한명 당 " + result + "위안 씩 내시면 됩니다.");
                        } else {
                            textView_distribute.setText("숫자 2 이상을 입력해주세요.");
                        }
                    } else {
                        textView_distribute.setText("물건값을 제대로 입력해주세요.");
                    }
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(), "에러발생", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public Pair<String, Integer> jsonParser(String jsonString){

        String date = null;
        Integer rate = null;

        try{

            JSONObject jsonObject = new JSONObject(jsonString);
            date = jsonObject.optString("date");
            rate = jsonObject.optInt("rate");
        }catch(JSONException e){
            e.getStackTrace();
        }

        Pair<String, Integer> result = new Pair(date, rate);

        return result;
    }
}
