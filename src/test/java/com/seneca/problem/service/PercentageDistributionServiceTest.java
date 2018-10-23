package com.seneca.problem.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import com.seneca.problem.exception.DataValidationException;
import com.seneca.problem.service.vo.VendorDetailInformationVO;
import com.seneca.problem.service.vo.VendorInformationVO;
import com.seneca.problem.service.vo.VendorRequestVO;

@RunWith(MockitoJUnitRunner.class)
public class PercentageDistributionServiceTest {

	@InjectMocks
	private PercentageDistributionService service;

	@Test
	public void testDistributeValidationException() throws Exception {
		VendorRequestVO requestVO = new VendorRequestVO();

		/**
		 * Case 1: totalWorkload null.
		 */
		validateError("provide total workloads", "totalWorkload", requestVO);

		/**
		 * Case 2 : vendor distributions empty.
		 */
		requestVO.setTotalWorkLoads(100);
		validateError("provide vendor distributions", "distributions", requestVO);

		/**
		 * Case 3 : Vendor distributions provided, but percentage not equal to 100.
		 */
		List<VendorInformationVO> informationVOs = new ArrayList<VendorInformationVO>();
		VendorInformationVO informationVO = new VendorInformationVO();
		informationVO.setName("Vendor 1");
		informationVO.setPercentage(45);
		informationVOs.add(informationVO);
		informationVO = new VendorInformationVO();
		informationVO.setName("Vendor 2");
		informationVO.setPercentage(30);
		informationVOs.add(informationVO);
		informationVO = new VendorInformationVO();
		informationVO.setName("Vendor 3");
		informationVO.setPercentage(10);
		informationVOs.add(informationVO);
		requestVO.setInformations(informationVOs);
		validateError("Percentage sum of all vendors should be 100. But actual is =>85", "totalPercentage", requestVO);
	}

	@Test
	public void testDistributeSuccessCase_withoutPriority() throws Exception {
		VendorRequestVO requestVO = new VendorRequestVO();

		/**
		 * Case 1 : evenly distributed workloads
		 */
		requestVO.setTotalWorkLoads(100);
		requestVO.setConsiderPriority(false);
		List<VendorInformationVO> informationVOs = new ArrayList<VendorInformationVO>();
		informationVOs.add(generateVendorInformationVO("Vendor 1", 45, null));
		informationVOs.add(generateVendorInformationVO("Vendor 2", 30, null));
		informationVOs.add(generateVendorInformationVO("Vendor 3", 10, null));
		informationVOs.add(generateVendorInformationVO("Vendor 4", 15, null));
		requestVO.setInformations(informationVOs);
		List<VendorDetailInformationVO> detailInformationVOs = service.distributeWorkloads(requestVO);
		List<VendorDetailInformationVO> expectedInformationVos = new ArrayList<VendorDetailInformationVO>();
		expectedInformationVos.add(generateVenforDetailInformation("Vendor 1", 45, 45));
		expectedInformationVos.add(generateVenforDetailInformation("Vendor 2", 30, 30));
		expectedInformationVos.add(generateVenforDetailInformation("Vendor 3", 10, 10));
		expectedInformationVos.add(generateVenforDetailInformation("Vendor 4", 15, 15));
		assertEquals(requestVO.getTotalWorkLoads().intValue(), detailInformationVOs.stream().mapToInt(VendorDetailInformationVO :: getRoundOffValue).sum());
		assertVendorDetailInformation(expectedInformationVos, detailInformationVOs);

		/**
		 * Case 2 : unevenly distributed workloads with decimals < 0.5 and > 0.5
		 */
		requestVO.setTotalWorkLoads(77);
		informationVOs.clear();
		informationVOs.add(generateVendorInformationVO("Vendor 1", 45, null));
		informationVOs.add(generateVendorInformationVO("Vendor 3", 10, null));
		informationVOs.add(generateVendorInformationVO("Vendor 4", 15, null));
		informationVOs.add(generateVendorInformationVO("Vendor 2", 30, null));
		requestVO.setInformations(informationVOs);
		detailInformationVOs.clear();
		detailInformationVOs = service.distributeWorkloads(requestVO);
		expectedInformationVos.clear();
		expectedInformationVos = new ArrayList<VendorDetailInformationVO>();
		expectedInformationVos.add(generateVenforDetailInformation("Vendor 1", 45, 34));
		expectedInformationVos.add(generateVenforDetailInformation("Vendor 3", 10, 8));
		expectedInformationVos.add(generateVenforDetailInformation("Vendor 4", 15, 12));
		expectedInformationVos.add(generateVenforDetailInformation("Vendor 2", 30, 23));
		assertEquals(requestVO.getTotalWorkLoads().intValue(), detailInformationVOs.stream().mapToInt(VendorDetailInformationVO :: getRoundOffValue).sum());
		assertVendorDetailInformation(expectedInformationVos, detailInformationVOs);
		
		/**
		 * Case 3 : unevenly distributed workloads with decimals > 0.5
		 */
		requestVO.setTotalWorkLoads(99);
		informationVOs.clear();
		informationVOs.add(generateVendorInformationVO("Vendor 1", 45, null));
		informationVOs.add(generateVendorInformationVO("Vendor 3", 10, null));
		informationVOs.add(generateVendorInformationVO("Vendor 4", 15, null));
		informationVOs.add(generateVendorInformationVO("Vendor 2", 30, null));
		requestVO.setInformations(informationVOs);
		detailInformationVOs.clear();
		detailInformationVOs = service.distributeWorkloads(requestVO);
		expectedInformationVos.clear();
		expectedInformationVos = new ArrayList<VendorDetailInformationVO>();
		expectedInformationVos.add(generateVenforDetailInformation("Vendor 1", 45, 44));
		expectedInformationVos.add(generateVenforDetailInformation("Vendor 3", 10, 10));
		expectedInformationVos.add(generateVenforDetailInformation("Vendor 4", 15, 15));
		expectedInformationVos.add(generateVenforDetailInformation("Vendor 2", 30, 30));
		assertEquals(requestVO.getTotalWorkLoads().intValue(), detailInformationVOs.stream().mapToInt(VendorDetailInformationVO :: getRoundOffValue).sum());
		assertVendorDetailInformation(expectedInformationVos, detailInformationVOs);
	}
	
