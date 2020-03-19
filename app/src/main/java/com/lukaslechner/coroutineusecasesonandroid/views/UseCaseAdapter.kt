package com.lukaslechner.coroutineusecasesonandroid.views

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lukaslechner.coroutineusecasesonandroid.R

class UseCaseAdapter(
    private val useCaseCategory: UseCaseCategory,
    private val onUseCaseClick: (UseCase) -> Unit
) : RecyclerView.Adapter<UseCaseAdapter.ViewHolder>() {

    class ViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_item, parent, false) as TextView
        return ViewHolder(
            textView
        )
    }

    override fun getItemCount() = useCaseCategory.useCases.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = useCaseCategory.useCases[position].description
        holder.textView.setOnClickListener {
            onUseCaseClick(useCaseCategory.useCases[position])
        }
    }
}