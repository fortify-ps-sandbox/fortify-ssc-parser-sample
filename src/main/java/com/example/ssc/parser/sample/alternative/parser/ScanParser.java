package com.example.ssc.parser.sample.alternative.parser;

import java.util.Map;

import com.fortify.plugin.api.ScanBuilder;

public class ScanParser extends AbstractParser {
    private final ScanBuilder scanBuilder;
    
	public ScanParser(ScanBuilder scanBuilder) {
		this.scanBuilder = scanBuilder;
	}
	
	@Override
	protected void addHandlers(Map<String, Handler> pathToHandlerMap) {
		pathToHandlerMap.put("/engineVersion", 
				jp -> scanBuilder.setEngineVersion(jp.getValueAsString()));
		pathToHandlerMap.put("/scanDate", 
				jp -> scanBuilder.setScanDate(DATE_DESERIALIZER.convert(jp.getValueAsString())));
		pathToHandlerMap.put("/buildServer", 
				jp -> scanBuilder.setHostName(jp.getValueAsString()));
		pathToHandlerMap.put("/elapsed", 
				jp -> scanBuilder.setElapsedTime(jp.getValueAsInt()));
		
	}
	
	@Override
	protected <T> T finish() {
		scanBuilder.completeScan();
		return null;
	}
}
