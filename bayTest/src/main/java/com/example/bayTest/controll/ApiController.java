package com.example.bayTest.controll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.bayTest.model.JsonStatus;
import com.example.bayTest.model.ObjArrayReq;
import com.example.bayTest.services.ApiService;

@RestController
public class ApiController {
	
	@Autowired
	private ApiService apiService;
	

	@GetMapping(value = "/api/report")
	@ResponseBody
		public ResponseEntity<JsonStatus> getReport() {
			return apiService.getReportDB();
		}
	
	@PostMapping(value = "/api/save")
	@ResponseBody
		public ResponseEntity<JsonStatus> saveReport(@RequestBody ObjArrayReq objArr) {
			return apiService.saveData(objArr);
		}
	
	@GetMapping(value = "/api/summary/report")
	@ResponseBody
		public ResponseEntity<JsonStatus> getSummaryReport() {
			return apiService.getSummaryData();
		}
	

}
