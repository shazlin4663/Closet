package com.app.closet;

import com.parse.ParseObject;

public class ImageData {
	private ParseObject _parseCloth, _parsePant, _parseShoe, _parseAccessory;
	
	public ParseObject getParseCloth() {
		return _parseCloth;
	}
	public void setParseCloth(ParseObject parseCloth) {
		this._parseCloth = parseCloth;
	}
	public ParseObject getParsePant() {
		return _parsePant;
	}
	public void setParsePant(ParseObject parsePant) {
		this._parsePant = parsePant;
	}
	public ParseObject getParseShoe() {
		return _parseShoe;
	}
	public void setParseShoe(ParseObject parseShoe) {
		this._parseShoe = parseShoe;
	}
	public ParseObject getParseAccessory() {
		return _parseAccessory;
	}
	public void setParseAccessory(ParseObject parseAccessory) {
		this._parseAccessory = parseAccessory;
	}
	
	

}
