export interface SearchParam {
  current: number;
  pageSize: number;
  searchText: string;
  type: string;
  sortField?: string;
  sortOrder?: string;
}
