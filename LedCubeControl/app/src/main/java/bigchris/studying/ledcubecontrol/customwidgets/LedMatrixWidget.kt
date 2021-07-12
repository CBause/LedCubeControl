package bigchris.studying.ledcubecontrol.customwidgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.CompoundButton
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.forEach
import bigchris.studying.ledcubecontrol.R

class LedMatrixWidget(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) , CompoundButton.OnCheckedChangeListener {
    private lateinit var ledGridLayout: GridLayout
    private lateinit var ledLayerIndicatorTextView: TextView
    private var layer: Int = -1
    private lateinit var ledToggleListener: LedToggleListener

    interface LedToggleListener {

        fun onLedToggle(toggleState: Boolean, coordinateX: Int, coordinateY: Int, layer: Int)

        fun onLedState(coordinateX: Int, coordinateY: Int, layer: Int) : Boolean
    }

    init {
        View.inflate(context, R.layout.led_layer, this)
        context.theme.obtainStyledAttributes(attrs, R.styleable.LedMatrixWidget, 0, 0).apply {
            try {
                layer = getInt(R.styleable.LedMatrixWidget_layer, 0)
            } finally {
                recycle()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val contentMargin: Int = resources.getDimension(R.dimen.led_matrix_content_margin).toInt()
        val layerIndicatorHeight: Int = resources.getDimension(R.dimen.layer_indicator_heigt).toInt()
        val height: Int = (width + contentMargin + layerIndicatorHeight)
        setMeasuredDimension(width, height)
        layoutParams = LinearLayoutCompat.LayoutParams(width, height)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (buttonView != null) {
            (buttonView as GridCheckBox).run {
                ledToggleListener.onLedToggle(this.isChecked, this.column, this.row, layer)
            }
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        setupGridLayout()
        setupLayerIndicator()
    }

    fun setupGridLayout() {
        ledGridLayout = findViewById(R.id.ledGridLayout)
        ledGridLayout.forEach {
            (it as GridCheckBox).setOnCheckedChangeListener(this)
        }
    }

    fun setupLayerIndicator() {
        ledLayerIndicatorTextView = findViewById(R.id.ledLayerTextview)
        ledLayerIndicatorTextView.setText(resources.getString(R.string.layer_indicator, layer + 1))
    }

    fun registerLedToggleListener(toggleListener: LedToggleListener) {
        ledToggleListener = toggleListener
    }

    fun updateLeds() {
        ledGridLayout.forEach {
            (it as GridCheckBox).isChecked = ledToggleListener.onLedState(it.column, it.row, layer)
        }
    }

}