	@Test
	public void testDistributeSuccessCase_withPriority() throws Exception {
		VendorRequestVO requestVO = new VendorRequestVO();

		/**
		 * Case 1 : evenly distributed workloads
		 */
		requestVO.setTotalWorkLoads(100);
		requestVO.setConsiderPriority(true);
		List<VendorInformationVO> informationVOs = new ArrayList<VendorInformationVO>();
		informationVOs.add(generateVendorInformationVO("Vendor 1", 45, 2));
		informationVOs.add(generateVendorInformationVO("Vendor 2", 30, 1));
		informationVOs.add(generateVendorInformationVO("Vendor 3", 10, 4));
		informationVOs.add(generateVendorInformationVO("Vendor 4", 15, 3));
		requestVO.setInformations(informationVOs);
		List<VendorDetailInformationVO> detailInformationVOs = service.distributeWorkloads(requestVO);
		List<VendorDetailInformationVO> expectedInformationVos = new ArrayList<VendorDetailInformationVO>();
		expectedInformationVos.add(generateVenforDetailInformation("Vendor 1", 45, 45));
		expectedInformationVos.add(generateVenforDetailInformation("Vendor 2", 30, 30));
		expectedInformationVos.add(generateVenforDetailInformation("Vendor 3", 10, 10));
		expectedInformationVos.add(generateVenforDetailInformation("Vendor 4", 15, 15));
		assertEquals(requestVO.getTotalWorkLoads().intValue(), detailInformationVOs.stream().mapToInt(VendorDetailInformationVO :: getRoundOffValue).sum());
		assertVendorDetailInformation(expectedInformationVos, detailInformationVOs);

		/**
		 * Case 2 : unevenly distributed workloads.
		 */
		requestVO.setTotalWorkLoads(77);
		informationVOs.clear();
		informationVOs.add(generateVendorInformationVO("Vendor 1", 45, 2));
		informationVOs.add(generateVendorInformationVO("Vendor 2", 30, 4));
		informationVOs.add(generateVendorInformationVO("Vendor 3", 10, 1));
		informationVOs.add(generateVendorInformationVO("Vendor 4", 15, 3));
		requestVO.setInformations(informationVOs);
		detailInformationVOs.clear();
		detailInformationVOs = service.distributeWorkloads(requestVO);
		expectedInformationVos.clear();
		expectedInformationVos = new ArrayList<VendorDetailInformationVO>();
		expectedInformationVos.add(generateVenforDetailInformation("Vendor 1", 45, 35));
		expectedInformationVos.add(generateVenforDetailInformation("Vendor 2", 30, 22));
		expectedInformationVos.add(generateVenforDetailInformation("Vendor 3", 10, 8));
		expectedInformationVos.add(generateVenforDetailInformation("Vendor 4", 15, 12));
		assertEquals(requestVO.getTotalWorkLoads().intValue(), detailInformationVOs.stream().mapToInt(VendorDetailInformationVO :: getRoundOffValue).sum());
		assertVendorDetailInformation(expectedInformationVos, detailInformationVOs);
		
		/**
		 * Case 3 : unevenly distributed workloads with decimals > 0.5
		 */
		requestVO.setTotalWorkLoads(99);
		informationVOs.clear();
		informationVOs.add(generateVendorInformationVO("Vendor 1", 45, 2));
		informationVOs.add(generateVendorInformationVO("Vendor 3", 10, 1));
		informationVOs.add(generateVendorInformationVO("Vendor 4", 15, 3));
		informationVOs.add(generateVendorInformationVO("Vendor 2", 30, 4));
		requestVO.setInformations(informationVOs);
		detailInformationVOs.clear();
		detailInformationVOs = service.distributeWorkloads(requestVO);
		expectedInformationVos.clear();
		expectedInformationVos = new ArrayList<VendorDetailInformationVO>();
		expectedInformationVos.add(generateVenforDetailInformation("Vendor 1", 45, 45));
		expectedInformationVos.add(generateVenforDetailInformation("Vendor 3", 10, 10));
		expectedInformationVos.add(generateVenforDetailInformation("Vendor 4", 15, 15));
		expectedInformationVos.add(generateVenforDetailInformation("Vendor 2", 30, 29));
		assertEquals(requestVO.getTotalWorkLoads().intValue(), detailInformationVOs.stream().mapToInt(VendorDetailInformationVO :: getRoundOffValue).sum());
		assertVendorDetailInformation(expectedInformationVos, detailInformationVOs);
	}

