package com.dabaicong.jpa.util;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;


public class XmlParse {
	public static int ELEMENT_TYPE_HEADER = 1;
	public static int ELEMENT_TYPE_BODY = 2;
	private Document document;
	private Element headerElement;
	private Element rootElement;
	private Element bodyElement;
	private Element mdElement;

	public void addHeaderElement(String nodeName, String content) {
		if ((content != null) && (content.length() > 0))
			this.headerElement.addElement(nodeName).setText(content);
	}

	public XmlParse(){
		
	}
	
	public static XmlParse  addGuoXinCaiHead(String agentNo,String code){
		XmlParse xmlParse=null;
		xmlParse = new XmlParse("message", "header", "body");
		xmlParse.addHeaderElement("sid", agentNo);
		xmlParse.addHeaderElement("cmd", code);
		xmlParse.addHeaderElement("digestType","md5");
		return xmlParse;
	}
	/**
	 * 拼串
	 * 
	 * @param rootElementName
	 * @param headerElementName
	 * @param bodyElemnetName
	 */
	public XmlParse(String rootElementName, String headerElementName,
			String bodyElemnetName) {
		this.document = DocumentHelper.createDocument();
		this.rootElement = this.document.addElement(rootElementName);
		if (StringUtils.isNotEmpty(headerElementName))
		this.headerElement = this.rootElement.addElement(headerElementName);
		if (StringUtils.isNotEmpty(bodyElemnetName))
			this.bodyElement = this.rootElement.addElement(bodyElemnetName);
	}

	public XmlParse(String rootElementName, String headerElementName) {
		this.document = DocumentHelper.createDocument();
		this.rootElement = this.document.addElement(rootElementName);
		this.headerElement = this.rootElement.addElement(headerElementName);
	}

	public XmlParse(String rootElementName) {
		this.document = DocumentHelper.createDocument();
		this.bodyElement = this.document.addElement(rootElementName);
	}

	/**
	 * 解析返回内容
	 * 
	 * @param returnContent
	 * @throws DocumentException
	 */
	public XmlParse(String returnContent, String head, String body, String type)
			throws DocumentException {
		this.document = DocumentHelper.parseText(returnContent);
		this.rootElement = this.document.getRootElement();
		this.headerElement = this.rootElement.element(head);
		this.bodyElement = this.rootElement.element(body);
	}

	public String getElementAttribute(String nodeName, String attributeName,int elementType) {
		Element nodeElement = getPathElement(nodeName, elementType);
		return nodeElement.attributeValue(attributeName);
	}
	
	public Element setElement(String nodeName,String type) {
		if("0".equals(type)){
		   	headerElement.setName(nodeName);
		   	return headerElement;
		}else{
			bodyElement.setName(nodeName);
			return bodyElement;
		}
	}
	/**
	 * 
	 * @param nodeName
	 * @param elementType  1head 2body
	 * @return
	 */
	public String getElementAttributeText(String nodeName, int elementType) {
		Element nodeElement = getPathElement(nodeName, elementType);
		return nodeElement.getText();
	}
	
	public String getBodyText(String nodeName) {
		return bodyElement.getText();
	}
	

	/**
	 * 获取属性值
	 * @param nodeElement
	 * @return
	 */
	  public static HashMap<String, String> getElementAttribute(Element nodeElement)
	  {
	    if (nodeElement != null) {
	      HashMap<String,String> returnMap = new HashMap<String,String>();
	      for (Iterator<?> it = nodeElement.attributeIterator(); it.hasNext(); ) {
	        Attribute nodeAttribute = (Attribute)it.next();
	        returnMap.put(nodeAttribute.getName(), nodeAttribute.getText());
	      }
	      return returnMap;
	    }
	    return null;
	  }
	private Element getPathElement(String nodeName,int elementType) {
		Element element = null;
		if (elementType == ELEMENT_TYPE_HEADER) {
				element = this.headerElement.element(nodeName);
		} else if (elementType == ELEMENT_TYPE_BODY){
			element = this.bodyElement.element(nodeName);
		}
		return element;
	}
    
	/**
	 * 解析element 得到根节点
	 * @return
	 * @throws DocumentException 
	 */
	public static Element getRootElement(String strs) throws DocumentException{
		Document doc  = DocumentHelper.parseText(strs);
		Element rootElt = doc.getRootElement(); 
		return rootElt;
	}
	
