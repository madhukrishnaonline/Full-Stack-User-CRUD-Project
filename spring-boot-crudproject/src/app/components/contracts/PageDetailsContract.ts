import { UserDetailsContract } from "./UserDetailsContracts";

export interface PageDetailsContract{
	totalPages: number;
	currentPage: number;
	isFirst: boolean;
	isLast: boolean;
	isEmpty: boolean;
	hasPrevious: boolean;
	hasNext: boolean;
	noOfRecordsInCurrentPage:number;
	totalNoOfRecords:number;
	userDataBindResponse:UserDetailsContract[]
}