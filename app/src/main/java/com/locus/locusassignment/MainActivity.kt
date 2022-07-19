package com.locus.locusassignment

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.locus.locusassignment.adapter.DataAdapter
import com.locus.locusassignment.adapter.ResultAdapter
import com.locus.locusassignment.model.DataModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),DataAdapter.ActionListener,View.OnClickListener {

    private val viewModel by viewModels<DataViewModel>()
    lateinit var adapter: DataAdapter
    lateinit var profileImageUri:Uri
    lateinit var idItem:String
    lateinit var takePicture:ActivityResultLauncher<Uri>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rv_list.layoutManager = LinearLayoutManager(this)
        adapter = DataAdapter(this, ArrayList<DataModel>())
        rv_list.adapter = adapter
        bt_submit.setOnClickListener(this)
        setObserver()
        viewModel.getData()
        takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
            if (success) {
                adapter.setImageValue(idItem,profileImageUri)
                viewModel.map.set(idItem,profileImageUri)
            }
            else{
                Log.e("Image Capture Failed","Failed $success")

            }
        }
    }

    private fun setObserver() {
        viewModel.model.observe(this, {


            adapter.setDataList(it)
        })
    }

    override fun selectImage(id: String) {
        idItem=id
        val root =
            File(this.filesDir, BuildConfig.APPLICATION_ID + File.separator)
        root.mkdirs()
        val fname = "img_" + System.currentTimeMillis() + ".jpg"
        val sdImageMainDirectory = File(root, fname)
        profileImageUri = FileProvider.getUriForFile(this, this?.applicationContext?.packageName + ".provider", sdImageMainDirectory)


        takePicture.launch(profileImageUri)


    }

    override fun comment(id: String, comment: String) {
            viewModel.map.set(id,comment)
    }

    override fun choice(id: String, selectedChoice: String) {

        viewModel.map.set(id,selectedChoice)

    }

    override fun deleteImage(id: String) {
        viewModel.map.remove(id)
    }

    override fun onClick(p0: View?) {
        var adapter=ResultAdapter(this,viewModel.model.value!!,viewModel.map)
        rv_results.layoutManager=LinearLayoutManager(this)
        rv_results.adapter=adapter
    }


}