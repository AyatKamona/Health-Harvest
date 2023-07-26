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
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE IF NOT EXISTS $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_NAME TEXT, $COLUMN_DATE_TAKEN TEXT, $COLUMN_CALORIES INTEGER)"
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
        AppData.lastTimeFoodAdded = System.currentTimeMillis()

        val db = this.writableDatabase
        return db.insert(TABLE_NAME, null, contentValues)
    }

    fun getAllFoodItems(): List<FoodItem> {
        val foodList = mutableListOf<FoodItem>()
        val db = this.readableDatabase
        val columns = arrayOf(COLUMN_ID, COLUMN_NAME, COLUMN_DATE_TAKEN, COLUMN_CALORIES)

        val cursor: Cursor? = db.query(TABLE_NAME, columns, null, null, null, null, null)
        cursor?.let {
            val idIndex = it.getColumnIndex(COLUMN_ID)
            val nameIndex = it.getColumnIndex(COLUMN_NAME)
            val dateTakenIndex = it.getColumnIndex(COLUMN_DATE_TAKEN)
            val caloriesIndex = it.getColumnIndex(COLUMN_CALORIES)


            while (it.moveToNext()) {
                val id = if (idIndex != -1) it.getLong(idIndex) else -1
                val name = if (nameIndex != -1) it.getString(nameIndex) else ""
                val dateTaken = if (dateTakenIndex != -1) it.getString(dateTakenIndex) else ""
                val calories = if (caloriesIndex != -1) it.getInt(caloriesIndex) else 0


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

    fun getAllFoodNames(): Array<String> {
        val db = this.readableDatabase
        val columns = arrayOf(COLUMN_NAME)

        val cursor: Cursor? = db.query(TABLE_NAME, columns, null, null, null, null, null)
        cursor?.let {
            val nameIndex = it.getColumnIndex(COLUMN_NAME)
            val foodNames = mutableListOf<String>()

            while (it.moveToNext()) {
                val name = if (nameIndex != -1) it.getString(nameIndex) else ""
                foodNames.add(name)
            }

            it.close()
            return foodNames.toTypedArray()
        }

        return emptyArray() // Return an empty array if there are no food items in the database
    }


    fun getFoodItem(id: Long): FoodItem? {
        val db = this.readableDatabase
        val columns = arrayOf(COLUMN_ID, COLUMN_NAME, COLUMN_DATE_TAKEN, COLUMN_CALORIES)

        val selection = "$COLUMN_ID = ?"
        val selectionArgs = arrayOf(id.toString())

        val cursor: Cursor? = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null)
        cursor?.let {
            if (it.moveToFirst()) {
                val idIndex = it.getColumnIndex(COLUMN_ID)
                val nameIndex = it.getColumnIndex(COLUMN_NAME)
                val dateTakenIndex = it.getColumnIndex(COLUMN_DATE_TAKEN)
                val caloriesIndex = it.getColumnIndex(COLUMN_CALORIES)


                val id = if (idIndex != -1) it.getLong(idIndex) else -1
                val name = if (nameIndex != -1) it.getString(nameIndex) else ""
                val dateTaken = if (dateTakenIndex != -1) it.getString(dateTakenIndex) else ""
                val calories = if (caloriesIndex != -1) it.getInt(caloriesIndex) else 0


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

        val cursor: Cursor? = db.query(TABLE_NAME, columns, null, null, null, null, null)

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

class WaterDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "water_database"
        private const val TABLE_NAME = "water_items"
        private const val COLUMN_ID = "id"
        private const val COLUMN_DATE_TAKEN = "date_taken"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE IF NOT EXISTS $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_DATE_TAKEN TEXT)"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // This function inserts a WaterItem into the database with the date taken as the current date
    fun addWaterIntakeForToday(): Int {
        val contentValues = ContentValues()
        AppData.lastTimeWaterAdded = System.currentTimeMillis()
        contentValues.put(COLUMN_DATE_TAKEN, SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()))

        val db = this.writableDatabase
        return db.insert(TABLE_NAME, null, contentValues).toInt()
    }

    // This function removes the latest WaterItem from the database for the current date.
    // If there is no WaterItem for the current date, it does not do any deletion and returns 0.
    fun removeLatestWaterIntakeForToday(): Int {
        val db = this.writableDatabase
        val selection = "$COLUMN_DATE_TAKEN = ?"
        val selectionArgs = arrayOf(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()))

        // Get the latest WaterItem for the current date
        val cursor: Cursor? = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, "$COLUMN_ID DESC", "1")

        cursor?.let {
            if (it.moveToFirst()) {
                val idColumnIndex = it.getColumnIndex(COLUMN_ID)
                if (idColumnIndex == -1) {
                    it.close()
                    return 0
                }
                val id = it.getInt(idColumnIndex)
                it.close()
                return db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
            }
            it.close()
        }
        return 0 // Return 0 if there is no WaterItem for the current date
    }

    fun getTotalWaterIntakeForToday(): Int {
        val db = this.readableDatabase
        val columns = arrayOf("COUNT($COLUMN_ID)")

        // Get the current date in yyyy-MM-dd format
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val selection = "$COLUMN_DATE_TAKEN = ?"
        val selectionArgs = arrayOf(currentDate)

        val cursor: Cursor? = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null)

        cursor?.let {
            if (it.moveToFirst()) {
                val totalWaterIntakeForToday = it.getInt(0)
                it.close()
                return totalWaterIntakeForToday
            }
            it.close()
        }
        return 0 // Return 0 if no calories are consumed today or an error occurs
    }
}
