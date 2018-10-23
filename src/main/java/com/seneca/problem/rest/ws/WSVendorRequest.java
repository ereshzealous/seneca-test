package com.seneca.problem.rest.ws;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WSVendorRequest {
	private String name;
	private Integer percentage;
	private Integer priority;
}
