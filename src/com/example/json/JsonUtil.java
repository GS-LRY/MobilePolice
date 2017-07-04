package com.example.json;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.example.model.Escaped;
import com.example.model.Normal;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class JsonUtil {

	private Gson gson = new Gson();

	/**
	 * 核录记录转化为JSON数据
	 */
	public String toJSONString(ArrayList<Normal> list) {
		String jsonArray = gson.toJson(list);
		return jsonArray;
	}
	
	// 在逃人员转化为JSON
	public String EscapedListToJSON(ArrayList<Escaped> list){
		return gson.toJson(list);
	}

	/* JSON数据转换为在逃人员列表 */
	public ArrayList<Escaped> toEscapedList(String jsondata) {
		Type listType = new TypeToken<ArrayList<Escaped>>() {
		}.getType();
		ArrayList<Escaped> list = (ArrayList<Escaped>) JSON.parseArray(
				jsondata, Escaped.class);
		return list;
	}

	/* JSON数据转换为在逃人员对象 */
	public Escaped JSONtoEscaped(String jsondata){
		return gson.fromJson(jsondata, Escaped.class);
	}
	
    // Map转JSON
	public String MaptoJSON(Map<String,String> map){
		return gson.toJson(map);
	}
}
