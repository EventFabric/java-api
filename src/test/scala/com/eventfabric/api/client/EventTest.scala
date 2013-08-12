import org.scalatest._
import com.eventfabric.model.Event
import com.eventfabric.api.client.{EventClient, Response, EndPointInfo}

class EventClientTest(username: String, password: String) extends EventClient(username, password) {
  var posts = List[(String, String)]()
  override def post(url: String, data: String): Response = {
    posts = posts ++ List((url, data))

    new Response("", 200, null)
  }
}

class EventSpec extends FlatSpec with ShouldMatchers {

  def createEvent(): Event = {
    val value = new java.util.LinkedHashMap[String, Object]()
    value.put("count", 4:java.lang.Integer)
    value.put("price", 12.3:java.lang.Double)
    value.put("yes", true:java.lang.Boolean)
    val event = new Event("my.channel", value)

    event
  }

  "An Event" should "have a value and a channel" in {
    val value = new java.util.HashMap[String, Object]()
    val event = new Event("my.channel", value)

    event.getChannel should be ("my.channel")
    event.toJSONString should be ("{\"channel\":\"my.channel\",\"value\":{}}")
  }

  it should "support a non empty value and a channel" in {
    val event = createEvent()

    event.toJSONString should be ("{\"channel\":\"my.channel\",\"value\":{\"count\":4,\"price\":12.3,\"yes\":true}}")
  }

  "An EventClient" should "send an event to the API" in {
    val event = createEvent()
    val client = new EventClientTest("mariano", "secret")

    client.getCredentials.getUsername should be ("mariano")
    client.getCredentials.getPassword should be ("secret")

    client.send(event)
    client.posts.size should be (1)
    val post = client.posts.head
    val eventEndpoint = new EndPointInfo(EndPointInfo.DEFAULT_API_ENDPOINT_EVENT)
    post._1 should be (eventEndpoint.toString)
    post._2 should be ("{\"channel\":\"my.channel\",\"value\":{\"count\":4,\"price\":12.3,\"yes\":true}}")
  }
}