	private void assertVendorDetailInformation(List<VendorDetailInformationVO> expected, List<VendorDetailInformationVO> actual) {
		assertEquals(expected.size(), actual.size());
		List<String> vendors = expected.stream().map(VendorDetailInformationVO::getName).distinct().collect(Collectors.toList());
		vendors.stream().forEach(vendor -> {
			VendorDetailInformationVO expectedValue = expected.stream().filter(expect -> StringUtils.equalsIgnoreCase(vendor, expect.getName())).findAny().orElse(null);
			assertTrue(expectedValue != null);
			VendorDetailInformationVO actualValue = actual.stream().filter(a -> StringUtils.equalsIgnoreCase(vendor, a.getName())).findAny().orElse(null);
			assertTrue(actualValue != null);
			assertEquals(expectedValue.getName(), actualValue.getName());
			assertEquals(expectedValue.getPercentage(), actualValue.getPercentage());
			assertEquals(expectedValue.getRoundOffValue(), actualValue.getRoundOffValue());
		});
	}

	private VendorInformationVO generateVendorInformationVO(String name, Integer percentage, Integer priority) {
		VendorInformationVO informationVO = new VendorInformationVO();
		informationVO.setName(name);
		informationVO.setPercentage(percentage);
		informationVO.setPriority(priority);
		return informationVO;
	}

	private VendorDetailInformationVO generateVenforDetailInformation(String name, Integer percentage, Integer roundOffValue) {
		VendorDetailInformationVO detailInformationVO = new VendorDetailInformationVO();
		detailInformationVO.setName(name);
		detailInformationVO.setPercentage(percentage);
		detailInformationVO.setRoundOffValue(roundOffValue);
		return detailInformationVO;
	}

	private void validateError(String errorMessage, String fieldName, VendorRequestVO requestVO) {
		try {
			service.distributeWorkloads(requestVO);
		} catch (DataValidationException dve) {
			assertEquals(errorMessage, dve.getErrorMessage());
			assertEquals(fieldName, dve.getFieldName());
		}
	}
}