	/**
	 * 解析xml下迭代 list属性 name、sex
	 * @param strs 要解析的xml串
	 * @param nodeName 取得节点父节点 response/alipay_results/
	 * @param rootElement 获取循环根节点 people
	 * <?xml version=\"1.0\" encoding=\"utf-8\"?>
	 * <alipay><response>
	 * <alipay_results>
	 * <people><name>zhang</name><sex>1</sex></people>
	 * <people><name>zhang</name><sex>1</sex></people>
	 * </alipay_results><people></response></alipay>
	 * @return
	 */
	public  static List<HashMap<String,String>> getElementTextList(String nodeName,String rootElement,String strs) throws DocumentException{
		 Element rootElt=getRootElement(strs);
		 List<HashMap<String,String>>lists=new ArrayList<HashMap<String,String>>();
		 Iterator<?> iterator=null;
		 String[] nodePathList = StringUtils.split(nodeName, "/");
		 for(int i=0;i<nodePathList.length;i++){
			rootElt=rootElt.element(nodePathList[i]+"");
		 }
		 try {
			iterator = rootElt.elementIterator(rootElement);
		} catch (Exception e) {
			return null;
		}
		while(iterator.hasNext()){
			HashMap<String,String> returnMap = new HashMap<String,String>();
			Element ele=(Element)iterator.next();
			List<?> nodes = ele.elements();
		    for (Iterator<?> it1 = nodes.iterator(); it1.hasNext(); ) {
		       Element node = (Element)it1.next();
		       returnMap.put(node.getName(), node.getText());
		    }
		    lists.add(returnMap);
		}
		return	lists;
	}
	
	/**
	 * 解析xml下迭代 属性 name、sex
	 * @param strs 要解析的xml串
	 * @param nodeName 取得节点父节点 /response
	 * @param subElement 获取循环根节点 alipay_results
	 * <?xml version=\"1.0\" encoding=\"utf-8\"?><alipay><response>
	 * <alipay_results><name>zhang</name><sex>1</sex></alipay_results>
	 * </response></alipay>
	 * @return
	 * @throws DocumentException 
	 */
	public  static HashMap<String,String> getElementText(String nodeName,String rootElement,String strs) throws DocumentException{
		 Element ele=getElement(nodeName,rootElement,strs);
		 HashMap<String,String> returnMap = new HashMap<String,String>();
		 List<?> nodes = ele.elements();
		 for (Iterator<?> it1 = nodes.iterator(); it1.hasNext(); ) {
		    Element node = (Element)it1.next();
		    returnMap.put(node.getName(), node.getText());
		}
		return	returnMap;
	}
	
	/**
	 * 解析xml下迭代 属性 name、sex
	 * @param strs 要解析的xml串
	 * @param nodeName 取得节点父节点 /response
	 * @param subElement 获取循环根节点 alipay_results
	 * <?xml version=\"1.0\" encoding=\"utf-8\"?><alipay><response>
	 * <alipay_results><name>zhang</name><sex>1</sex></alipay_results>
	 * </response></alipay>
	 * @return
	 * @throws DocumentException 
	 */
	public  static HashMap<String,String> getElementAttribute(String nodeName,String rootElement,String strs) throws DocumentException{
		 Element ele=getElement(nodeName,rootElement,strs);
		 HashMap<String,String> returnMap = new HashMap<String,String>();
		 List<?> nodes = ele.attributes();
		 for (Iterator<?> it1 = nodes.iterator(); it1.hasNext(); ) {
		     Attribute node =(Attribute) it1.next();
		     returnMap.put(node.getName(),node.getValue());
		 }
		 return	returnMap;
	}
	/**
	 * 读取属性list
	 * @param nodeName  body/response/result/
	 * @param rootElement  code
	 * @param strs
	 * @return"<ticket><body><response errorcode=\"错误码\"><result>
	 * <code ordered=\"2011122334444555\" state=\"5\" amount =\"500\" preamount =\"500\"/>
	 * <code ordered=\"2011122334444556\" state=\"4\" amount =\"0\" preamount =\"0\"/>
	 * </result></response></body></ticket>";
	 * @throws DocumentException
	 */
	public  static List<HashMap<String,String>> getElementAttributeList(String nodeName,String rootElement,String strs) throws DocumentException{
		 Element rootElt=getRootElement(strs);
		 Iterator<?> iterator=null;
		 String[] nodePathList = StringUtils.split(nodeName, "/");
		 for(int i=0;i<nodePathList.length;i++){
			rootElt=rootElt.element(nodePathList[i]+"");
		 }
		 List<HashMap<String, String>>list=new ArrayList<HashMap<String,String>>();
		 Element ele=null;
		 iterator=rootElt.elementIterator(rootElement);
		 while(iterator.hasNext()){
			 HashMap<String,String> returnMap = new HashMap<String,String>();
			 ele=(Element)iterator.next();
			 List<?> nodes = ele.attributes();
			 for (Iterator<?> it1 = nodes.iterator(); it1.hasNext(); ) {
			     Attribute node =(Attribute) it1.next();
			     returnMap.put(node.getName(),node.getValue());
			 }
			 list.add(returnMap);
		 }
		 return	list;
	}
	
