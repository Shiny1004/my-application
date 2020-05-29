package com.jc.foodapp.activity

import android.app.AlertDialog
import android.content.Intent
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
import org.json.JSONObject
import java.lang.Exception
import java.util.HashMap

class NewPasswordActivity : AppCompatActivity() {

    lateinit var edtxtotp: EditText
    lateinit var edtxtnewpassword: EditText
    lateinit var edtxtconfirmnewpassword: EditText
    lateinit var btnresetpassword: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_password)

        val mobileno = intent.getStringExtra("mobile_num")

        edtxtotp = findViewById(R.id.edtxtotp)
        edtxtnewpassword = findViewById(R.id.edtxtnewpassword)
        edtxtconfirmnewpassword = findViewById(R.id.edtxtconfirmnewpassword)
        btnresetpassword = findViewById(R.id.btnresetpassword)

        title = "Reset Password"

        btnresetpassword.setOnClickListener {

            print(mobileno)

            val password = edtxtnewpassword.text.toString()
            val cpassword = edtxtconfirmnewpassword.text.toString()
            val otp = edtxtotp.text.toString()

            if (otp.isNullOrEmpty() || password.isEmpty() || cpassword.isEmpty()){
                Toast.makeText(
                    this@NewPasswordActivity,
                    "All fields are required",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else if(password.length <4)
            {
                Toast.makeText(
                    this@NewPasswordActivity,
                    "Password must have atleast 4 characters",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else if (password.compareTo(cpassword) != 0){
                Toast.makeText(
                    this@NewPasswordActivity,
                    "Passwords don't match",
                    Toast.LENGTH_SHORT
                ).show()
            }else {

                val url = "http://13.235.250.119/v2/reset_password/fetch_result"
                val queue = Volley.newRequestQueue(this@NewPasswordActivity)

                if (ConnectionManager().checkConnectivity(this@NewPasswordActivity)) {

                    val jsonParams = JSONObject()
                    jsonParams.put("mobile_number", mobileno)
                    jsonParams.put("password", password)
                    jsonParams.put("otp", otp)

                    val jsonObjectRequest =
                        object : JsonObjectRequest(Request.Method.POST, url, jsonParams,
                            Response.Listener {
                                try {

                                    val data = it.getJSONObject("data")
                                    val success = data.getBoolean("success")

                                    if (success) {

                                        val successmessage = data.getString("successMessage")

                                        Toast.makeText(
                                            this,
                                            successmessage,
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        val intent = Intent(this@NewPasswordActivity, LoginActivity::class.java)
                                        startActivity(intent)
                                        finish()

                                    } else {
                                        Toast.makeText(
                                            this@NewPasswordActivity,
                                            "Check your OTP again", Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        this,
                                        "Some Error Occured.Try again later", Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }, Response.ErrorListener {
                                Toast.makeText(
                                    this@NewPasswordActivity,
                                    "Weak Password", Toast.LENGTH_SHORT
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
                    val dialog = AlertDialog.Builder(this)
                    dialog.setTitle("ERROR")
                    dialog.setMessage("No Internet Connection")
                    dialog.setPositiveButton("Open Settings") { text, listener ->
                        val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                        startActivity(settingsIntent)
                    } //will navigate to settings
                    dialog.setNegativeButton("Exit") { text, listener ->
                        ActivityCompat.finishAffinity(this)
                    }//closes the app
                    dialog.create()
                    dialog.show()

                }
            }
        }

    }

    override fun onBackPressed() {

        val intent=Intent(this@NewPasswordActivity,ForgotPasswordActivity::class.java)
        startActivity(intent)
        finish()

    }

}
