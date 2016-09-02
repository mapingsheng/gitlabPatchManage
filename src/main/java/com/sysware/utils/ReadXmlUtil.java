package com.sysware.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.QName;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultElement;
import org.junit.Test;

public class ReadXmlUtil {

	
	/**
	 * 根据xml文件名称获取文件的输入流对象
	 *
	 * @serialData 2015-11-26 11:34
	 * @author maps
	 * @param xmlFileName xml文件名称
	 * @return InputStream 文件输入流
	 **/
	private static InputStream getXmlInputStream(String xmlFileName){
		InputStream inputStream = ReadXmlUtil.class.getClassLoader().getResourceAsStream(xmlFileName+".xml");
		return inputStream;
	} 
	
	/**
	 * 根据文件输入流对象获取文件的跟节点对象
	 *
	 * @serialData 2015-11-26 11:34
	 * @author maps
	 * @param inputStream xml文件输入流对象
	 * @return InputStream 文件输入流
	 **/
	private static Element getXmlRootElement(InputStream inputStream){
		Document doc = null;
		SAXReader saxReader = new SAXReader();
		try {
			doc = saxReader.read(inputStream);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		Element rootElement = doc.getRootElement();
		return rootElement;
	}
	
	/**
	 * 根据xml文件名称，节点名称，节点名称中的属性名称获取对应的属性值
	 *
	 * @serialData 2015-11-26 11:34
	 * @author maps
	 * @param xmlFileName xml文件名称
	 * @param nodeName 节点名称
	 * @param keyName 节点中的属性名称
	 * @return String 节点对应的属性值
	 **/
	public static String getXmlNodeValueByKey(String xmlFileName,String nodeName,String keyName){
		InputStream inputStream = ReadXmlUtil.getXmlInputStream(xmlFileName);
		Element rootElement = ReadXmlUtil.getXmlRootElement(inputStream);
		
		Element templatePathElement = rootElement.element(nodeName);
		String value = templatePathElement.attributeValue(keyName);
		
		return value;
	}
	/**
	 * 根据xml文件物理路径，节点名称，节点名称中的属性名称获取对应的属性值
	 *
	 * @serialData 2015-11-26 11:34
	 * @author maps
	 * @param xmlFilePath xml文件路径
	 * @param nodeName 节点名称
	 * @param keyName 节点中的属性名称
	 * @return String 节点对应的属性值
	 **/
	public static String[] getXmlFilePathNodeTextByKey(InputStream inputStream,String... nodeName) throws FileNotFoundException{
		String[] stringArry = new String[nodeName.length];
		String value = "";
		try{
			Element rootElement = ReadXmlUtil.getXmlRootElement(inputStream);
			for(int i=0;i<nodeName.length;i++){
				Element templatePathElement = rootElement.element(nodeName[i]);
				value =templatePathElement.getText();
				stringArry[i] = value;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return stringArry;
	}
	
	
	
	/**
	 * 根据xml文件名称，节点名称，节点名称中的属性名称获取对应的属性值
	 *
	 * @serialData 2015-11-26 11:34
	 * @author maps
	 * @param xmlFileName xml文件名称
	 * @param nodeName 节点名称
	 * @param keyName 节点中的属性名称
	 * @return String 节点对应的属性值
	 **/
	public static Map<String,Map<String,String>> getXmlNodeValue(String xmlFileName){
		InputStream inputStream = ReadXmlUtil.getXmlInputStream(xmlFileName);
		Element rootElement = ReadXmlUtil.getXmlRootElement(inputStream);
		
		List<Map<String,Map<String,String>>> priList=new ArrayList<Map<String,Map<String,String>>>();
		
		Map<String,Map<String,String>> map = new HashMap<String, Map<String,String>>();
		
		@SuppressWarnings("unchecked")
		List<Element> listSonElement =  rootElement.elements();
		for(Element element:listSonElement){
			QName qName = element.getQName();
			String nodeName = qName.getName();
			//获取所有属性信息
			List list = element.attributes();
			for (int i=0;i<list.size();i++) {
				Attribute attribute = (Attribute) list.get(i);
				String name = attribute.getName();
				String value = attribute.getValue();
				
				Map<String,String> attributeMap = new HashMap<String, String>();
				attributeMap.put(name, value);
				
				map.put(nodeName, attributeMap);
			}
		}
		
		return map;
	}
	
	
	/**
	 * 根据xml文件名称，节点名称，节点名称中的属性名称获取对应的属性值
	 *
	 * @serialData 2015-11-26 11:34
	 * @author maps
	 * @param xmlFileName xml文件名称
	 * @param nodeName 节点名称
	 * @param keyName 节点中的属性名称
	 * @return String 节点对应的属性值
	 **/
	public static Map<String,Map<String,String>> getLoginInfoXmlNodeValue(){
		return getXmlNodeValue("loginInfo");
	}
	
	public static String getValueFormat(String[] valueArray,String format){
		StringBuilder sbf = new StringBuilder();
		for(String value:valueArray){
			sbf.append(value).append(format);
		}
		
		if(sbf.toString().endsWith(format)){
			sbf.delete(sbf.length()-1,sbf.length());
			
		}
		
		return sbf.toString();
	}
	
	
	@Test
	public void getXmlNodeValueTest(){
		Map<String,Map<String,String>> list = ReadXmlUtil.getXmlNodeValue("loginInfo");
	}
}



