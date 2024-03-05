package com.okanaktas.instagram

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PackageManagerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.okanaktas.instagram.databinding.ActivityFeedBinding
import com.okanaktas.instagram.databinding.ActivityUploadBinding

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    //registerLauncher içerisinde aldığım intentFromResult artık artık görselin uri sini barındırdığı için bu değişkene atamak için oluşturuyorum
    var selectedPicture: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUploadBinding.inflate(layoutInflater)
        val view = binding.root
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        registerLauncher()


    }


    fun upload(view: View) {

    }

    fun selectedImage(view: View) {
        //izni daha önce aldık mı diye kontrol ediyorum
        if (ContextCompat.checkSelfPermission(this@UploadActivity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //izin yoksa
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@UploadActivity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(view, "Permission Needed!", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission") {
                    //request permission
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }.show()
            } else {
                //request permission
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        } else {
            //izni hali hazırda aldıysak, izin varsa
            //Intent.ACTION_PICK -> Git ve oradan bir veri al. Virgülden sonrası -> Nereden alacak bunu MediStore.Images.Media.EXTERNAL_CONTENT_URI
            val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            //start activity for result
            activityResultLauncher.launch(intentToGallery)
        }

    }

    private fun registerLauncher() {
        //Bir aktivite başlatacağım ama bir sonuç için yani veri için. burada kullanıcının seçtiği verinin ki burada görselin nerede kayıtlı olduğunun nerede kayıtlı olduğunu yani URI ını alıyoruz ve bize bize bir callBack veriyor. Bu da seçildikten sonra ne olacak kısmı. süslü parantezin içi bu kısım.
        //Burada kullanıcı izni onayladıysa yani RESULT_OK ise
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                //gelen sonucu yani URI ı result.data ile alıyorum fakat bu nullable geliyor o yüzden if ile kontrol etmem gerek
                val intentFromResult = result.data
                if (intentFromResult != null) {
                    selectedPicture = intentFromResult.data
                    //if ile kontrol etmek yerine böyle de kontrol edilebilir nullable
                    selectedPicture?.let {
                        binding.imageView.setImageURI(it)
                    }
                }
            }
        }

        //bir boolean dönüyor yani izin verildi mi verilmedi mi diye bunu direkt " result-> " olarak değiştiryorum bu result burada boolean
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) {
                //permission granted
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            } else {
                //permission denied
                Toast.makeText(this@UploadActivity, "Permission Needed!", Toast.LENGTH_LONG).show()
            }
        }
    }

}