package com.hand.WebExam3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.gson.JsonObject;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		Thread t = new connet();
		t.start();

	}

}

class connet extends Thread {

	HttpClient clCilent = HttpClients.createDefault();

	@Override
	public void run() {
		HttpGet get = new HttpGet("http://hq.sinajs.cn/list=sz300170");
		try {
			HttpResponse response = clCilent.execute(get);
			HttpEntity entity = response.getEntity();
			String bString = EntityUtils.toString(entity, "UTF-8");

			cJson(bString);
			cXml(bString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void cJson(String bString) {
		String info[] = bString.split(",");

		String getname[] = info[0].split("\"");
		String name = getname[1];
		System.out.println(name);

		JsonObject object = new JsonObject();
		object.addProperty("name", name);
		object.addProperty("open", info[1]);
		object.addProperty("close", info[2]);
		object.addProperty("current", info[3]);
		object.addProperty("hight", info[4]);
		object.addProperty("low", info[5]);

		try {
			File file = new File("handJson.json");
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter writer = new BufferedWriter(osw);
			writer.write(object.toString());
			
			writer.flush();
			writer.close();
			osw.close();
			fos.close();
			System.out.println("已写入json");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void cXml(String bString){
		String info[] = bString.split(",");

		String getname[] = info[0].split("\"");
		String cname = getname[1];
		System.out.println(cname);
		
		
		try {
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			Document document = builder.newDocument();
			
			Element stock = document.createElement("stock");
			Element name = document.createElement("name");
			name.setTextContent(cname);
			Element open = document.createElement("open");
			open.setTextContent(info[1]);
			Element close = document.createElement("close");
			close.setTextContent(info[2]);
			Element current = document.createElement("current");
			current.setTextContent(info[3]);
			Element hight = document.createElement("hight");
			hight.setTextContent(info[4]);
			Element low = document.createElement("low");
			low.setTextContent(info[5]);

			stock.appendChild(name);
			stock.appendChild(open);
			stock.appendChild(close);
			stock.appendChild(current);
			stock.appendChild(hight);
			stock.appendChild(low);
			document.appendChild(stock);
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(document), new StreamResult(new File("hand.xml")));

			System.out.println(writer.toString());
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}