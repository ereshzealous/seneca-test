package com.seneca.problem.rest.ws;

import com.seneca.problem.service.vo.VendorDetailInformationVO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WSVendorInformation {
	private String name;
	private Integer percentage;
	private Integer distribution;
	
	public WSVendorInformation(VendorDetailInformationVO informationVO) {
		this.name = informationVO.getName();
		this.percentage = informationVO.getPercentage();
		this.distribution = informationVO.getRoundOffValue();
	}
}
