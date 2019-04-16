package com.jerry.base.adapter

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

abstract class BaseListAdapter<T> :RecyclerView.Adapter<RecyclerViewHolder> {

    val mContext:Context
    val mDatas :List<T>

    var mOnItemClickListener:RecyclerViewHolder.OnItemClickListener?=null

    constructor(context: Context,datas:List<T>){
        mContext = context
        mDatas = datas
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(mContext).inflate(getLayoutId(),parent,false)
        val holder = RecyclerViewHolder(mContext,view)
        view.setOnClickListener {
            mOnItemClickListener?.onItemClick(holder.adapterPosition)
        }
        return holder
    }

    override fun getItemCount(): Int {
        return mDatas.size
    }

    @LayoutRes
    abstract fun getLayoutId():Int

    fun setOnItemClickListener(listener:RecyclerViewHolder.OnItemClickListener){
        this.mOnItemClickListener = listener
    }
}


class RecyclerViewHolder :RecyclerView.ViewHolder{

    val mSparseView:SparseArray<View> = SparseArray()
    val mContext:Context
    val mContentView:View
    constructor(context: Context,contentView:View):super(contentView){
        mContext = context
        mContentView = contentView
    }

    fun <T:View> getView(@IdRes viewId:Int):T{

        return if(mSparseView.indexOfKey(viewId)!=-1){
            mSparseView[viewId] as T
        }else{
            val view = mContentView.findViewById<View>(viewId)
            mSparseView.put(viewId,view)
            view as T
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position:Int)
    }


}