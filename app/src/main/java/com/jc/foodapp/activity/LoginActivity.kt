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
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.jc.foodapp.R
import com.jc.foodapp.util.ConnectionManager
import org.json.JSONObject
import java.lang.Exception
import java.util.HashMap

class LoginActivity : AppCompatActivity() {

    lateinit var edtxtusername:EditText
    lateinit var edtxtpassword:EditText
    lateinit var btnlogin:Button
    lateinit var txtforgotpassword:TextView
    lateinit var txtnewuser:TextView
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences=getSharedPreferences(getString(R.string.preferences_login),Context.MODE_PRIVATE)

        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn",false)//must

        if(isLoggedIn)
        {
            val userid=sharedPreferences.getString("user_id","00")
            val name =sharedPreferences.getString("name","Welcome")
            val email=sharedPreferences.getString("email","Null")
            val mobilenumber=sharedPreferences.getString("mobile_number","Null")
            val address=sharedPreferences.getString("address","Null")

            val intent = Intent(this@LoginActivity,MainActivity::class.java)
            intent.putExtra("user_id",userid)
            intent.putExtra("name",name)
            intent.putExtra("email",email)
            intent.putExtra("mobile_number",mobilenumber)
            intent.putExtra("address",address)
            startActivity(intent)
            finish()
        }
        else
        {
            setContentView(R.layout.activity_login)


        title="Sign In"

        edtxtusername = findViewById(R.id.edtxtmobilenologin)
        edtxtpassword = findViewById(R.id.edtxtpasswordlogin)
        btnlogin = findViewById(R.id.btnlogin)
        txtforgotpassword = findViewById(R.id.txtforgotpassword)
        txtnewuser = findViewById(R.id.txtnewuser)


        txtforgotpassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        txtnewuser.setOnClickListener {
            val intent = Intent(this, UserRegisterActivity::class.java)
            startActivity(intent)
        }

        btnlogin.setOnClickListener {

            val mobileno = edtxtusername.text.toString()
            val password = edtxtpassword.text.toString()

            if (mobileno.length != 10) {
                Toast.makeText(
                    this@LoginActivity,
                    "Mobile Number should have 10 digits",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (password.length < 4) {
                Toast.makeText(this@LoginActivity, "Incorrect Credentials", Toast.LENGTH_SHORT)
                    .show()
            } else {

                if (ConnectionManager().checkConnectivity(this@LoginActivity)) {

                    val queue = Volley.newRequestQueue(this@LoginActivity)
                    val url = "http://13.235.250.119/v2/login/fetch_result/"

                    val jsonParams = JSONObject()
                    jsonParams.put("mobile_number", mobileno)
                    jsonParams.put("password", password)

                    val jsonObjectRequest =
                        object : JsonObjectRequest(Request.Method.POST, url, jsonParams,
                            Response.Listener {

                                try {
                                    val data = it.getJSONObject("data")
                                    val success = data.getBoolean("success")

                                    if (success) {
                                        val user = data.getJSONObject("data")

                                        val userid=user.getString("user_id")
                                        val username = user.getString("name")
                                        val Email = user.getString("email")
                                        val MobileNo = user.getString("mobile_number")
                                        val Address = user.getString("address")

                                        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
                                        sharedPreferences.edit().putString("user_id",userid).apply()
                                        sharedPreferences.edit().putString("name", username).apply()
                                        sharedPreferences.edit().putString("email", Email).apply()
                                        sharedPreferences.edit().putString("mobile_number", MobileNo).apply()
                                        sharedPreferences.edit().putString("address", Address).apply()

                                        val intent =
                                            Intent(this@LoginActivity, MainActivity::class.java)
                                        intent.putExtra("user_id",userid)
                                        intent.putExtra("name", username)
                                        intent.putExtra("email", Email)
                                        intent.putExtra("mobile_number", MobileNo)
                                        intent.putExtra("address", Address)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        Toast.makeText(
                                            this@LoginActivity,
                                            "Invalid Credentials",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "Some Error Occured.Try again later",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }, Response.ErrorListener {

                                Toast.makeText(
                                    this@LoginActivity,
                                    "Some Error Occured.Try again later",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }) {
                            override fun getHeaders(): MutableMap<String, String> {

                                val headers = HashMap<String, String>()
                                headers["Content-type"] = "application/json"
                                headers["token"] = "b9e05b8368cd66"
                                return headers
                            }
                        }
                    queue.add(jsonObjectRequest)
                } else {
                    val dialog = AlertDialog.Builder(this@LoginActivity)
                    dialog.setTitle("ERROR")
                    dialog.setMessage("No Internet Connection")
                    dialog.setPositiveButton("Open Settings") { text, Listener ->

                        val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                        startActivity(settingsIntent)
                    }
                    dialog.setNegativeButton("Exit") { text, Listener ->

                        ActivityCompat.finishAffinity(this@LoginActivity)
                    }
                    dialog.create()
                    dialog.show()
                }
            }

        }
    }


    }
    override fun onPause() {
        super.onPause()
       // edtxtusername.setText("")
        edtxtpassword.setText("")
    }

}
