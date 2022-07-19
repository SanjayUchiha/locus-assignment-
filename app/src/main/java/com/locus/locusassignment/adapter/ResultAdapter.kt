package com.locus.locusassignment.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.locus.locusassignment.R
import com.locus.locusassignment.model.DataModel

class ResultAdapter(private val mContext: Context,private val list:ArrayList<DataModel>,private val map:HashMap<String,Any>): RecyclerView.Adapter<ResultAdapter.ResultHolder>() {


    class ResultHolder(v: View):RecyclerView.ViewHolder(v){
        var title=v.findViewById<TextView>(R.id.tv_title)
        var image=v.findViewById<ImageView>(R.id.iv_image)
        var answer=v.findViewById<TextView>(R.id.tv_answer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultHolder {
        return ResultHolder(LayoutInflater.from(mContext).inflate(R.layout.result_template,parent,false))
    }

    override fun onBindViewHolder(holder: ResultHolder, position: Int) {
        holder.title.text=list[position].id
        if(map.containsKey(list[position].id)){
            if(list[position].id.contains("pic")){
                holder.answer.visibility=View.GONE
                holder.image.visibility=View.VISIBLE
                holder.image.setImageURI(map.get(list[position].id) as Uri)
            }
            else{
                holder.answer.visibility=View.VISIBLE
                holder.image.visibility=View.GONE
                holder.answer.text=map.get(list[position].id) as String
            }
        }
        else{
            holder.image.visibility=View.GONE
            holder.answer.visibility=View.VISIBLE
            holder.answer.text="No Answer Given"
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}