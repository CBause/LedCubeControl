package bigchris.studying.ledcubecontrol

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import bigchris.studying.datamodel.DataModelFactory
import bigchris.studying.datamodel.DataModelProxy
import bigchris.studying.ledcubecontrol.customwidgets.LedMatrixWidget
import bigchris.studying.ledcubecontrol.ledbaselayer.LedBaseLayerViewModel

class ViewModelFactory(private val getMainViewModel: Boolean) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        val mainViewModel: MainViewModel by lazy {MainViewModel()}
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>) = with(modelClass) {
        when {
            getMainViewModel || isAssignableFrom(MainViewModel::class.java) -> ViewModelFactory.mainViewModel
            isAssignableFrom(LedBaseLayerViewModel::class.java) -> LedBaseLayerViewModel(DataModelFactory.dataModel())
            else -> throw Exception("Undefined viewmodel")
        }
    } as T
}

fun Fragment.getViewModelFactory(getMainViewModel: Boolean = false) = ViewModelFactory(getMainViewModel)
fun AppCompatActivity.getViewModelFactory(getMainViewModel: Boolean = false) = ViewModelFactory(getMainViewModel)