package com.jd.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**
 * html网址解析器
 * @author shaoduo
 *
 */
public class HtmlParser {
	
	String htmlUrl = "" ; 
	String nextPageUrl = "" ; 
	String charSet = "" ; 

	
	List<String> hrefList = new ArrayList<String>() ;
	
	
	public HtmlParser(String htmlUrl)
	{
		this.htmlUrl  = htmlUrl ; 
	}
	
/*	public List<String> getHrefList ()
	{
		try {
			
			parser() ;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return hrefList ; 
	}*/
	
	
	public String getNextPageHrefList()
	{
		
		return this.nextPageUrl ;
	}

	public  void parser(String htmlUrl) throws IOException {
		
		Document doc = getDocFromUrl(htmlUrl, "D:\\cat.txt","utf-8") ;
		hrefList = parseItemUrl(doc) ;
		for (String string : hrefList) {
			Document conDoc = getDocFromUrl(string, "D:\\content.txt","gbk") ;
			parseContent(conDoc);
			}
		
		nextPageUrl =  parseNext(doc) ;
		if(nextPageUrl!="")
		{
			System.out.println("解析下一页------------");
			parser(nextPageUrl) ;
		}
		
	}
	
	
	
	
	public Document getDocFromUrl(String htmlUrl,String toSavePath,String type) throws IOException
	{
		URL url = new URL(htmlUrl) ;
		
		File file  = new File(toSavePath) ;
		if(file.exists())
			file.delete() ;
		file.createNewFile() ; 
		
		FileOutputStream fos = new FileOutputStream(file) ;
		
		BufferedReader br = new BufferedReader(
				new InputStreamReader(url.openStream(),type)) ;
		
		byte [] b = new byte[1024] ;
		String c ; 
		while(((c = br.readLine())!=null))
		{
			fos.write(c.getBytes());
		}
		
		System.out.println("保存分类html完毕");
		
		Document doc = Jsoup.parse(file,"UTF-8") ;
		
		return doc ;
	}
	
	
	
	public List<String> parseItemUrl(Document doc)
	{
		//获取页面
		Element contentEl = doc.select("ul.gl-warp").first() ;
		Elements lis = contentEl.getElementsByTag("li");
		for (Element li : lis) {
			  
			Element titleEl = li.select("div.p-name").first() ;
			Element a = titleEl.select("a").first() ;
			String  href = a.attr("href") ;
			href = "http:"+href ; 
			hrefList.add(href) ;
			}
		return hrefList  ;
		
	}
	
	/**
	 * 解析书本的信息
	 * @param doc
	 */
	public void parseContent(Document doc)
	{
		Element contentEl = doc.getElementById("parameter2") ;
		if(contentEl==null)
			return ; 
		
		Elements lis = contentEl.getElementsByTag("li");
		
		if(lis==null)
			return ; 
		
		for (Element li : lis) {
			  String text = li.text() ;
			  System.out.println("--"+text);
			}
	}
	
	public String parseNext(Document doc)
	{	String nextPageHref="";
	
		Element el = doc.select("span.p-num").first() ;
		Elements as = el.getElementsByTag("a") ;
		for(Element a :as)
		{
			
			if(a.attr("class").endsWith("pn-next"))
			{
				String  pageHref = a.attr("href") ;
				nextPageHref = htmlUrl.substring(0,htmlUrl.lastIndexOf("/"))+pageHref;
				System.out.println("下一页地址："+nextPageHref);
		}
		}
			return nextPageHref;
	}
	
	
	
	
}
