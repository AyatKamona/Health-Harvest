package csci5708.mobilecomputing.healthharvest.Adapters
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import csci5708.mobilecomputing.healthharvest.DataModels.FoodItem
import csci5708.mobilecomputing.healthharvest.R


class FoodAdapter(private val foodList: List<FoodItem>) :
    RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foodName: TextView = itemView.findViewById(R.id.foodNameTextView)
        val dateTaken: TextView = itemView.findViewById(R.id.dateTakenTextView)
        val calories: TextView = itemView.findViewById(R.id.caloriesTextView)
        val option: TextView = itemView.findViewById(R.id.optionTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
        return FoodViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val currentItem = foodList[position]
        holder.foodName.text = currentItem.name
        holder.dateTaken.text = currentItem.dateTaken
        holder.calories.text = currentItem.calories.toString()
        holder.option.text = currentItem.option
    }

    override fun getItemCount() = foodList.size
}
