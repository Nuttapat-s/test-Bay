package com.example.bayTest.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.bayTest.model.AllData;
import com.example.bayTest.model.AllDataObj;
import com.example.bayTest.model.JsonStatus;
import com.example.bayTest.model.ListSummaryData;
import com.example.bayTest.model.ObjArrayReq;
import com.example.bayTest.model.ReqObj;
import com.example.bayTest.model.ResGetAll;
import com.example.bayTest.model.SummaryData;
import com.example.bayTest.repo.Repo;
import com.google.gson.Gson;


@Service
public class ApiService {
	@Autowired
	private Repo repo;
	
	public ResponseEntity<JsonStatus> getReportDB(){
		JsonStatus jsonStatus = new JsonStatus();
		List<AllData> result = repo.findAll();
		if(result.isEmpty() || result == null) {
			jsonStatus.setResultCode("404");
			jsonStatus.setResultDesc("Data not found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonStatus);
		}
		ArrayList<ResGetAll> NewData = manageData(result);
		
		jsonStatus.setResultCode("200");
		jsonStatus.setResultDesc("SUCCESS");
		jsonStatus.setResultData(NewData);
		return ResponseEntity.ok(jsonStatus);
		
	}
	
	public ResponseEntity<JsonStatus> saveData(ObjArrayReq objArr){
		JsonStatus jsonStatus = new JsonStatus();
		try {
			jsonStatus.setResultCode("200");
			jsonStatus.setResultDesc("SUCCESS");
			for(AllData data : objArr.getList()) {
				repo.save(data);
			}
			
		} catch (Exception e) {
			jsonStatus.setResultCode("502");
			jsonStatus.setResultDesc("DB ERROR");
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(jsonStatus);
		}
		return ResponseEntity.ok(jsonStatus);
	}
	
	public ResponseEntity<JsonStatus> getSummaryData(){
		JsonStatus jsonStatus = new JsonStatus();
		List<AllData> result = repo.findAll();
		if(result.isEmpty() || result == null) {
			jsonStatus.setResultCode("404");
			jsonStatus.setResultDesc("Data not found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonStatus);
		}
		
		
		jsonStatus.setResultCode("200");
		jsonStatus.setResultDesc("SUCCESS");
		jsonStatus.setResultData(vaildateSummayData(result));
		return ResponseEntity.ok(jsonStatus);
	}
	
	private ListSummaryData vaildateSummayData(List<AllData> result) {
		ArrayList<AllDataObj> objData = new ArrayList<>();
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		ArrayList<String> arrDate = new ArrayList<>();
		ArrayList<String> arrProfile = new ArrayList<>();
		Gson  g = new Gson();
		
		for(AllData data : result) {
			AllDataObj obj = new AllDataObj();
			obj.setId(data.getId());
			obj.setReqObj(g.fromJson(data.getReqObj(), ReqObj.class));
			obj.setUri(data.getUri());
			try {
				obj.setDateTime(simpleDateFormat.format(sdfDate.parse(data.getDateTime())) );
				arrDate.add(simpleDateFormat.format(sdfDate.parse(data.getDateTime())));
			} catch (ParseException e) {
				obj.setDateTime(simpleDateFormat.format("") );
			}
			objData.add(obj);
		}
		
		Set<String> set = new HashSet<>(arrDate);
		arrDate.clear();
		arrDate.addAll(set);
//		List<Str> listDate = new ArrayList<>(arrDate);
//		List<Date> newListDate = listDate.stream().distinct().collect(Collectors.toList());
		Collections.sort(arrDate);
		
		for(AllDataObj d : objData) {
			arrProfile.add( d.getReqObj().getProfile());
		}
		
		set = new HashSet<>(arrProfile);
		arrProfile.clear();
		arrProfile.addAll(set);
//		List<String> listProfile = new ArrayList<>(arrProfile);
//		List<String> newListProfgile = listProfile.stream().distinct().collect(Collectors.toList());
		Collections.sort(arrProfile);
		
		HashMap<String, Integer> map = new HashMap<>();
		
		for(String date : arrDate) {
			for(String profile : arrProfile) {
				int count = 0;
				for(AllDataObj data : objData) {
					if(date.equals(data.getDateTime()) && profile.equals(data.getReqObj().getProfile())) {
						count++;
					}
					map.put(date+","+profile, count);
				}
			}
		}
		ListSummaryData list = new ListSummaryData();
		ArrayList<SummaryData> sdArr = new ArrayList<>();
		for(Entry<String, Integer> e : map.entrySet()) {
			SummaryData sd = new SummaryData();
			if(e.getValue() != 0) {
				sd.setCount(e.getValue());
				sd.setDateTime(e.getKey().split("\\,")[0]);
				sd.setProfile(e.getKey().split("\\,")[1]);
				sdArr.add(sd);
			}
		}
		
		list.setList(sdArr);
		return list;
		
	}
	
	private ArrayList<ResGetAll> manageData(List<AllData> result){
		Gson g = new Gson();
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		ArrayList<ResGetAll> newAllData = new ArrayList<>();
		ArrayList<String> arrDate = new ArrayList<>();
		for(AllData data : result) {
			try {
				arrDate.add(simpleDateFormat.format(sdfDate.parse(data.getDateTime())) );
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		Set<String> set = new HashSet<>(arrDate);
		arrDate.clear();
		arrDate.addAll(set);
//		List<Date> list = new ArrayList<>(arrDate);
//		List<Date> newList = list.stream().distinct().collect(Collectors.toList());
		Collections.sort(arrDate);
		for(String date : arrDate) {
			for(AllData data : result) {
				try {
					if(simpleDateFormat.format(sdfDate.parse(data.getDateTime())).equals(date)) {
						ResGetAll res = new ResGetAll();
						ReqObj obj = g.fromJson(data.getReqObj(), ReqObj.class);
						res.setReqDate(simpleDateFormat.format(sdfDate.parse(data.getDateTime())));
						res.setUri(data.getUri().replace("https://", "").split("/")[1]);
						res.setAction(data.getUri().replace("https://", "").split("/")[3]);
						res.setProfile(obj.getProfile());
						res.setUsername(obj.getUsername());
						res.setMobile(obj.getMobile());
						res.setDeliver(obj.getDeliver());
						if(obj.getOtp() == null) {
							res.setOtp("");
						}else {
							res.setOtp(obj.getOtp());
						}
						
						newAllData.add(res);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		
		return newAllData;
		
	}
}
