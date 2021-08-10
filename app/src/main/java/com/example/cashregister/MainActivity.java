package com.example.cashregister;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.lang.*;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private String price="";   // price showing on screen
    private String typeClass=""; // number meaning on screen

    private Boolean displayToken=true; // check if view should be cleared
    private Boolean refund=false;

    private double priceCount=0; // buffer price for current item
    private double totalPrice=0; // total price for a bill
    private double changeAmount=0; // amount to give back to customer

    private double tpsAmount=0; // tps tax
    private double tvqAmount=0; // tvq tax

    private int times=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void displayScreen(String price,String type) {
        TextView display=findViewById(R.id.Display);
        display.setText(price+" "+type);
    }

    public void setRefundMode(View v) {
        this.refund=true;
    }

    public void setTypeClass(String typeClass) {
        if(this.refund) {
            this.typeClass="-"+typeClass;
        }
        else {
            this.typeClass=typeClass;
        }
    }

    // **************************** for number pins ***********************************
    public void enterPin(View v) {
       if(!displayToken) {
            this.clearBufferPrice();
            displayToken=true;
       }
        this.setPin(v);
    }

    public void setPin(View v) {
        Button button = findViewById(v.getId());
        this.price += button.getText().toString();
        this.displayScreen(this.price,this.typeClass);
    }

    public void setPoint(View v) {
        this.price+=".";
        this.displayScreen(this.price,this.typeClass);
    }

    public void setDecimal(View view) {
        this.price+=".00";
        this.displayScreen(this.price,this.typeClass);
    }

    // **************************** clearing ***********************************
    // erase display view
    public void clearBufferPrice() {
        this.priceCount=0; // set buffer price to 0
        this.price=""; // reset display price
        this.setTypeClass(""); // reset item type
    }

    // reset transaction completely
    public void clearAll(View v) {
        this.totalPrice=0; // set total price to 0
        this.clearBufferPrice();
        this.displayScreen(this.price,this.typeClass); // show nothing on display
    }


    // **************************** calculation ***********************************

    public void modifyTotal() {
        if(this.refund) {
            this.totalPrice += (this.priceCount*-1);
            this.refund=false;
        }
        else {
            this.totalPrice +=this.priceCount;
        }
        this.times=1; // changed
    }


    // calculate tax item
    public void calculateTax(View v) {
        this.priceCount=Double.parseDouble(this.price); // convert display text to number

        // count taxes and set to total price
        this.countTps();
        this.countTvq();
        this.priceCount+=this.tpsAmount+this.tvqAmount;
        this.priceCount*=this.times; // changed
        this.setTypeClass("tx");
        this.modifyTotal();

        // display current item price
        this.price=Double.toString(this.priceCount);  // repeat

        this.displayScreen(this.price,this.typeClass);
        this.clearBufferPrice();
        this.displayToken=false;
    }
    public void countTps() {
        this.tpsAmount=this.priceCount*0.05;
    }
    public void countTvq() {
        this.tvqAmount=this.priceCount*.09975;
    }

    // calculate no tax item
    public void setNoTaxPrice(View view) {
        this.priceCount=Double.parseDouble(this.price);
        this.priceCount*=this.times; // changed
        Button button=findViewById(view.getId());
        if(button.getText().toString().equals("Loto")) {
            this.setTypeClass("loto");
        }
        else {
            this.setTypeClass("pas tx");
        }
        this.modifyTotal();
        this.price=Double.toString(this.priceCount); // repeat
        this.displayScreen(this.price,this.typeClass);
        this.clearBufferPrice();
        this.displayToken=false;
    }

    // calculate Total for a transaction
    public void getTotalPrice(View view) {
        this.totalPrice=(Math.round(this.totalPrice*100));
        this.totalPrice/=100;
        String total=Double.toString(this.totalPrice);

        this.setTypeClass("total");

        this.displayScreen(total,this.typeClass);
        this.clearBufferPrice();
    }

    public void setCigaret(View v) {
        this.priceCount=Double.parseDouble(this.price);
        this.countTps();
        this.priceCount+=this.tpsAmount;
        this.priceCount*=this.times;
        this.setTypeClass("cig");
        this.modifyTotal();
        this.price=Double.toString(this.priceCount); // repeat

        this.displayScreen(this.price,this.typeClass);
        this.clearBufferPrice();
        this.displayToken=false;
    }

    public void cashBack(View v) {
        TextView display=findViewById(R.id.Display);
        double paidAmount=Double.parseDouble(display.getText().toString());
        this.changeAmount=paidAmount-this.totalPrice;
        this.changeAmount=Math.round(this.changeAmount*100);
        this.changeAmount/=100;
        if(changeAmount<0) {
            this.changeAmount*=-1;
            this.setTypeClass("missing");
            this.totalPrice=this.changeAmount;
        }
        else {
            this.setTypeClass("return");
        }
        this.displayScreen(Double.toString(this.changeAmount),this.typeClass);
        this.displayToken=false;
    }

    public void setTimes(View v) {
        TextView display=findViewById(R.id.Display);
        Double a=Double.parseDouble(display.getText().toString());
        this.times=a.intValue();
        Log.d("succ",String.valueOf(this.times));
        this.clearBufferPrice();
    }


}