package com.seneca.problem.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.seneca.problem.exception.DataValidationException;
import com.seneca.problem.service.vo.VendorDetailInformationVO;
import com.seneca.problem.service.vo.VendorInformationVO;
import com.seneca.problem.service.vo.VendorRequestVO;

@Service
public class PercentageDistributionService {

	public List<VendorDetailInformationVO> distributeWorkloads(VendorRequestVO vendorRequestVO) throws DataValidationException {
		List<VendorInformationVO> informationVOs = vendorRequestVO.getInformations();
		Integer total = vendorRequestVO.getTotalWorkLoads();

		// Validation of request
		validateVendorDistribition(informationVOs, total);
		
		List<VendorDetailInformationVO> result = new ArrayList<VendorDetailInformationVO>();
		List<VendorDetailInformationVO> detailInformationVOs = new ArrayList<VendorDetailInformationVO>();
		// Calculate percentage distribution for vendors.
		informationVOs.stream().forEach(info -> {
			calulateVendorPercentageValues(total, detailInformationVOs, info, vendorRequestVO.getConsiderPriority());
		});
		List<VendorDetailInformationVO> finalListSorted = generateFinalVendorList(detailInformationVOs, vendorRequestVO.getConsiderPriority());
		Integer sum = finalListSorted.stream().mapToInt(VendorDetailInformationVO::getRoundOffValue).sum();
		Integer difference = total - sum;
		if (difference < 0) {
			Integer deveation = Math.abs(difference);
			List<VendorDetailInformationVO> differenceList = finalListSorted.stream().limit(deveation).collect(Collectors.toList());
			result.addAll(differenceList.stream().map(data -> this.mauplateData(data)).collect(Collectors.toList()));
			List<String> vendors = differenceList.stream().map(VendorDetailInformationVO :: getName).collect(Collectors.toList());
			result.addAll(
					finalListSorted.stream().filter(data -> !vendors.contains(data.getName())).collect(Collectors.toList()));
		} else if (difference > 0) {
			List<VendorDetailInformationVO> differenceList = finalListSorted.stream().limit(difference).collect(Collectors.toList());
			result.addAll(differenceList.stream().map(data -> this.mauplatePostiveData(data)).collect(Collectors.toList()));
			List<String> vendors = differenceList.stream().map(VendorDetailInformationVO :: getName).collect(Collectors.toList());
			result.addAll(
					finalListSorted.stream().filter(data -> !vendors.contains(data.getName())).collect(Collectors.toList()));
		} else {
			result.addAll(finalListSorted);
		}
		return result;
	}
	
	private VendorDetailInformationVO mauplateData(VendorDetailInformationVO detailInformationVO) {
		Integer d = (int) Math.round(detailInformationVO.getActualValue()) - 1;
		detailInformationVO.setRoundOffValue(d);
		return detailInformationVO;
	}
	
	private VendorDetailInformationVO mauplatePostiveData(VendorDetailInformationVO detailInformationVO) {
		Integer d = (int) Math.round(detailInformationVO.getActualValue() + 0.5);
		detailInformationVO.setRoundOffValue(d);
		return detailInformationVO;
	}
	
	
	private void validateVendorDistribition(List<VendorInformationVO> informationVOs, Integer totalWorkLoads) throws DataValidationException {
		if (totalWorkLoads == null) {
			throw new DataValidationException("totalWorkload", "provide total workloads");
		}
		if (CollectionUtils.isEmpty(informationVOs)) {
			throw new DataValidationException("distributions", "provide vendor distributions");
		}
		Integer totalPercentage = informationVOs.stream().mapToInt(VendorInformationVO :: getPercentage).sum();
		if (totalPercentage != 100) {
			throw new DataValidationException("totalPercentage", "Percentage sum of all vendors should be 100. But actual is =>" + totalPercentage);
		}
	}

	private List<VendorDetailInformationVO> generateFinalVendorList(List<VendorDetailInformationVO> list, Boolean considerPriority) {
		if (considerPriority) {
			return list.stream().sorted(Comparator.comparing(VendorDetailInformationVO::getPriority, Comparator.nullsLast(Comparator.naturalOrder())).reversed())
					.collect(Collectors.toList());
		}
		return list.stream().sorted(Comparator.comparing(VendorDetailInformationVO::getDecimalValue, Comparator.nullsLast(Comparator.naturalOrder()))).collect(Collectors.toList());
	}

	private void calulateVendorPercentageValues(Integer total, List<VendorDetailInformationVO> detailInformationVOs, VendorInformationVO info, Boolean considerPriority) {
		VendorDetailInformationVO detailInformationVO = new VendorDetailInformationVO();
		detailInformationVO.setName(info.getName());
		detailInformationVO.setPercentage(info.getPercentage());
		if (considerPriority) {
			detailInformationVO.setPriority(info.getPriority());
		}
		detailInformationVO.setActualValue(total * info.getPercentage() * 0.01);
		detailInformationVO.setRoundOffValue(Integer.parseInt(String.valueOf(Math.round((total * info.getPercentage() * 0.01)))));
		detailInformationVOs.add(detailInformationVO);
	}

}
