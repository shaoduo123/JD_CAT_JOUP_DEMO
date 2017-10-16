package com.jd.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 京东图书分类html网址解析主入口
 * @author shaoduo
 *
 */
public class JD {

	
	static List<String> hrefList = new ArrayList<String>() ;
	static String nextPageUrl = "" ;
	
	
	public static void main(String arg[])
	{
		
			
			String startCatUrl  = "https://list.jd.com/list.html?cat=1713,3279,3646";
			
			HtmlParser hp = new HtmlParser(startCatUrl) ;
			try {
				
				hp.parser(startCatUrl);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
	/*		hrefList = hp.getHrefList() ;
			nextPageUrl = hp.getNextPageHrefList() ;
			for (String string : hrefList) {
			//	System.out.println("URL: "+string);
				try {
					
					fatch(string) ;
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
			
			
			System.out.println();*/
		
	}

	private static void fatch(String contentUrl) throws IOException {
		URL url = new URL(contentUrl) ;
		File file  = new File("D:\\jd.txt") ;
		if(file.exists())
			file.delete() ;
		file.createNewFile() ; 
	
		FileOutputStream fos = new FileOutputStream(file) ;
		
		BufferedReader br = new BufferedReader(
				new InputStreamReader(url.openStream(),"gbk")) ;
		
		byte [] b = new byte[1024] ;
		String c ; 
		while(((c = br.readLine())!=null))
		{
			fos.write(c.getBytes());
		}
		
		System.out.println("执行完毕");
		
		Document doc = Jsoup.parse(file,"UTF-8") ;
		// TODO Auto-generated method stub

		
		Element contentEl = doc.getElementById("parameter2") ;
		Elements lis = contentEl.getElementsByTag("li");
		for (Element li : lis) {
			  String text = li.text() ;
			  System.out.println("--"+text);
			}
		
	}
}
