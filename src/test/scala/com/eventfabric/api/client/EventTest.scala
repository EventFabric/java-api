import org.scalatest._
import com.eventfabric.model.Event

class StackSpec extends FlatSpec with ShouldMatchers {

  "An Event" should "have a value and a channel" in {
    val value = new java.util.HashMap[String, Object]()
    val event = new Event("my.channel", value)

    event.getChannel should be ("my.channel")
    event.toJSONString should be ("{\"channel\":\"my.channel\",\"value\":{}}")
  }
}

