import org.scalatest._
import com.eventfabric.model.Event

class StackSpec extends FlatSpec with ShouldMatchers {

  "An Event" should "have a value and a channel" in {
    val event = new Event("my.channel", null)

    event.toJSON should be ("{\"channel\": \"my.channel\", \"value\": null}")
  }
}

