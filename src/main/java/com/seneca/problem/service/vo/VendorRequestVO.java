package com.seneca.problem.service.vo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.seneca.problem.rest.ws.WSVendorDistributeRequest;
import com.seneca.problem.rest.ws.WSVendorRequest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VendorRequestVO {
	private List<VendorInformationVO> informations;
	private Integer totalWorkLoads;
	private Boolean considerPriority;

	public VendorRequestVO(WSVendorDistributeRequest request) {
		this.informations = request.getDistributions().stream().map(VendorInformationVO::new).collect(Collectors.toList());
		this.totalWorkLoads = request.getTotalWorkload();
		this.considerPriority = request.getConsiderPriority();
	}
	
	
	public VendorRequestVO(WSVendorDistributeRequest request, boolean value) {
		if (value) {
		List<WSVendorRequest> priorityRequests = request.getDistributions().stream().sorted(Comparator.comparing(WSVendorRequest::getPriority, Comparator.nullsLast(Comparator.naturalOrder())))
				.collect(Collectors.toList());
		List<VendorInformationVO> informationVOs = new ArrayList<VendorInformationVO>();
		Integer index = 0;
		for (WSVendorRequest vendorRequest : priorityRequests) {
			index ++;
			informationVOs.add(new VendorInformationVO(vendorRequest, index));
		}
		this.informations = informationVOs;
		this.totalWorkLoads = request.getTotalWorkload();
		this.considerPriority = request.getConsiderPriority();
		} else {
			new VendorRequestVO(request);
		}
	}
}
