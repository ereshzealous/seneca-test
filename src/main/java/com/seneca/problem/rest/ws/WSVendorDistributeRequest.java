package com.seneca.problem.rest.ws;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WSVendorDistributeRequest {
	private List<WSVendorRequest> distributions = new ArrayList<WSVendorRequest>();
	private Integer totalWorkload;
	private Boolean considerPriority = false;
}
