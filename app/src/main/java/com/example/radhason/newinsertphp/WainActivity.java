package com.example.radhason.newinsertphp;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TimeZone;

public class WainActivity extends ListActivity {
    private static final String[] paths = {"Select Weather type", "Sunny", "Rainy", "Cloudy"};
    private EditText etFullName;
    private EditText description;
    private EditText etPhone;
    private EditText etEmail;
    private String timeDate, name,a_phone;
    static int TAKE_PICTURE = 1;
    private ImageView imageView;
    String image;
    private Bitmap bitmap;
    private Button buttonChoose;
    private Spinner spinner;
    private ProgressDialog pDialog;
    private ArrayList<String>results=new ArrayList<String>();
    private static String url= "http://radhason.comxa.com/get.php";
    //private static String url= "http://10.0.2.2/localwebsite/contact.php";
    //    private static final String TAG_CONTACTS = "contacts";
    private static final String TAG_CONTACTS = "user_info";
    // private static final String TAG_ID = "id";
    private static final String TAG_NAME = "fullname";
    private static final String TAG_EMAIL = "email";
    //  private static final String TAG_ADDRESS = "address";
    //  private static final String TAG_GENDER = "gender";
    private static final String TAG_PHONE = "phone";
     private static final String TAG_DESCFRIPTION = "description";
     private static final String TAG_SPINNER = "spinner";
    //  private static final String TAG_PHONE_OFFICE = "office";

    // contacts JSONArray
    JSONArray contacts = null;
    Calendar cal;
    // Hashmap for ListView
    ArrayList<HashMap<String, String>> contactList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wactivity_main);
        cal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        timeDate= String.valueOf(cal.getTime());
        Log.d("Time", "" + timeDate);

        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        etFullName = (EditText) findViewById(R.id.etFullName);
        description = (EditText) findViewById(R.id.description);

        etPhone = (EditText) findViewById(R.id.etPhone);
        etEmail = (EditText) findViewById(R.id.etEmail);
        spinner = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(WainActivity.this,
                android.R.layout.simple_spinner_item, paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        contactList = new ArrayList<HashMap<String, String>>();

        ListView lv = getListView();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = ((TextView) view.findViewById(R.id.name)).getText().toString();
                String cost = ((TextView) view.findViewById(R.id.email)).getText().toString();

                String description = ((TextView) view.findViewById(R.id.mobile)).getText().toString();
//                Intent in =new Intent(getApplicationContext(),SingleContactActivity.class);
//
//                in.putExtra(TAG_NAME, name);
//                in.putExtra(TAG_EMAIL, cost);
//                in.putExtra(TAG_PHONE_MOBILE, description);
//                startActivity(in);


            }
        });
        new GetContacts().execute();
    }

    private void showFileChooser() {
//        Intent intent = new Intent();
        Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // start camera activity
        startActivityForResult(intent1, TAKE_PICTURE);
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == TAKE_PICTURE && resultCode == RESULT_OK && intent != null) {

            Bundle extras = intent.getExtras();

            // get bitmap
            bitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(bitmap);
        }

    }

    public void signup(View v) {
        Intent intent = new Intent(WainActivity.this, SecondActivity.class);
        intent.putExtra("name", etFullName.getText().toString());
        intent.putExtra("description", description.getText().toString());

        intent.putExtra("phone", etPhone.getText().toString());
        intent.putExtra("email", etEmail.getText().toString());
        intent.putExtra("spinner", String.valueOf(spinner.getSelectedItem()));
        startActivity(intent);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //   @Override
    public void onClick(View v) {
        if (v == buttonChoose) {
            showFileChooser();
        }
    }




    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(WainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
            Log.d("url: ", "> " + url);
            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    contacts = jsonObj.getJSONArray(TAG_CONTACTS);

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

//                        String id = c.getString(TAG_ID);
                         name = c.getString(TAG_NAME);
                        Log.d("ASHRADHA",""+name);
                        String email = c.getString(TAG_EMAIL);
                         String description = c.getString(TAG_DESCFRIPTION);
                        String spinner = c.getString(TAG_SPINNER);
                      a_phone = c.getString(TAG_PHONE);

                        // Phone node is JSON Object
//                        JSONObject phone = c.getJSONObject(TAG_PHONE);
//                        String mobile = phone.getString(TAG_PHONE_MOBILE);
//                        String home = phone.getString(TAG_PHONE_HOME);
//                        String office = phone.getString(TAG_PHONE_OFFICE);

                        // tmp hashmap for single contact
                        HashMap<String, String> contact = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
//                        contact.put(TAG_ID, id);
                        contact.put(TAG_NAME, name);
                        contact.put(TAG_EMAIL, email);
//                        contact.put(TAG_PHONE_MOBILE, mobile);
                        contact.put(TAG_PHONE, a_phone);
                        contact.put(TAG_DESCFRIPTION, description);
                        contact.put(TAG_SPINNER, spinner);

                        // adding contact to contact list
                        contactList.add(contact);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    WainActivity.this, contactList,
                    R.layout.list_item, new String[]{TAG_NAME,TAG_EMAIL, TAG_PHONE,TAG_SPINNER,TAG_DESCFRIPTION}, new int[]{R.id.name,R.id.email, R.id.mobile,R.id.spinner,R.id.showdes});
//            WainActivity.this, contactList,
//                    R.layout.list_item, new String[] { TAG_NAME, TAG_EMAIL,
//                    TAG_PHONE_MOBILE }, new int[] { R.id.name,
//                    R.id.email, R.id.mobile });
//                      results.add("Name: " + name + "  phone: " + a_phone);

           setListAdapter(adapter);
        }

    }
}