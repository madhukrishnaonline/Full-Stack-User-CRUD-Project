package com.mvc.binding;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDataBindResponsePage {
	
	private Integer totalPages;
	
	private Integer currentPage;
	
	private Boolean isFirst;
	
	private Boolean isLast;
	
	private Boolean isEmpty;
	
	private Boolean hasPrevious;

	private Boolean hasNext;
	
	private Integer noOfRecordsInCurrentPage;

	private Long totalNoOfRecords;
	
    private List<UserDataBindResponse> userDataBindResponse;
	
}//class