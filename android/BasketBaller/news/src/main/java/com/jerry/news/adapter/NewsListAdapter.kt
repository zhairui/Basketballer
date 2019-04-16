package com.jerry.news.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jerry.base.adapter.BaseListAdapter
import com.jerry.base.adapter.RecyclerViewHolder
import com.jerry.data.entity.NewsEntity
import com.jerry.news.R
import com.jerry.news.viewmodels.NewsListAdapterViewModel

class NewsListAdapter:ListAdapter<NewsEntity,NewsListAdapter.ViewHolder>(NewsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.news_item_list,parent,false
            )
        )
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).let {newsEntity ->
            with(holder){
                itemView.tag = newsEntity
                bind(newsEntity)
            }

        }
    }

    class ViewHolder(
        private val binding:com.jerry.news.databinding.NewsItemListBinding
    ):RecyclerView.ViewHolder(binding.root){

        fun bind(newsEntity: NewsEntity){
            with(binding){
                newsViewModel = NewsListAdapterViewModel(
                    itemView.context,
                    newsEntity
                )
            }
        }
    }

    class NewsDiffCallback:DiffUtil.ItemCallback<NewsEntity>(){
        override fun areItemsTheSame(oldItem: NewsEntity, newItem: NewsEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NewsEntity, newItem: NewsEntity): Boolean {
            return oldItem == newItem
        }

    }
}