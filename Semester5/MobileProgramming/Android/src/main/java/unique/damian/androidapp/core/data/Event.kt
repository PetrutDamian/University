package unique.damian.androidapp.core.data
enum class OfflineStatus{
    DELETED,CREATED,UPDATED
}
data class Event(
    val _id:String,
    var title:String,
    var description:String,
    var location:String,
    var reservationRequired:Boolean,
    var price: Int,
    var offline:OfflineStatus? = null
){
    override fun toString():String = title
}