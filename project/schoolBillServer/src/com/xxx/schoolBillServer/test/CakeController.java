package com.xxx.schoolBillServer.test;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.xxx.schoolBillServer.entity.BillType;

@Controller
@RequestMapping("/cake")
public class CakeController {
	
	@Resource
	private CakeService cakeService;
	
	@RequestMapping(value = "get", method=RequestMethod.GET)
	public void toAdd(HttpServletRequest request) {
		System.out.println("111");
		List<BillType> list =cakeService.getList();
		for(BillType type:list) {
			System.out.println(type.toString());
		}
	}
	
	

}
