package com.example.gestionnotes;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.security.AccessController;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    ListView listView;
    Spinner spinner;
    Button btn;
    TextInputEditText inp;
    ArrayList<String> array_modules;
    ArrayAdapter<String> adapterSpinner; ArrayAdapter adapterList;
    List<String> modules;
    TextView tv;
    int item_pos;
    Long sum=0L;
    float result=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] ArrayModules = getResources().getStringArray(R.array.modules);
        modules = new ArrayList<>(Arrays.asList(ArrayModules));

        // ListView
        listView = findViewById(R.id.list);
        array_modules = new ArrayList<>();
        for ( String m: modules ){
            array_modules.add(m+":      0");
        }

        adapterList = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array_modules);
        listView.setAdapter(adapterList);

        // Spinner
        spinner = findViewById(R.id.spinner);

//        adapterSpinner = ArrayAdapter.createFromResource(this, R.array.modules, android.R.layout.simple_spinner_item);
        adapterSpinner = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, modules);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);
        spinner.setOnItemSelectedListener(this);

        // Button
        btn = (Button)findViewById(R.id.save);
        btn.setOnClickListener(this);

        // Input
        inp = (TextInputEditText)findViewById(R.id.note);

        // TextView
        tv = findViewById(R.id.result);
        tv.setVisibility(View.GONE);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item_pos = position;
        String item = spinner.getSelectedItem().toString();
        String text = "Item: "+item+"  Position: "+item_pos;
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        String module = spinner.getSelectedItem().toString();
        String note = inp.getText().toString();
        sum+= Long.parseLong(note);
        inp.setText("");
        int val = 0;
        while (array_modules.size() > val) {
            String curmod = array_modules.get(val).split(":")[0];
            System.out.println(curmod);
//            System.out.println("-------- val:"+val+"/ sum:"+sum+"--------");

            if(module.equals(curmod)){
                array_modules.set(val, module+":        "+note);
                adapterList = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array_modules);
                listView.setAdapter(adapterList);

                modules.remove(item_pos);
                adapterSpinner.notifyDataSetChanged();
            }
            if (modules.size() == 0){
                // Hide spinner & button
                btn.setVisibility(View.GONE);
                spinner.setVisibility(View.GONE);
//                System.out.println("-------- sum:" + sum +"--------");
//                System.out.println("-------- size:" + array_modules.size() +"--------");

                // Calcule la moyenne
                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                decimalFormat.setGroupingUsed(true);
                decimalFormat.setGroupingSize(3);
                result = (float)sum/array_modules.size();
                tv.setText("Note:   "+decimalFormat.format(result));
                tv.setVisibility(View.VISIBLE);
            }
            val++ ;
        }
    }
}