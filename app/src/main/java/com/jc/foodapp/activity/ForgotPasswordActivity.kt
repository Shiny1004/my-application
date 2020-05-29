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

class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var edtxtphonenoresetpassword:EditText
    lateinit var edtxtemailresetpassword:EditText
    lateinit var btnsendotp:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        edtxtphonenoresetpassword=findViewById(R.id.edtxtphonenoresetpassword)
        edtxtemailresetpassword=findViewById(R.id.edtxtemailresetpassword)
        btnsendotp=findViewById(R.id.btnsendotp)

        title="Reset Password"

        btnsendotp.setOnClickListener {

            val mobileno=edtxtphonenoresetpassword.text.toString()
            val email=edtxtemailresetpassword.text.toString()

            if(mobileno.length != 10)
            {
                Toast.makeText(
                    this@ForgotPasswordActivity,
                    "Mobile Number should have 10 digits",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else if(mobileno.isEmpty()||email.isEmpty())
            {
                Toast.makeText(
                    this@ForgotPasswordActivity,
                    "All fields are compulsory",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else
            {
                if(ConnectionManager().checkConnectivity(this@ForgotPasswordActivity))
                {

                    val url="http://13.235.250.119/v2/forgot_password/fetch_result"
                    val queue = Volley.newRequestQueue(this@ForgotPasswordActivity)

                    val jsonParams=JSONObject()
                    jsonParams.put("mobile_number",mobileno)
                    jsonParams.put("email",email)

                    val jsonObjectRequest =  object : JsonObjectRequest(Request.Method.POST,url,jsonParams,

                        Response.Listener {

                            try {

                                val data = it.getJSONObject("data")
                                val success = data.getBoolean("success")

                                if(success)
                                {
                                    val firsttry = data.getBoolean("first_try")

                                    if(firsttry)
                                    {
                                        Toast.makeText(
                                            this@ForgotPasswordActivity,
                                            "OTP sent to E-mail. Use same OTP for the next 24hrs.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    else
                                    {
                                        Toast.makeText(
                                            this@ForgotPasswordActivity,
                                            "OTP sent to E-mail.Use same OTP for the next 24hrs.",
                                            Toast.LENGTH_SHORT
                                        ).show() }

                                    val intent = Intent(this@ForgotPasswordActivity,NewPasswordActivity::class.java)
                                    intent.putExtra("mobile_num",mobileno)
                                    startActivity(intent)
                                    finish()
                                }
                                else
                                {
                                    Toast.makeText(
                                        this@ForgotPasswordActivity,
                                        "Invalid data",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            catch (e:Exception)
                            {
                                Toast.makeText(
                                    this@ForgotPasswordActivity,
                                    "Some Error Occured.Try again later",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    },Response.ErrorListener {
                            Toast.makeText(
                                this@ForgotPasswordActivity,
                                "User doesn't exist",
                                Toast.LENGTH_SHORT
                            ).show()
                    })
                    {
                        override fun getHeaders(): MutableMap<String, String> {
                            val headers = HashMap<String, String>()
                            headers["Content-type"] = "application/json"
                            headers["token"] = "b9e05b8368cd66"
                            return headers }
                    }
                    queue.add(jsonObjectRequest)
                }
                else
                {
                    val dialog = AlertDialog.Builder(this@ForgotPasswordActivity)
                    dialog.setTitle("ERROR")
                    dialog.setMessage("No Internet Connection")
                    dialog.setPositiveButton("Open Settings"){text,Listener->
                        val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                        startActivity(settingsIntent)
                    }
                    dialog.setNegativeButton("Exit"){text,Listener->
                        ActivityCompat.finishAffinity(this@ForgotPasswordActivity)
                    }
                    dialog.create()
                    dialog.show()
                }
            }
        }
    }
}
