package com.example.currencyconverterwithretrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Button mConvert;
    EditText mCurrencyToConvert;
    EditText mConvertedCurrency;
    Spinner mConvertToList;
    Spinner mConvertFromList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //String[] dropDownList = {"USD", "INR","EUR","NZD","GBP"};

        mConvert = (Button) findViewById(R.id.button);
        mCurrencyToConvert = (EditText) findViewById(R.id.currency_to_be_converted);
        mConvertedCurrency = (EditText) findViewById(R.id.currency_converted);
        mConvertFromList = (Spinner) findViewById(R.id.convert_from);
        mConvertToList = (Spinner) findViewById(R.id.convert_to);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.currencies, android.R.layout.simple_spinner_dropdown_item);
        mConvertFromList.setAdapter(adapter);
        mConvertToList.setAdapter(adapter);

        convert();

    }

    public void convert() {
        mConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrofitInterface retrofitInterface = RetrofitBuilder.getRetrofitInstance().create(RetrofitInterface.class);
                Call<JsonObject> call = retrofitInterface.getExchangeCurrency(mConvertFromList.getSelectedItem().toString());
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        JsonObject res = response.body();
                        JsonObject rates = res.getAsJsonObject("rates");
                        Double currency = Double.valueOf(mCurrencyToConvert.getText().toString());
                        Double convertedAmount = Double.valueOf(rates.get(mConvertToList.getSelectedItem().toString()).toString());
                        Double finalConversationAmount = currency * convertedAmount;

                        mConvertedCurrency.setText(finalConversationAmount.toString());

                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });
            }
        });
    }
}