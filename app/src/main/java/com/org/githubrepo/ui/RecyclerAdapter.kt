package com.org.githubrepo.ui

import android.annotation.SuppressLint
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

    private var item = ArrayList<GithubRepo>()


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val repoName: TextView = view.findViewById(R.id.name)
        private val description: TextView = view.findViewById(R.id.description)
        private val imageView: ImageView = view.findViewById(R.id.avatar_url)
        fun bind(item: GithubRepo, context: Context) {
            repoName.text = item.full_name
            description.text = item.description
            Glide.with(context).load(item.avatar_url).placeholder(R.drawable.placeholder)
                .into(imageView)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.repo_item_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(item[position], holder.itemView.context)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(githubRepoItems: ArrayList<GithubRepo>) {
        item.clear()
        item = githubRepoItems
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setFilter(githubRepoList: List<GithubRepo>) {
        item.clear()
        item.addAll(githubRepoList)
        notifyDataSetChanged()
    }

}