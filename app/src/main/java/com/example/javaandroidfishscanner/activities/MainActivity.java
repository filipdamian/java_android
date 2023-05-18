package com.example.javaandroidfishscanner.activities;
import com.example.javaandroidfishscanner.R;
//import com.example.javaandroidfishscanner.adapters.CustomAdapter;
import com.example.javaandroidfishscanner.utils.SessionManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.net.HttpURLConnection;

public class MainActivity extends AppCompatActivity {

    private EditText loginEmail, loginPassword, registerName, registerEmail, registerPassword;
    private Button loginButton, registerButton;
    private SessionManager sessionManager;
    //private RecyclerView recyclerView;
   // private CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        registerName = findViewById(R.id.register_name);
        registerEmail = findViewById(R.id.register_email);
        registerPassword = findViewById(R.id.register_password);

        loginButton = findViewById(R.id.login_button);
        registerButton = findViewById(R.id.register_button);

        sessionManager = new SessionManager(this);

        // Set onClickListeners for the buttons
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                loginUser();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        // Get the name, email, and password from the UI elements
        String name = registerName.getText().toString().trim();
        String email = registerEmail.getText().toString().trim();
        String password = registerPassword.getText().toString().trim();

        try {
            // Create a new URL object with the API endpoint
            URL url = new URL("https://d445-2a02-2f00-100-4400-50be-55fb-3dae-2f84.ngrok-free.app/api/Account/register");

            // Create a new HttpURLConnection object
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // Set the request headers
            conn.setRequestProperty("Content-Type", "application/json");

            // Create a JSON object to store the user's information
            JSONObject json = new JSONObject();
            json.put("name", name);
            json.put("email", email);
            json.put("password", password);

            // Write the JSON object to the request body
            OutputStream os = conn.getOutputStream();
            os.write(json.toString().getBytes());
            os.flush();
            os.close();

            // Check the response code
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Registration was successful
                Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_SHORT).show();
            } else {
                // Registration failed
                Toast.makeText(getApplicationContext(), "Registration failed", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // Handle any errors that occurred
            e.printStackTrace();
        }
    }


    private void loginUser() {
        String email = loginEmail.getText().toString().trim();
        String password = loginPassword.getText().toString().trim();

        // Create a new thread to perform the login process in the background
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Construct the URL to your login API
                    URL url = new URL("https://d445-2a02-2f00-100-4400-50be-55fb-3dae-2f84.ngrok-free.app/api/Account/login");

                    // Open a connection to the URL
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);

                    // Create the request body as a JSON string
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("uniqueIdentifier", email);
                    jsonParam.put("password", password);

                    // Write the JSON string to the connection's output stream
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    os.writeBytes(jsonParam.toString());
                    os.flush();
                    os.close();

                    // Get the response code from the server
                    int responseCode = conn.getResponseCode();
                    Log.d("Login", "Response code: " + responseCode);

                    // Check if the login was successful (i.e., response code is 200)
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // Save the user's login information using the session manager
                        sessionManager.setLogin(true);
                        sessionManager.setUserEmail(email);

                        // Start the next activity (e.g., the home screen)
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Display an error message to the user
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
