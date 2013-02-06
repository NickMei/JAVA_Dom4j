package model;

public class Frame {

	/**
	 * @param args
	 */
	String width;
	String height;
	String src;
	String start;
	String end;
	public Frame(String width, String height, String src, String start, String end){
		this.width = width;
		this.height = height;
		this.src = src;
		this.start = start;
		this.end = end;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}

}
