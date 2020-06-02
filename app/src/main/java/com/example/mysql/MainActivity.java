//Author: Mordy dabah
package com.example.mysql;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String NAME_REGEX = "^[a-zA-Z]+";
    private static final String CITY_REGEX = "^[a-zA-Z ]+";
    private CustomerHelper customerHelper;
    private Button addCustomer, showCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customerHelper = new CustomerHelper(this);
        customerHelper.open();

        addCustomer = findViewById(R.id.btn_add_customers);
        showCustomer = findViewById(R.id.btn_show_customers);

        showCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShowCustomersActivity.class);
                startActivity(intent);
            }
        });

        // add customers
        addCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.activity_add_customers);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                final EditText firstname = dialog.findViewById(R.id.et_firstname);
                final EditText lastname = dialog.findViewById(R.id.et_lastname);
                final EditText city = dialog.findViewById(R.id.et_city);
                final EditText avg_price = dialog.findViewById(R.id.et_AVG_price);
                Button btn_add = dialog.findViewById(R.id.btn_add);

                btn_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String firstname_text = firstname.getText().toString();
                        String lastname_text = lastname.getText().toString();
                        String city_text = city.getText().toString();

                        int price_text = checkFields(firstname, lastname, city, avg_price);

                        if (price_text == -1)
                            return;

                        Customer customer = new Customer(firstname_text, lastname_text, city_text, price_text);

                        if (!customerHelper.isExistingCustomer(customer)) {
                            customerHelper.createCustomer(customer);
                            Toast.makeText(MainActivity.this, "Customer added successfully", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        } else
                            Toast.makeText(MainActivity.this, "This customer already exists", Toast.LENGTH_LONG).show();
                    }
                });
                dialog.show();
            }
        });
    }


    public static int checkFields(EditText firstname, EditText lastname, EditText city, EditText avg_price) {
        String firstname_text = firstname.getText().toString();
        String lastname_text = lastname.getText().toString();
        String city_text = city.getText().toString();
        int price_text = 0;

        // check values

        int flag = 0;
        try {
            price_text = Integer.parseInt(avg_price.getText().toString());
        } catch (Exception e) {
            avg_price.setError("Number cannot be empty");
            flag++;
        }

        if (!firstname_text.matches(NAME_REGEX)) {
            firstname.setError("First name can contain only letters");
            flag++;
        }
        if (!lastname_text.matches(NAME_REGEX)) {
            lastname.setError("Last name can contain only letters");
            flag++;
        }
        if (!city_text.matches(CITY_REGEX)) {
            city.setError("City name can contain only letters and spaces");
            flag++;
        }

        if (flag != 0)
            return -1;
        return price_text;
    }
}
