package com.seneca.problem.service.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class VendorDetailInformationVO extends VendorInformationVO {
	private Integer roundOffValue;
	private Double actualValue;
	private Double decimalValue;
}
