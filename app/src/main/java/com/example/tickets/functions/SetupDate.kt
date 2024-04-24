import android.app.DatePickerDialog
import android.content.Context
import android.widget.Button
import java.text.SimpleDateFormat
import java.util.*

fun setupDatePicker(context: Context, button: Button) {
    val calendar = Calendar.getInstance()

    val dateSetListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val dateFormat = SimpleDateFormat("dd MMM, E", Locale("ru"))
            val formattedDate = dateFormat.format(calendar.time)
            button.text = formattedDate
        }

    button.setOnClickListener {
        DatePickerDialog(
            context,
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    // Set default date
    val defaultDateFormat = SimpleDateFormat("dd MMM, E", Locale("ru"))
    button.text = defaultDateFormat.format(calendar.time)
}


