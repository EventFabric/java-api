package com.eventfabric.api.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

public class Event {
	private String channel;
	private String bucket;
	private ObjectNode value;

	public Event(String channel) {
		this.channel = channel;
	}

	/**
	 * Create an object based on the JSON Tree Model (used internally).
	 * 
	 * @see <a href=
	 *      "http://wiki.fasterxml.com/JacksonInFiveMinutes#Tree_Model_Example"
	 *      >Jackson's Tree Model example</a>
	 */
	public Event(String channel, ObjectNode value) {
		this.channel = channel;
		this.value = value;
	}

	/**
	 * Create an object based on the JSON Simple Data Binding.
	 * 
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 * 
	 * @see <a href=
	 *      "http://wiki.fasterxml.com/JacksonInFiveMinutes#Simple_Data_Binding_Example"
	 *      >Jacksons's Simple Data Binding example</a>
	 */
	public Event(String channel, Map<String, Object> value)
			throws JsonGenerationException, JsonMappingException, IOException {

		this.channel = channel;

		ObjectMapper mapper = new ObjectMapper();
		// Auto-convert Map to a JSON string...
		StringWriter str = new StringWriter();
		mapper.writeValue(str, value);

		// ... and parse it back into a tree node
		ObjectNode node = mapper.readValue(str.toString(), ObjectNode.class);
		this.value = node;
	}

	public Event(String channel, String value) throws JsonParseException,
			JsonMappingException, IOException {
		this.channel = channel;
		ObjectMapper mapper = new ObjectMapper();
		this.value = (ObjectNode) mapper.readTree(value);
	}

	public Event(String channel, String value, String bucket)
			throws JsonParseException, JsonMappingException, IOException {
		this(channel, value);
		setBucket(bucket);
	}

	public Event(String channel, Map<String, Object> value, String bucket)
			throws JsonParseException, JsonMappingException, IOException {
		this(channel, value);
		setBucket(bucket);
	}

	public Event(String channel, ObjectNode value, String bucket)
			throws JsonParseException, JsonMappingException, IOException {
		this(channel, value);
		setBucket(bucket);
	}

	public ObjectNode toJSON() {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode root = mapper.createObjectNode();

		root.put("channel", channel);
		root.put("value", value);
		root.put("username", bucket);

		return value;
	}

	public String toJSONString() throws IOException {
		ObjectNode json = toJSON();

		ObjectMapper mapper = new ObjectMapper();

		return mapper.writeValueAsString(json);
	}

	public String getChannel() {
		return this.channel;
	}

	public ObjectNode getValue() {
		return this.value;
	}

	public void loadValueFromFile(String path) throws JsonProcessingException,
			IOException {
		ObjectMapper mapper = new ObjectMapper();
		BufferedReader fileReader = new BufferedReader(new FileReader(path));
		this.value = (ObjectNode) mapper.readTree(fileReader);
	}

	public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}
}
