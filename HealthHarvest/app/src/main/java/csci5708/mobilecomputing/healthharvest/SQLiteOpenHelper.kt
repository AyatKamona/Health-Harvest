package csci5708.mobilecomputing.healthharvest
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import csci5708.mobilecomputing.healthharvest.DataModels.FoodItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FoodDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "food_database"
        private const val TABLE_NAME = "food_items"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_DATE_TAKEN = "date_taken"
        private const val COLUMN_CALORIES = "calories"
        private const val COLUMN_OPTION = "option"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_NAME TEXT, $COLUMN_DATE_TAKEN TEXT, $COLUMN_CALORIES INTEGER, $COLUMN_OPTION TEXT)"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertFoodItem(foodItem: FoodItem): Long {
        val contentValues = ContentValues()
        contentValues.put(COLUMN_NAME, foodItem.name)
        contentValues.put(COLUMN_DATE_TAKEN, foodItem.dateTaken)
        contentValues.put(COLUMN_CALORIES, foodItem.calories)


        val db = this.writableDatabase
        return db.insert(TABLE_NAME, null, contentValues)
    }

    fun getAllFoodItems(): List<FoodItem> {
        val foodList = mutableListOf<FoodItem>()
        val db = this.readableDatabase
        val columns = arrayOf(COLUMN_ID, COLUMN_NAME, COLUMN_DATE_TAKEN, COLUMN_CALORIES, COLUMN_OPTION)

        val cursor: Cursor? = db.query(TABLE_NAME, columns, null, null, null, null, null)
        cursor?.let {
            val idIndex = it.getColumnIndex(COLUMN_ID)
            val nameIndex = it.getColumnIndex(COLUMN_NAME)
            val dateTakenIndex = it.getColumnIndex(COLUMN_DATE_TAKEN)
            val caloriesIndex = it.getColumnIndex(COLUMN_CALORIES)
            val optionIndex = it.getColumnIndex(COLUMN_OPTION)

            while (it.moveToNext()) {
                val id = if (idIndex != -1) it.getLong(idIndex) else -1
                val name = if (nameIndex != -1) it.getString(nameIndex) else ""
                val dateTaken = if (dateTakenIndex != -1) it.getString(dateTakenIndex) else ""
                val calories = if (caloriesIndex != -1) it.getInt(caloriesIndex) else 0
                val option = if (optionIndex != -1) it.getString(optionIndex) else ""

                val foodItem = FoodItem(id, name, dateTaken, calories)
                foodList.add(foodItem)
            }
            it.close()
        }
        return foodList
    }


    fun deleteFoodItem(id: Long): Int {
        val db = this.writableDatabase
        val selection = "$COLUMN_ID = ?"
        val selectionArgs = arrayOf(id.toString())

        // Delete the row with the given id from the table
        return db.delete(TABLE_NAME, selection, selectionArgs)
    }

    fun getFoodItem(id: Long): FoodItem? {
        val db = this.readableDatabase
        val columns = arrayOf(COLUMN_ID, COLUMN_NAME, COLUMN_DATE_TAKEN, COLUMN_CALORIES, COLUMN_OPTION)

        val selection = "$COLUMN_ID = ?"
        val selectionArgs = arrayOf(id.toString())

        val cursor: Cursor? = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null)
        cursor?.let {
            if (it.moveToFirst()) {
                val idIndex = it.getColumnIndex(COLUMN_ID)
                val nameIndex = it.getColumnIndex(COLUMN_NAME)
                val dateTakenIndex = it.getColumnIndex(COLUMN_DATE_TAKEN)
                val caloriesIndex = it.getColumnIndex(COLUMN_CALORIES)
                val optionIndex = it.getColumnIndex(COLUMN_OPTION)

                val id = if (idIndex != -1) it.getLong(idIndex) else -1
                val name = if (nameIndex != -1) it.getString(nameIndex) else ""
                val dateTaken = if (dateTakenIndex != -1) it.getString(dateTakenIndex) else ""
                val calories = if (caloriesIndex != -1) it.getInt(caloriesIndex) else 0
                val option = if (optionIndex != -1) it.getString(optionIndex) else ""

                return FoodItem(id, name, dateTaken, calories)
            }
            it.close()
        }
        return null // Return null if the food item with the given id is not found
    }

    // Function to update a FoodItem in the database
    fun updateFoodItem(foodItem: FoodItem): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_NAME, foodItem.name)
        contentValues.put(COLUMN_DATE_TAKEN, foodItem.dateTaken)
        contentValues.put(COLUMN_CALORIES, foodItem.calories)

        val selection = "$COLUMN_ID = ?"
        val selectionArgs = arrayOf(foodItem.id.toString())

        return db.update(TABLE_NAME, contentValues, selection, selectionArgs)
    }

    fun getTotalCaloriesToday(): Int {
        val db = this.readableDatabase
        val columns = arrayOf("SUM($COLUMN_CALORIES)")

        // Get the current date in yyyy-MM-dd format
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val selection = "$COLUMN_DATE_TAKEN = ?"
        val selectionArgs = arrayOf(currentDate)

        val cursor: Cursor? = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null)

        cursor?.let {
            if (it.moveToFirst()) {
                val totalCalories = it.getInt(0)
                it.close()
                return totalCalories
            }
            it.close()
        }

        return 0 // Return 0 if no calories are consumed today or an error occurs
    }
}
