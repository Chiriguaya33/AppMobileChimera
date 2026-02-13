package com.matrix.appmobilechimera.ui.adapter

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.matrix.appmobilechimera.R
import com.matrix.appmobilechimera.model.Discussion

class DiscussionAdapter(
    private val discussions: List<Discussion>,
    private val onItemClick: (Discussion) -> Unit
) : RecyclerView.Adapter<DiscussionAdapter.DiscussionViewHolder>() {

    class DiscussionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvDiscussionTitle)
        val tvAuthor: TextView = view.findViewById(R.id.tvDiscussionAuthor)
        val tvSnippet: TextView = view.findViewById(R.id.tvDiscussionSnippet)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscussionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_discussion, parent, false)
        return DiscussionViewHolder(view)
    }

    override fun onBindViewHolder(holder: DiscussionViewHolder, position: Int) {
        val discussion = discussions[position]
        holder.tvTitle.text = discussion.title
        holder.tvAuthor.text = "Por: ${discussion.author}"

        // Pulido: Limpiamos el HTML del mensaje para mostrar una vista previa limpia
        holder.tvSnippet.text = Html.fromHtml(discussion.message, Html.FROM_HTML_MODE_COMPACT)

        holder.itemView.setOnClickListener { onItemClick(discussion) }
    }

    override fun getItemCount() = discussions.size
}