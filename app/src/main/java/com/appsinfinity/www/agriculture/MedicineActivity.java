package com.appsinfinity.www.agriculture;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class MedicineActivity extends AppCompatActivity {

    String medicine, percentage;

    TextView t1, t2, t3, t4, t5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        medicine = getIntent().getStringExtra("medicine");
        percentage = getIntent().getStringExtra("percentage");

        t1 = findViewById(R.id.txt_first);
        t2 = findViewById(R.id.txt_second);
        t3 = findViewById(R.id.txt_third);
        t4 = findViewById(R.id.txt_four);
        t5 = findViewById(R.id.txt_five);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String msgBody = "Report \n\n"+t1.getText().toString()+"\n\n"+t2.getText().toString()+"\n\n"+t3.getText().toString()+"\n\n"+t4.getText().toString();
                final ProgressDialog progressView = new ProgressDialog(
                        MedicineActivity.this
                );
                progressView.setMessage("Creating Sharable Report..");
                progressView.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                progressView.setCancelable(false);
                progressView.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressView.dismiss();
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "REPORT");
                        sharingIntent.putExtra(Intent.EXTRA_TEXT, msgBody);
                        startActivity(sharingIntent);

                    }
                }, 3000);
            }
        });

        initialize();
    }

    private void initialize() {
        if(medicine.equals("1")){
            t1.setText("Disease: Bacterial Leaf Streak");
            t2.setText("Sun Light Condition: "+getSunLight());
            t3.setText("Growth Prediction: "+getGrowth());
            t4.setText("Fertilizer: Apply copper based fungicide on leaves can be effective in controling the disease.");
            t5.setText("Pesticide: Epsom Salt Pesticide\n" +
                    "\n" +
                    "Epsom salt has many great uses in the garden – one of which is as a pest deterrent and pesticide.\n" +
                    "\n" +
                    "Spraying and sprinkling are two of the most effective ways of using these magnesium rich salts to combat bugs.\n" +
                    "\n" +
                    "To make an Epsom salt spray, dissolve one cup of the salts (available from here) in five gallons of water. Decant into a spray bottle and apply to the affected plants");
        } else if (medicine .equals("2")){
            t1.setText("Disease: Brown Spot");
            t2.setText("Sun Light Condition: "+getSunLight());
            t3.setText("Growth Prediction: "+ getGrowth());
            t4.setText("Fertilizer: Spraying with a baking soda solution (a tablespoon of baking soda, 2 1/2 tablespoons of vegetable oil, a teaspoon of liquid soap, not detergent, to one gallon of water), or neem oil (do not use when pollinating insects including bees or other beneficial insects are present). Baking soda may burn some plant leaves. Spray only a few and then check for a reaction before applying applications every two weeks.");
            t5.setText("Pesticide: Citrus Insecticide\n" +
                    "\n" +
                    "This fresh-scented lemon pesticide is especially useful if your garden is inundated with brown spots.\n" +
                    "\n" +
                    "To make, simply bring a pint of water to the boil. Meanwhile, grate the rind from one lemon. Once the water is boiling, remove it from the heat and add the lemon rind. Allow to steep overnight before straining the liquid through a cheesecloth or fine mesh sieve. Pour this clear liquid into a spray bottle and apply to the top and underside of the leaves of the affected plant.\n");
        } else if (medicine.equals("3")){
            t1.setText("Disease: Black Horse Riding");
            t2.setText("Sun Light Condition: "+getSunLight());
            t3.setText("Growth Prediction: "+getGrowth());
            t4.setText("Fertilizer: You can use a chemical fungicide or any number of organic options such as: Copper, Lime Sulfur, Neem Oil, Potassium or Ammonium Bicarbonate, Sulfur.");
            t5.setText("Pesticide: Essential Oil Blends\n" +
                    "\n" +
                    "Essential oils can be put to great use in the garden. They work to do everything from attracting pollinators to suppressing fungus.\n" +
                    "\n" +
                    "Mix a teaspoon of vodka with 10 drops of lemon essential oil, 10 drops of eucalyptus oil, 10 drops of cedarwood oil and an ounce of water. Add to a glass spray bottle and apply thoroughly, shaking before each use.");
        } else if (medicine.equals("4")){
            t1.setText("No disease found.");
            t2.setText("Sun Light Condition: GOOD.");
            t3.setText("Growth Prediction: Normal growth will occur. (3 months)");
            t4.setText("Fertilizer: No Need");
            t5.setText("Pesticide: No Need.");
        }
    }

    private String getSunLight(){
        if(Float.parseFloat(percentage) > 5.00){
            return "NOT GOOD";
        }

        return "GOOD";
    }

    private String getGrowth(){
        if(Float.parseFloat(percentage) < 1.00){
            return "4 Months (Better)";
        } else if(Float.parseFloat(percentage) > 2.00){
            return "5 Months (Not Good)";
        } if(Float.parseFloat(percentage) > 5.00){
            return "Won't Grow. Better apply fertilizer";
        }
        return "4 Months (Better)";
    }

}
