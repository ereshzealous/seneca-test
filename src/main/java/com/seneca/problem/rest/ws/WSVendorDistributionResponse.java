package com.seneca.problem.rest.ws;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WSVendorDistributionResponse {
	private List<WSVendorInformation> distributions;
}
