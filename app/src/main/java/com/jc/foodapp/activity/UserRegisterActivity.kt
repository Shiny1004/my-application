package com.jc.foodapp.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.jc.foodapp.R
import com.jc.foodapp.util.ConnectionManager
import kotlinx.android.synthetic.main.activity_user_register.*
import org.json.JSONObject
import java.lang.Exception
import java.util.HashMap

class UserRegisterActivity : AppCompatActivity() {


    lateinit var edtxtname: EditText
    lateinit var edtxtemail: EditText
    lateinit var edtxtmobileno: EditText
    lateinit var edtxtaddress: EditText
    lateinit var edtxtpass: EditText
    lateinit var edtxtconfirmpass: EditText
    lateinit var btnregister: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_user_register)

        title = "Sign Up"

        edtxtname = findViewById(R.id.edtxtname)
        edtxtemail = findViewById(R.id.edtxtemailregister)
        edtxtmobileno = findViewById(R.id.edtxtmobilenoregister)
        edtxtaddress = findViewById(R.id.edtxtaddress)
        edtxtpass = findViewById(R.id.edtxtpasswordregister)
        edtxtconfirmpass = findViewById(R.id.edtxtconfirmpassword)

        btnregister = findViewById(R.id.btnregister)


        btnregister.setOnClickListener {


            val name = edtxtname.text.toString()
            val email = edtxtemail.text.toString()
            val phone = edtxtmobileno.text.toString()
            val address = edtxtaddress.text.toString()
            val pass = edtxtpass.text.toString()
            val cpass = edtxtconfirmpass.text.toString()

            if(pass.length < 4) {
                Toast.makeText(
                    this@UserRegisterActivity,
                    "Password must have minimum of 4 characters",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else if(phone.length != 10) {
                Toast.makeText(
                    this@UserRegisterActivity,
                    "Mobile Number should have 10 digits",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else if (pass.compareTo(cpass)!=0) {
                Toast.makeText(
                    this@UserRegisterActivity,
                    "Password doesn't match",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else if(name.isEmpty()||phone.isEmpty()||email.isEmpty()||pass.isEmpty()||cpass.isEmpty()||address.isEmpty())
            {
                Toast.makeText(
                    this@UserRegisterActivity,
                    "All fields are compulsory",
                    Toast.LENGTH_LONG
                ).show()

            }
            else {
                
                if (ConnectionManager().checkConnectivity(this@UserRegisterActivity)) {

                    val queue = Volley.newRequestQueue(this@UserRegisterActivity)
                    val url = "http://13.235.250.119/v2/register/fetch_result"

                    val jsonParams = JSONObject()
                    jsonParams.put("name", name)
                    jsonParams.put("mobile_number", phone)
                    jsonParams.put("password", pass)
                    jsonParams.put("address", address)
                    jsonParams.put("email", email)

                    val jsonObjectRequest =
                        object : JsonObjectRequest(Request.Method.POST, url, jsonParams,
                            Response.Listener {

                                try {
                                  val data=it.getJSONObject("data")

                                    val success = data.getBoolean("success")

                                    if (success) {
                                       val jsonObject = data.getJSONObject("data")
                                        val userid=jsonObject.getString("user_id")
                                        val Name=jsonObject.getString("name")
                                        val Email=jsonObject.getString("email")
                                        val MobileNo=jsonObject.getString("mobile_number")
                                        val Address= jsonObject.getString("address")

                                        Toast.makeText(
                                            this@UserRegisterActivity,
                                            "Successfully Registered",
                                            Toast.LENGTH_LONG
                                        ).show()

                                        val intent = Intent(
                                            this@UserRegisterActivity,
                                            LoginActivity::class.java
                                        )
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        Toast.makeText(
                                            this@UserRegisterActivity,
                                            "User already exists",
                                            Toast.LENGTH_LONG).show()
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        this@UserRegisterActivity,
                                        "Some Error Occured.Try again later",
                                        Toast.LENGTH_LONG).show() }
                            }, Response.ErrorListener {
                                Toast.makeText(
                                    this@UserRegisterActivity,
                                    "Invalid Data",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }) {
                            override fun getHeaders(): MutableMap<String, String> {
                                val headers = HashMap<String, String>()
                                headers["Content-type"] = "application/json"
                                headers["token"] = "b9e05b8368cd66"
                                return headers }
                        }
                    queue.add(jsonObjectRequest)
                } else {
                    val dialog = AlertDialog.Builder(this@UserRegisterActivity)
                    dialog.setTitle("ERROR")
                    dialog.setMessage("No Internet Connection")
                    dialog.setPositiveButton("Open Settings") { text, listener ->

                        val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                        startActivity(settingsIntent)

                    } //will navigate to settings
                    dialog.setNegativeButton("Exit") { text, listener ->

                        ActivityCompat.finishAffinity(this@UserRegisterActivity)

                    }//closes the app
                    dialog.create()
                    dialog.show()
                }

            }


        }
    }


}
