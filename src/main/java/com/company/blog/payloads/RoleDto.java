package com.company.blog.payloads;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RoleDto {
	private Integer id;
	private String name;
	
	@JsonIgnore
	public Integer getId() {
		return this.id;
	}

	@JsonProperty
	public void setId(Integer id) {
		this.id = id;
	}
}
