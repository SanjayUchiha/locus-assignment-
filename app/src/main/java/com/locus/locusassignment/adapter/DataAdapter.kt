package com.locus.locusassignment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.opengl.Visibility
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.locus.locusassignment.MainActivity
import com.locus.locusassignment.R
import com.locus.locusassignment.model.DataModel

class DataAdapter(private val mContext: Context, var list: ArrayList<DataModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var map=HashMap<String,Any>()


    fun setDataList(dataList: ArrayList<DataModel>) {
        list = dataList
        notifyDataSetChanged()
    }

    fun setImageValue(id:String,uri: Uri){
        map.set(id,uri)
        notifyDataSetChanged()
    }

    class ImageViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var number=v.findViewById<TextView>(R.id.tv_number)
        var image=v.findViewById<ImageView>(R.id.iv_image)
        var clear=v.findViewById<ImageView>(R.id.iv_close)
        var placeholder=v.findViewById<View>(R.id.v_placeholder)
    }

    class ChoiceViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var number=v.findViewById<TextView>(R.id.tv_number)
        var group=v.findViewById<RadioGroup>(R.id.rg_items)
        var title=v.findViewById<TextView>(R.id.tv_title)
    }

    class CommentViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var number=v.findViewById<TextView>(R.id.tv_number)
        var switch=v.findViewById<SwitchCompat>(R.id.sw_switch)
        var editText=v.findViewById<EditText>(R.id.edit_text)
        var title=v.findViewById<TextView>(R.id.tv_title)

    }


    override fun getItemViewType(position: Int): Int {
        if (list[position].type == "PHOTO") {
            return 0
        } else if (list[position].type == "SINGLE_CHOICE") {
            return 1
        } else {
            return 2
        }
    }

    override fun getItemCount(): Int {
        return list.size;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 0) {
            return ImageViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.image_template, parent, false)
            )
        } else if (viewType == 1) {
            return ChoiceViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.options_layout, parent, false)
            )
        } else {
            return CommentViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.comment_template, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        when (holder) {
            is ImageViewHolder -> {
                holder.number.text = "${position + 1}"
                holder.placeholder.setOnClickListener {
                    (mContext as MainActivity).selectImage(list[position].id)
                }
                if(map.containsKey(list[position].id) && map.get(list[position].id)!=null){
                    holder.placeholder.visibility=View.GONE
                    holder.clear.visibility=View.VISIBLE
                    holder.image.visibility=View.VISIBLE
                    holder.image.setImageURI(map.get(list[position].id) as Uri)
                }
                else{
                    holder.placeholder.visibility=View.VISIBLE
                    holder.clear.visibility=View.GONE
                    holder.image.visibility=View.GONE
                }
                holder.clear.setOnClickListener {
                    (mContext as MainActivity).deleteImage(list[position].id)
                        map.remove(list[position].id)
                    notifyItemChanged(position)
                }

            }
            is ChoiceViewHolder -> {
                holder.number.text = "${position + 1}"
                holder.title.text = list[position].title
                holder.group.removeAllViews()
                list[position].dataMap?.options.map {
                    var radioButton = RadioButton(mContext)
                    radioButton.setText(it)
                    holder.group.addView(radioButton)
                }
                if(map.containsKey(list[position].id)){
                    holder.group.children.iterator().forEach {
                        if((it as RadioButton).text==map.get(list[position].id)){
                            it.isChecked=true
                        }
                    }
                }
                holder.group.setOnCheckedChangeListener { radioGroup, i ->
                    var button=radioGroup.findViewById<RadioButton>(i)
                    var value=button.text
                    map.set(list[position].id,value.toString())
                    (mContext as MainActivity).choice(list[position].id,value.toString())
                }


            }
            else -> {
                (holder as CommentViewHolder).number.text = "${position + 1}"
                (holder as CommentViewHolder).title.text = list[position].title
                holder.editText.visibility=if (holder.switch.isChecked) View.VISIBLE else View.GONE

                if(map.containsKey(list[position].id)){
                    holder.editText.text=Editable.Factory.getInstance().newEditable(map.get(list[position].id) as String)
                }
                holder.switch.setOnCheckedChangeListener { compoundButton, b ->
                    holder.editText.visibility = if (b) View.VISIBLE else View.GONE
                    if(!b) {
                        (mContext as MainActivity).comment(list[position].id, "")
                        map.set(list[position].id,"")
                        notifyItemChanged(position)
                    }

                }
                holder.editText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        map.set(list[position].id,p0.toString())
                        (mContext as MainActivity).comment(list[position].id,p0.toString())


                    }

                    override fun afterTextChanged(p0: Editable?) {

                    }

                })
            }
        }
    }

    interface ActionListener{
        fun selectImage(id:String)
        fun comment(id:String,comment:String)
        fun choice(id:String,selectedChoice:String)
        fun deleteImage(id: String)
    }
}