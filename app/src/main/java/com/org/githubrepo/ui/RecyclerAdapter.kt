package com.org.githubrepo.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.org.githubrepo.R
import com.org.githubrepo.room.GithubRepo

class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

    var item = ArrayList<GithubRepo>()

    fun setData(item: ArrayList<GithubRepo>) {
        this.item = item
        notifyDataSetChanged()
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val repoName: TextView = view.findViewById(R.id.name)
        val decription: TextView = view.findViewById(R.id.description)
        val imageView: ImageView = view.findViewById(R.id.avatar_url)
        fun bind(item: GithubRepo, context: Context) {
            repoName.setText(item.full_name)
            decription.setText(item.description)
            Glide.with(context).load(item.avatar_url).placeholder(R.drawable.placeholder)
                .into(imageView);

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.repo_item_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(item.get(position), holder.itemView.context)
    }

    override fun getItemCount(): Int {
        return item.size
    }
}