package com.seneca.problem.service.vo;

import com.seneca.problem.rest.ws.WSVendorRequest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class VendorInformationVO {
	private String name;
	private Integer percentage;
	private Integer priority;

	public VendorInformationVO(WSVendorRequest request) {
		this.name = request.getName();
		this.percentage = request.getPercentage();
		this.priority = request.getPriority();
	}

	public VendorInformationVO(WSVendorRequest request, Integer priority) {
		this.name = request.getName();
		this.percentage = request.getPercentage();
		this.priority = priority;
	}
}
