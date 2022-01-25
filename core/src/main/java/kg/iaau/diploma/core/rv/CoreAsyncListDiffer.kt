package kg.iaau.diploma.core.rv

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class CoreAsyncListDiffer<T, VH: RecyclerView.ViewHolder>: RecyclerView.Adapter<VH>() {

    private val diffCallback: DiffUtil.ItemCallback<T> =
        object : DiffUtil.ItemCallback<T>() {
            override fun areItemsTheSame(oldItem: T, newItem: T) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: T, newItem: T) =
                oldItem.hashCode() == oldItem.hashCode()
        }

    val mDiffer: AsyncListDiffer<T> = AsyncListDiffer(this, diffCallback)
}