	public  static Element getElement(String nodeName,String rootElement,String strs) throws DocumentException{
		 Element rootElt=getRootElement(strs);
		 Iterator<?> iterator=null;
		 String[] nodePathList = StringUtils.split(nodeName, "/");
		 for(int i=0;i<nodePathList.length;i++){
			rootElt=rootElt.element(nodePathList[i]+"");
		 }
		 Element ele=null;
		 try {
			iterator = rootElt.elementIterator(rootElement);
		} catch (Exception e) {
			return null;
		}
		if(iterator.hasNext()){
			ele=(Element)iterator.next();
		 }
		 return ele;
	}
	
	public void addHeaderAttribute(Map<String, String> attributeMap) {
		if ((attributeMap != null) && (!attributeMap.isEmpty())) {
			Iterator<?> it = attributeMap.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				this.headerElement.addAttribute(key,
						(String) attributeMap.get(key));
			}
		}
	}

	public Element addBodyElement(Map<String, String> nodeMap,Element element) {
		if ((nodeMap != null) && (!nodeMap.isEmpty())) {
			Iterator<?> it = nodeMap.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				addBodyElement(key, (String) nodeMap.get(key),element);
			}
		}
		return element;
	}

	public void addBodyElement(String nodeName, String content,Element element) {
		if ((content != null) && (content.length() > 0))
			element.addElement(nodeName, "").setText(content);
	}
	
	public void addBodyElement(String nodeName, String content) {
		if ((content != null) && (content.length() > 0))
			this.bodyElement.addElement(nodeName, "").setText(content);
	}

	public Element addBodyElementAndAttribute(String nodeName, String content,
			Map<String, Object> attributeMap) {
		Element bodyNodeElement = this.bodyElement.addElement(nodeName);
		if ((content != null) && (content.length() > 0)) {
			bodyNodeElement.setText(content);
		}
		return addElementAttribute(bodyNodeElement, attributeMap);
	}
	
	public Element addBodyElementAndAttribute(String nodeName, String content,
			Map<String, Object> attributeMap,Element element) {
		Element bodyNodeElement = element.addElement(nodeName);
		if ((content != null) && (content.length() > 0)) {
			bodyNodeElement.setText(content);
		}
		return addElementAttribute(bodyNodeElement, attributeMap);
	}

	public Element addElementAttribute(Element element,
			Map<String, Object> attributeMap) {
		if ((attributeMap != null) && (!attributeMap.isEmpty())) {
			Iterator<?> it = attributeMap.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				element.addAttribute(key, attributeMap.get(key)+"");
			}
		}
		return element;
	}
    /**
     * 博雅头部内容
     * @param fcbyConfig
     * @param code
     * @param igenGeneratorService
     * @return
     */
	public static XmlParse  addFcbyHead(String agencyCode,String code,String messageId){
		XmlParse xmlParse=null;
		String timestamp = DateUtil.format("yyyyMMddHHmmss", new Date());
		xmlParse = new XmlParse("ticket", "ctrl", "body");
		xmlParse.addHeaderElement("agentid", agencyCode);
		xmlParse.addHeaderElement("cmd", code);
		xmlParse.addHeaderElement("messageid",messageId);
		xmlParse.addHeaderElement("timestamp", timestamp);
		return xmlParse;
	}
	
	/**
	 * 中彩汇头部内容
	 * @param agentNo  商户号
	 * @param code  服务编码
	 * @param messageId 加密串
	 * @return
	 */
	public static XmlParse  addZchHead(String agentNo,String code){
		XmlParse xmlParse=null;
		xmlParse = new XmlParse("message", "header", "body");
		xmlParse.addHeaderElement("sid", agentNo);
		xmlParse.addHeaderElement("cmd", code);
		xmlParse.addHeaderElement("digestType","md5");
		return xmlParse;
	}
	/**
	 * 华彩头部内容
	 * @param agentNo  商户号
	 * @param code  服务编码
	 * @return
	 */
	public static XmlParse addHuacaiHead(String agentID,String code){
		XmlParse xmlParse=null;
		xmlParse = new XmlParse("message", "header", "body");
		xmlParse.document.setXMLEncoding("GBK");
		xmlParse.rootElement.addAttribute("version", "1.0");
		xmlParse.addHeaderElement("messengerID", UUID.randomUUID().toString().replaceAll("\\-", ""));
		xmlParse.addHeaderElement("agentID", agentID);
		xmlParse.addHeaderElement("timestamp", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		xmlParse.addHeaderElement("transactionType", code);
		return xmlParse;
	}
	
	public static Document decoder(String decodestr){
		String resultstr="";
		try{
			resultstr=URLDecoder.decode(decodestr,"UTF-8");
			Document doc = DocumentHelper.parseText(resultstr);
			return doc;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public String asBodyXML() {
		return this.bodyElement.asXML();
	}

	public String asXML() {
		return this.document.asXML();
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public Element getHeaderElement() {
		return headerElement;
	}

	public void setHeaderElement(Element headerElement) {
		this.headerElement = headerElement;
	}

	public Element getRootElement() {
		return rootElement;
	}

	public void setRootElement(Element rootElement) {
		this.rootElement = rootElement;
	}

	public Element getBodyElement() {
		return bodyElement;
	}

	public void setBodyElement(Element bodyElement) {
		this.bodyElement = bodyElement;
	}

	public Element getMdElement() {
		return mdElement;
	}

	public void setMdElement(Element mdElement) {
		this.mdElement = mdElement;
	}
	public void fun(){
		String text= "<message version=\"1.0\"><header><messengerID>30d4c805a5574c18accf9c60b9fb1e3c</messengerID><agentID>QD0006A5</agentID><timestamp>20140815174939</timestamp><transactionType>503</transactionType><digest>ba61e7417b84d9464b666a7e407e46c4</digest></header><body><response code=\"0\" message=\"您的操作已成功\"><tiketQueryResult><ticket id=\"6000012007051012345678\" dealTime=\"20070510094200\" status=\"1\" message=\"成功,系统正常处理\"/><ticket id=\"6000012007051012345679\" dealTime=\"20070510094200\" status=\"1\" message=\"成功,系统正常处理\"/></ticketQueryResult></response></body></message>";
		String res ="<?xml version=\"1.0\" encoding=\"utf-8\"?><message version=\"1.0\"><header><messengerID>30d4c805a5574c18accf9c60b9fb1e3c</messengerID><agentID>QD0006A5</agentID><timestamp>20140815174939</timestamp><transactionType>503</transactionType><digest>ba61e7417b84d9464b666a7e407e46c4</digest></header><body><response code=\"0\" message=\"您的操作已成功\"/></body></message>";
				
		
		try {
			Map<String,String> mapResult=XmlParse.getElementAttribute("body/", "response",res );
			System.out.println("code "+mapResult.get("code"));
			List<HashMap<String,String>> mapLists=XmlParse.getElementAttributeList("body/response/tiketQueryResult", "ticket", text);
	    	List<HashMap<String,String>> mLists=XmlParse.getElementTextList("body/response/", "ticket", text);
	    	System.out.println(mapLists.size());
	    	System.out.println(mLists.size());
//			Map<String,String> map=XmlParse.getElementAttribute("body/", "response", text);
//			List<HashMap<String,String>> attributeList=XmlParse.getElementAttributeList("body/response/tiketQueryResult/","ticket",res);
//			String msg=map.get("message");
//			System.out.println(msg);
//			System.out.println(attributeList.get(0).get("id"));
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {
		XmlParse p = new XmlParse();
		p.fun();
	}
}