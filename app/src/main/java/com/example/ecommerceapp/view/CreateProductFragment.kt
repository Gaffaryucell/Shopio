package com.example.ecommerceapp.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.ecommerceapp.R
import com.example.ecommerceapp.databinding.FragmentAccountoptionsBinding
import com.example.ecommerceapp.databinding.FragmentCreateProductBinding
import com.example.ecommerceapp.model.Product
import com.example.ecommerceapp.model.ProductX
import com.example.ecommerceapp.util.Status
import com.example.ecommerceapp.util.Util.DATABASE_URL
import com.example.ecommerceapp.viewmodel.AccountoptionsViewModel
import com.example.ecommerceapp.viewmodel.CreateProductViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.util.UUID


class CreateProductFragment : Fragment() {

    private lateinit var viewModel: CreateProductViewModel
    private lateinit var binding : FragmentCreateProductBinding

    private var selectedImageView : ImageView? = null

    private val selectedColors = mutableListOf<Int>()

    private val REQUEST_IMAGE_CAPTURE = 101
    private val REQUEST_IMAGE_PICK = 102
    private val PERMISSION_REQUEST_CODE = 200
    private var allPermissionsGranted = false

    private var resultByteArray = arrayListOf<ByteArray>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateProductBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(CreateProductViewModel::class.java)
        requestPermissionsIfNeeded()
        binding.saveButton.setOnClickListener {
            getDataAndCreateProduct()
        }
        binding.buttonColorPicker.setOnClickListener {
            ColorPickerDialog
                .Builder(requireContext())
                .setTitle("Product color")
                .setPositiveButton("Select", object : ColorEnvelopeListener {
                    override fun onColorSelected(envelope: ColorEnvelope?, fromUser: Boolean) {
                        envelope?.let {
                            selectedColors.add(it.color)
                        }
                    }

                }).setNegativeButton("Cancel") { colorPicker, _ ->
                    colorPicker.dismiss()
                }.show()
        }

        binding.imageView1.setOnClickListener {
            openCamera(binding.imageView1)
        }
        binding.imageView2.setOnClickListener {
            openCamera(binding.imageView2)
        }
        binding.imageView3.setOnClickListener {
            openCamera(binding.imageView3)
        }
        binding.imageView4.setOnClickListener {
            openCamera(binding.imageView4)
        }
        observeLiveData()
    }

    private fun observeLiveData(){
        viewModel.uploadProductStatus.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.saveButton.revertAnimation()
                }
                Status.ERROR -> {
                    binding.saveButton.revertAnimation()
                }
                Status.LOADING -> {
                    binding.saveButton.startAnimation()
                }
            }
        })
    }
    private fun getDataAndCreateProduct(){
        val name = binding.edName.text.toString()
        val category = binding.edCategory.text.toString()
        val description = binding.edDescription.text.toString()
        val price = binding.edPrice.text.toString().toDoubleOrNull()
        val offerPercentage = binding.offerPercentage.text.toString().toDoubleOrNull()
        val sizes = binding.edSizes.text.toString()
        val id = UUID.randomUUID().toString()
        viewModel.createProduct(
            id,
            name,
            category,
            description,
            price ?: 1.0,
            offerPercentage ?: 1.0,
            sizes,
            selectedColors,
            resultByteArray
        )
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun openCamera(imageView: ImageView) {
        selectedImageView = imageView
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        }
    }
    @SuppressLint("QueryPermissionsNeeded")
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_PICK)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    selectedImageView?.setImageBitmap(imageBitmap)
                    compressedForCam(imageBitmap)
                }
                REQUEST_IMAGE_PICK -> {
                    val selectedImageUri = data?.data
                    selectedImageView?.setImageURI(selectedImageUri)
                    if (selectedImageUri != null){
                        compressedForGalery(selectedImageUri)
                    }
                }
            }
        }
    }
    private fun requestPermissionsIfNeeded() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            // İzinleri talep et
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                PERMISSION_REQUEST_CODE
            )
        } else {
            // İzinler zaten verilmişse burada yapılacak işlemler
            allPermissionsGranted = true
        }
    }

    //Kameradan gelen resmi compress etmek için kullanılan fonksiyon
    private fun compressedForCam(photo : Bitmap){
        val compress = BackgroundImageCompress(photo)
        val myUri : Uri? = null
        compress.execute(myUri)
    }
    //Galeryden gelen resmi compress etmek için kullanılan fonksiyon
    private fun compressedForGalery(photo: Uri){
        val compress = BackgroundImageCompress()
        compress.execute(photo)
    }

    //arkaplanda kompress işleminin yapılacağı sınıf
    @SuppressLint("StaticFieldLeak")
    inner class BackgroundImageCompress : AsyncTask<Uri, Void, ByteArray> {
        var myBitmap : Bitmap? = null
        //eğer kameradan görsel alınırsa burda bir bitmap değeri olur
        //ve bu değer myBitmap'e eşitlenir
        constructor(b : Bitmap?){
            if (b != null){
                myBitmap = b
            }
        }
        //eğer galeryden bir değer geldiyse bu Uri olarak verilir
        //ve bu constractor boş olarak kalır
        constructor()
        @Deprecated("Deprecated in Java")
        override fun onPreExecute() {
            super.onPreExecute()
        }

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg p0: Uri?): ByteArray {
            //Galeryden resim geldi ise galerideki resnib pozisyonuna git ve bitmap değerini al
            //anlamına geliyor
            if (myBitmap == null){
                //Uri
                myBitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver,p0[0])
            }
            var imageByteArray : ByteArray? = null
            for (i in 1..5){
                imageByteArray = converteBitmapTOByte(myBitmap,100/i)
            }
            return imageByteArray!!
        }

        @Deprecated("Deprecated in Java")
        override fun onProgressUpdate(vararg values: Void?) {
            super.onProgressUpdate(*values)
        }

        //son olarak sonuçla ne yapılacağı burada belirlenir istediğiniz bir fonksiyona parametre olarak atanabilir
        @Deprecated("Deprecated in Java")
        override fun onPostExecute(result: ByteArray?) {
            super.onPostExecute(result)
            if (result!=null){
                resultByteArray.add(result)
            }
        }
    }

    //bitmap'i byteArray'e çeviren fonksiyon
    private fun converteBitmapTOByte(myBitmap: Bitmap?, i: Int): ByteArray {
        var stream = ByteArrayOutputStream()
        myBitmap?.compress(Bitmap.CompressFormat.JPEG,i,stream)
        return stream.toByteArray()
    }
}