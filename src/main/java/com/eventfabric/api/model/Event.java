package com.eventfabric.api.model;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


public class Event {
	private String channel;
	private String bucket;
	private String key;
	private String value;
	private static ObjectMapper mapper = new ObjectMapper();

	public Event(String channel) {
		this.channel = channel;
	}

	public Event(String channel, String value) {
		this.channel = channel;
		this.value = value;
	}

	public Event(String channel, Map<String, Object> value) throws JsonGenerationException, JsonMappingException, IOException {

		this.channel = channel;
		// Auto-convert Map to a JSON string...
		StringWriter str = new StringWriter();
		mapper.writeValue(str, value);
		this.value = str.toString();
	}
	
	public Event(String channel, ObjectNode value) throws JsonProcessingException {
		this.channel = channel;
		this.value = mapper.writeValueAsString(value);
	}

	public Event(String channel, String value, String bucket) {
		this(channel, value);
		setBucket(bucket);
	}

	public Event(String channel, Map<String, Object> value, String bucket)
			throws IOException {
		this(channel, value);
		setBucket(bucket);
	}
	
	public Event(String channel, ObjectNode value, String bucket)
			throws IOException {
		this(channel, value);
		setBucket(bucket);
	}

	public ObjectNode toJSON() throws JsonProcessingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode root = mapper.createObjectNode();

		root.put("channel", channel);
		root.put("value", value);
		root.put("username", bucket);

		return (ObjectNode)mapper.readTree(value);
	}

	public String getChannel() {
		return this.channel;
	}

	public String getValue() {
		return this.value;
	}

	public String getBucket() {
		return bucket;
	}

	public String getKey() {
		return this.key;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
