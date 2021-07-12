package bigchris.studying.ledcubecontrol.customwidgets

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatCheckBox
import bigchris.studying.ledcubecontrol.R

class GridCheckBox(context: Context, attrs: AttributeSet) : AppCompatCheckBox(context, attrs) {
    var column: Int = -1
        private set
    var row: Int = -1
        private set

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.GridCheckBox, 0, 0).apply {
            try {
                column = getInt(R.styleable.GridCheckBox_column, 0)
                row = getInt(R.styleable.GridCheckBox_row, 0)
            } finally {
                recycle()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(width, width)
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

}