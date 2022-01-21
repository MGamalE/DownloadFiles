package com.example.downloadfiles.presentation.features.fileslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.downloadfiles.R
import com.example.downloadfiles.databinding.ItemFilesListBinding
import com.example.downloadfiles.entity.uifiles.DownloadStatus
import com.example.downloadfiles.entity.uifiles.FilesUiData

class FilesListAdapter(
    private val clickListener: (fileResponse: FilesUiData) -> Unit,
    private val items: MutableList<FilesUiData>
) : RecyclerView.Adapter<FilesListAdapter.ViewHolder>() {

    fun insertNewItems(list: List<FilesUiData>) {
        items.addAll(list)
        notifyDataSetChanged()
    }

    fun updateFiles(updatedFileItem: FilesUiData, position: Int) {
        items[position] = updatedFileItem
        notifyItemChanged(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, clickListener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, position)
    }

    class ViewHolder private constructor(
        private val binding: ItemFilesListBinding,
        private val clickListener: (fileResponse: FilesUiData) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FilesUiData, position: Int) {

            drawActions(item, binding)

            listenToItemClick(itemView, item, position)

        }

        private fun drawActions(item: FilesUiData, binding: ItemFilesListBinding) {
            binding.tvFileTitle.text = item.name

            when (item.status) {
                DownloadStatus.PENDING -> {
                    binding.imgDownloadStatus.visibility = View.VISIBLE
                    binding.progress.visibility = View.GONE
                    binding.imgDownloadStatus.apply {
                        setImageResource(R.drawable.ic_download)
                    }
                }
                DownloadStatus.PROGRESS -> {
                    binding.imgDownloadStatus.visibility = View.GONE
                    binding.progress.visibility = View.VISIBLE
                }
                DownloadStatus.COMPLETED -> {
                    binding.imgDownloadStatus.visibility = View.VISIBLE
                    binding.progress.visibility = View.GONE
                    binding.imgDownloadStatus.apply {
                        setImageResource(R.drawable.ic_complete)
                    }
                }
            }
        }

        private fun listenToItemClick(itemView: View, item: FilesUiData, position: Int) {
            itemView.setOnClickListener {
                when (item.status) {
                    DownloadStatus.PENDING -> {
                        clickListener(
                            FilesUiData(
                                id = item.id,
                                name = item.name,
                                type = item.type,
                                url = item.url,
                                status = DownloadStatus.PROGRESS,
                                position = position
                            )
                        )
                        binding.imgDownloadStatus.visibility = View.GONE
                        binding.progress.visibility = View.VISIBLE
                    }
                    DownloadStatus.PROGRESS -> {

                    }
                    DownloadStatus.COMPLETED -> {

                    }
                }

            }
        }

        companion object {
            fun from(
                parent: ViewGroup,
                clickListener: (fileResponse: FilesUiData) -> Unit
            ): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemFilesListBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding, clickListener)
            }
        }

    }

}