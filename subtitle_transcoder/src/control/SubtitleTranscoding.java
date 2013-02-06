package control;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.Frame;


import org.dom4j.CDATA;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class SubtitleTranscoding {

	/**
	 * @param args
	 * @return 
	 */
	String filepath;
	ArrayList<Frame> frameList = new ArrayList<Frame>() ;
	public String parseSubtitleFile(String filePath){
		String message = "";
		try{
			this.filepath = filePath;
			Document subtitle = this.read(filePath);
			Element tt = subtitle.getRootElement();
//			Node div = tt.elementIterator(arg0)
			Iterator<?> iter = tt.elementIterator();
		    while(iter.hasNext()){
					Element element = (Element) iter.next();
					if(element.getName().equals("body"))
					{
					  System.out.println("right");
					  Element div = (Element) element.elementIterator().next();
					  System.out.println(div);
					  Iterator<?> it = div.elementIterator();
					  while(it.hasNext()){
						   Element listElement = (Element) it.next();
						    String start = listElement.attribute("begin").getValue();
							String end = listElement.attribute("end").getValue();
							String width = listElement.attribute("width").getValue();
							String height = listElement.attribute("height").getValue();
							String src = listElement.getText();
							src = src.replaceFirst("http://moviestoreclickplayhk.blob.core.windows.net/content/136/full_movie/subtitle/", "");
							start = validTime(start);
							end = validTime(end);
							//System.out.println(start);
							Frame frame = new Frame(width, height, src, start, end);
							frameList.add(frame);
							message = "file is selected";
					  }
					}
					
				}
		}catch (Exception e){
			System.out.println(e.getMessage());
			message = "fail to select the specified file";
		}
		return message;
		
	}
	private String validTime1(String obj){
		obj = obj.trim();
        if(obj.lastIndexOf(".") == -1)
        	return obj.replaceFirst("s", "000");
        else
        {
        	int index = obj.length() - obj.lastIndexOf(".");
        	String time = obj.replaceFirst("\\.", "");
        	//System.out.println(index);
        	 switch(index)
             {
             case 2:
             	time = time.replaceFirst("s", "000");
             	break;
             case 3:
             	time = time.replaceFirst("s", "00");
             	break;
             case 4 :
            	 time = time.replaceFirst("s", "0");
              	break;
             }
        	 return time;
        }

	}
	private String validTime(String obj){
		obj = obj.trim();
		String[] time = obj.split(":");
		int result = Integer.parseInt(time[0])*3600 + Integer.parseInt(time[1])*60 + Integer.parseInt(time[2]);
		return result+"000";
		
	}
	private Document read(String fileName) throws MalformedURLException, DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(new File(fileName));
		return document;
	}
	public void toSMAI (){
		
		Document subtitleElement = DocumentHelper.createDocument();
		Element root = subtitleElement.addElement("SAMI");
		root.addElement("HEAD");
		Element body = root.addElement("BODY");
		//run a loop for getting each frame link and store into new element;
		
		for(int i= 0; i < frameList.size(); i++)
		{
			
			Frame frame = frameList.get(i);
			Element frameElement = body.addElement("SYNC");
			frameElement.addAttribute("Start", frame.getStart());
			Element pElement = frameElement.addElement("P");
			pElement.addAttribute("Class", "ENUSCC");
			pElement.addText(frame.getSrc());
			if(i+1 < frameList.size())
			{
				Frame _nextFrame = frameList.get(i+1);
				if(!frame.getEnd().equals(_nextFrame.getStart())){
					Element _nextFrameElement = body.addElement("SYNC");
					_nextFrameElement.addAttribute("Start", frame.getEnd());
					Element _nextElement = _nextFrameElement.addElement("P");
					_nextElement.addAttribute("Class", "ENUSCC");
					_nextElement.addText("&nbsp;");
				}
			}
		}
		XMLWriter writer = null;
		/** éæ¤æ·å¼éæ¤æ·éæ¤æ·éï¿½éæ¤æ·éæ¤æ·IEéæ¤æ·éæ­ä¼æ·éï¿½*/
		OutputFormat format = OutputFormat.createPrettyPrint();
		/** æéæ¤æ·XMLéæ¤æ·éæ¤æ· */
		
		format.setEncoding("UTF-8");
		format.setIndent(true);
		format.setSuppressDeclaration(true);
		format.setExpandEmptyElements(true);
		format.setTrimText(false);
		try{
		writer = new XMLWriter(new FileWriter(new File(filepath.replaceFirst("xml", "smi"))),format);
		writer.setEscapeText(false);
		writer.write(subtitleElement);
		writer.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public void toJSON(){
		String json_string= "[";
		for(int i = 0; i < frameList.size(); i++ )
		{
			String subtitle = "";
			if(i != 0)
				subtitle +=",";
			Frame frame = frameList.get(i);
			// must use "" instead of ' to use data!
			subtitle += "{start:\""+frame.getStart()+"\",end:\""+frame.getEnd()+"\",url:\""+frame.getSrc().replaceFirst("http://moviestoreclickplayhk.blob.core.windows.net/content/136/full_movie/subtitle/", "")+"\"}";
			json_string += subtitle;
			
		}
		json_string += "]";
		try{
			File file = new File(filepath.replaceFirst("xml", "txt"));	
			if(!file.exists()){ //判断存在不存在,不存在就创建
			file.createNewFile();	
			}
			PrintWriter pw = new PrintWriter(file); //创建字符打印流
			pw.println(json_string); //需求如果成功,就往文件里写.
			pw.close();	
		}catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		

		
		
	}
}
