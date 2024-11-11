package com.tugasakhir.ideapedia.util;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public class TransformPagination {
    private String sortBy = "";
    private String sort = "";

    public ResponseEntity<Object> transformObject(Map<String,Object> mapz, List ls, Page page
            , String column, String value)//<PENAMBAHAN 21-12-2023>
    {
        /**
         *  Mengambil informasi sortby berdasarkan dari object page yang diproses sebelumnya
         *  UNSORTED adalah default dari spring data JPA untuk menghandle value nya tidak di set dari program
         *  jadi kita gunakan "id" untuk default sortBy dan "asc" untuk default sort
         */
        sortBy = page.getSort().toString().split(":")[0];
        sortBy = sortBy.equals("UNSORTED")?"id":sortBy;
        sort   = sortBy.equals("UNSORTED")?"asc":page.getSort().toString().split(":")[1];
        mapz.put("content",ls);
        mapz.put("total_items",page.getTotalElements());
        mapz.put("page_number",page.getNumber());
        mapz.put("total_pages",page.getTotalPages());
        mapz.put("sort",sort.trim().toLowerCase());
        mapz.put("size_per_page",page.getNumberOfElements());
        mapz.put("column_name",column);
//        mapz.put("component-filter",componentFiltering);
        mapz.put("value",value);
        return ResponseEntity.status(HttpStatus.OK).body(mapz);
    }
}
