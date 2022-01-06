package pavel.ivanov.pictureoftheday.repository.recyclerlist

const val TYPE_EARTH =0
const val TYPE_MARS =1
const val TYPE_HEADER =3
data class Data(val id:Int,val someText: String = "Text", val someDescription: String? = "Description",val type:Int=TYPE_MARS)


data class Change<out T>(
    val oldData: T,
    val newData: T
)

fun <T> createCombinedPayload(payloads: List<Change<T>>): Change<T> {
    assert(payloads.isNotEmpty())
    val firstChange = payloads.first()
    val lastChange = payloads.last()

    return Change(firstChange.oldData, lastChange.newData)
}
