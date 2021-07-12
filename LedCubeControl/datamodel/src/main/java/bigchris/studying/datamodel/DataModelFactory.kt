package bigchris.studying.datamodel

class DataModelFactory {

    companion object {
        private val dataModel: DataModel by lazy {DataModel()}

        fun dataModel() : DataModelProxy =  dataModel

    }

}