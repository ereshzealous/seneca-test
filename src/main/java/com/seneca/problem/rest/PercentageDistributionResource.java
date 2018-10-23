package com.seneca.problem.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seneca.problem.rest.ws.WSVendorDistributeRequest;
import com.seneca.problem.rest.ws.WSVendorDistributionResponse;
import com.seneca.problem.rest.ws.WSVendorInformation;
import com.seneca.problem.service.PercentageDistributionService;
import com.seneca.problem.service.vo.VendorDetailInformationVO;
import com.seneca.problem.service.vo.VendorRequestVO;

@RestController
@RequestMapping("/api")
public class PercentageDistributionResource {

	@Autowired
	private PercentageDistributionService distributionService;

	@PostMapping("/distribute")
	private ResponseEntity<WSVendorDistributionResponse> distributeWorkload(@RequestBody WSVendorDistributeRequest request) throws Exception {
		List<VendorDetailInformationVO> detailInformationVOs = distributionService.distributeWorkloads(new VendorRequestVO(request, request.getConsiderPriority()));
		WSVendorDistributionResponse response = new WSVendorDistributionResponse();
		response.setDistributions(detailInformationVOs.stream().map(WSVendorInformation::new).collect(Collectors.toList()));
		return new ResponseEntity<WSVendorDistributionResponse>(response, HttpStatus.OK);

	}
}
