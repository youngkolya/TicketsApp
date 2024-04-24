import android.text.InputFilter
import android.text.Spanned
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText

fun EditText.kirilica() {
    // Ограничение ввода кириллических символов
    filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
        val regex = Regex("[а-яёА-ЯЁ\\s]")
        if (!source.toString().matches(regex)) {
            return@InputFilter ""
        }
        null
    })

    // Обработка нажатий клавиш для предотвращения вставки некириллических символов
    setOnKeyListener { _, keyCode, event ->
        if (event.action == KeyEvent.ACTION_DOWN && keyCode != KeyEvent.KEYCODE_DEL) {
            if (event.isShiftPressed || event.isCtrlPressed) {
                // Пропускаем, если нажата клавиша Shift или Ctrl
                return@setOnKeyListener false
            }
            if (!event.isPrintingKey) {
                // Пропускаем, если это не печатный символ (например, клавиши управления)
                return@setOnKeyListener false
            }
            val newChar = event.unicodeChar.toChar()
            if (newChar !in 'а'..'я' && newChar !in 'А'..'Я' && newChar != 'ё' && newChar != 'Ё') {
                // Пропускаем, если новый символ не кириллический
                return@setOnKeyListener true
            }
        }
        false
    }

    // Обработка действия "Готово" на клавиатуре для скрытия клавиатуры
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            clearFocus()
            return@setOnEditorActionListener true
        }
        false
    }
}
