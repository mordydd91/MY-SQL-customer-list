//Author: Mordy dabah
package com.example.mysql;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class ShowCustomersActivity extends AppCompatActivity {

    private ListView lv_customers;
    private CustomerAdapter adapter;
    private CustomerHelper helper;
    private ArrayList<Customer> data;
    private Button btn_show_all, btn_sort_by_price, btn_sort_by_substring;

    public void deleteCustomerFromData(Customer customer) {
        data.remove(customer);
    }

    public void updateCustomerInData(Customer old_customer, Customer new_customer) {
        int idx = data.indexOf(old_customer);
        data.set(idx, new_customer);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_customers);

        helper = new CustomerHelper(this);
        helper.open();

        lv_customers = findViewById(R.id.lv_customers);
        btn_show_all = findViewById(R.id.btn_show_all);
        btn_sort_by_price = findViewById(R.id.btn_sort_by_price);
        btn_sort_by_substring = findViewById(R.id.btn_find_by_substring);

        data = helper.showAll();
        adapter = new CustomerAdapter(ShowCustomersActivity.this, data);
        lv_customers.setAdapter(adapter);

        btn_sort_by_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(ShowCustomersActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_avg_price);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                final EditText et_input = dialog.findViewById(R.id.et_avg_price);
                final Button btn_sort = dialog.findViewById(R.id.btn_sort);
                final Button btn_cancel = dialog.findViewById(R.id.btn_cancel_avg);

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btn_sort.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        data = helper.getCustomersAvgPrice(Integer.parseInt(et_input.getText().toString()));
                        adapter = new CustomerAdapter(ShowCustomersActivity.this, data);
                        lv_customers.setAdapter(adapter);
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        btn_sort_by_substring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(ShowCustomersActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_substring);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                final EditText et_input = dialog.findViewById(R.id.et_input_name);
                final Button btn_find_substring = dialog.findViewById(R.id.btn_find_substring);
                final Button btn_cancel = dialog.findViewById(R.id.btn_cancel_substring);

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btn_find_substring.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        data = helper.getCustomersSubString(et_input.getText().toString());
                        adapter = new CustomerAdapter(ShowCustomersActivity.this, data);
                        lv_customers.setAdapter(adapter);
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        btn_show_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data = helper.showAll();
                adapter = new CustomerAdapter(ShowCustomersActivity.this, data);
                lv_customers.setAdapter(adapter);
            }
        });
    }

    public class CustomerAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<Customer> data;

        public CustomerAdapter(Context context, ArrayList<Customer> data) {
            this.context = context;
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(R.layout.list_customer_format, null);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.activity_update_customers);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                    final EditText firstname = dialog.findViewById(R.id.et_firstname_update);
                    final EditText lastname = dialog.findViewById(R.id.et_lastname_update);
                    final EditText city = dialog.findViewById(R.id.et_city_update);
                    final EditText avg_price = dialog.findViewById(R.id.et_AVG_price_update);

                    firstname.setText(data.get(position).getFirstName());
                    lastname.setText(data.get(position).getLastName());
                    city.setText(data.get(position).getCity());
                    avg_price.setText(data.get(position).getAvg_shopping_price() + "");

                    ImageView iv_update = dialog.findViewById(R.id.iv_update);
                    ImageView iv_delete = dialog.findViewById(R.id.iv_delete);

                    iv_update.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String firstname_text = firstname.getText().toString();
                            String lastname_text = lastname.getText().toString();
                            String city_text = city.getText().toString();

                            int price_text = MainActivity.checkFields(firstname, lastname, city, avg_price);

                            if (price_text == -1)
                                return;

                            Customer customer = new Customer(data.get(position).getCustomerId(), firstname_text, lastname_text, city_text, price_text);

                            updateCustomerInData(data.get(position), customer);

                            helper.updateCustomer(customer);

                            Toast.makeText(context, "Customer updated successfully", Toast.LENGTH_LONG).show();
                            adapter = new CustomerAdapter(context, data);
                            lv_customers.setAdapter(adapter);
                            dialog.dismiss();
                        }
                    });

                    iv_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Customer customer_temp = data.get(position);
                            if (helper.deleteCustomer(customer_temp.getCustomerId())) {
                                Toast.makeText(context, "Customer deleted successfully", Toast.LENGTH_LONG).show();
                                deleteCustomerFromData(customer_temp);
                                adapter = new CustomerAdapter(context, data);
                                lv_customers.setAdapter(adapter);
                            } else
                                Toast.makeText(context, "Error deleting customer", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            });

            TextView id, firstname, lastname, city, avg_price;
            id = view.findViewById(R.id.clm_id);
            firstname = view.findViewById(R.id.clm_f_name);
            lastname = view.findViewById(R.id.clm_l_name);
            city = view.findViewById(R.id.clm_city);
            avg_price = view.findViewById(R.id.clm_avg_price);

            id.setText(data.get(position).getCustomerId() + "");
            firstname.setText(data.get(position).getFirstName());
            lastname.setText(data.get(position).getLastName());
            city.setText(data.get(position).getCity());
            avg_price.setText(data.get(position).getAvg_shopping_price() + "");

            return view;
        }
    }
}
