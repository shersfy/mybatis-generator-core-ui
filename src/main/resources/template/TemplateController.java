package org.shersfy.controller;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.young.commons.beans.BaseForm;
import org.young.commons.beans.Result;

import com.alibaba.fastjson.JSON;

%s

@RestController
@RequestMapping("/%s")
public class TemplateController extends BaseController {
	
	@Autowired
	private TemplateService service;
	
	@GetMapping("/list")
	public Result list(BaseForm form) {
		Result res = new Result();
		
		Template where = null;
		if (StringUtils.isNotBlank(form.getKeyword())) {
			where = JSON.parseObject(form.getKeyword(), Template.class);
		} else {
			where = new Template();
		}
		res.setModel(service.findPage(where, form.getPageNo(), form.getPageSize()));
		return res;
	}
	
	@PostMapping("/save")
	public Result save(Template info) {
		Result res = new Result();
		res.setModel(service.save(info.getId(), info)==1?info:null);
		return res;
	}
	
	@PostMapping("/delete")
	public Result delete(@RequestParam(required = true)%s id) {
		Result res = new Result();
		res.setModel(service.deleteById(id));
		return res;
	}
	
	@PostMapping("/delete/ids")
	public Result delete(@RequestParam(required = true)%s[] ids) {
		Result res = new Result();
		res.setModel(service.deleteByIds(Arrays.asList(ids)));
		return res;
	}

}
