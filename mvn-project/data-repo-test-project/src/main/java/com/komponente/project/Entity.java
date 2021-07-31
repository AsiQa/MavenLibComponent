package com.komponente.project;

import java.util.Map;
import java.util.UUID;

public class Entity implements Comparable<Entity> {

	private String id;
	private String naziv;

	private Map<String, String> simpleProperties;
	private Map<String, Entity> entityProperties;

	public Entity() {

	}

	public Entity(String id, String naziv, Map<String, String> simpleProperties, Map<String, Entity> entityProperties) {
		this.id = id;
		this.naziv = naziv;
		this.simpleProperties = simpleProperties;
		this.entityProperties = entityProperties;
	}

//	this.id = UUID.randomUUID().toString();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public Map<String, String> getSimpleProperties() {
		return simpleProperties;
	}

	public void setSimpleProperties(Map<String, String> simpleProperties) {
		this.simpleProperties = simpleProperties;
	}

	public Map<String, Entity> getEntityProperties() {
		return entityProperties;
	}

	public void setEntityProperties(Map<String, Entity> entityProperties) {
		this.entityProperties = entityProperties;
	}

	@Override
	public String toString() {
		return "Entity{" +
				"id='" + id + '\'' +
				", naziv='" + naziv + '\'' +
				", simpleProperties=" + simpleProperties +
				", entityProperties=" + entityProperties +
				'}';
	}

	@Override
	public int compareTo(Entity o) {
		return 0;
	}
}
