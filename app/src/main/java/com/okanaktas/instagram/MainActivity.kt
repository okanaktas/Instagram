package com.okanaktas.instagram

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.okanaktas.instagram.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    private lateinit var email: String
    private lateinit var password: String
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        auth = Firebase.auth

        email = binding.emailText.text.toString()
        password = binding.passwordText.text.toString()

    }

    fun signInClick(view: View) {

        if (email.isEmpty() && password.isEmpty()) {
            Toast.makeText(this@MainActivity, "Enter mail and password", Toast.LENGTH_LONG).show()
        } else {
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                val intent = Intent(this@MainActivity, FeedActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                Toast.makeText(this@MainActivity, it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }


    }

    fun signUpClick(view: View) {

        //if(email.isNotEmpty() && password.isNotEmpty()){} -> kotlinde boş mu değil mi diye kontrol etmek için ya da aşağıda ki örnekte olduğu gibi
        if (email.equals("") || password.equals("")) {
            Toast.makeText(this@MainActivity, "Enter mail and password!", Toast.LENGTH_LONG).show()
        } else {
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                //Succes dönerse çalıştır
                val intent = Intent(this@MainActivity, FeedActivity::class.java)
                startActivity(intent)
                //kullanıcı geri butonuna bastığında geri dönemesin diye aktiviteyi sonlandırıyorum.
                finish()
            }.addOnFailureListener {
                //Toast içerisinde it.localizedMessage dememin sebebi sistem kendisi mesajı kullanıcıya göstersin diye ve 'it' i zaten veriyor lambda içerisinde
                Toast.makeText(this@MainActivity, it.localizedMessage, Toast.LENGTH_LONG).show()
            }

        }

    }
}