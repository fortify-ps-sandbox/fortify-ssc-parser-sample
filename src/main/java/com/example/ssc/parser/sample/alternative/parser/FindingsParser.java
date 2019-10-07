package com.example.ssc.parser.sample.alternative.parser;

import java.util.Date;
import java.util.Map;

import com.example.ssc.parser.sample.alternative.CustomVulnAttribute;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fortify.plugin.api.BasicVulnerabilityBuilder.Priority;
import com.fortify.plugin.api.StaticVulnerabilityBuilder;
import com.fortify.plugin.api.VulnerabilityHandler;

public class FindingsParser extends AbstractParser {
	private enum CustomStatus {
        NEW, OPEN, REMEDIATED;
    };

	
	private final VulnerabilityHandler vulnerabilityHandler;

    public FindingsParser(VulnerabilityHandler vulnerabilityHandler) {
		this.vulnerabilityHandler = vulnerabilityHandler;
	}
    
    @Override
    protected void addHandlers(Map<String, Handler> pathToHandlerMap) {
    	pathToHandlerMap.put("/findings", jp->parseArrayEntries(jp, ()->new FindingParser()));
    }
        
    private final class FindingParser extends AbstractParser {    	
    	// mandatory attributes
        @JsonProperty private String uniqueId;

        // builtin attributes
        @JsonProperty private String category;
        @JsonProperty private String fileName;
        @JsonProperty private String vulnerabilityAbstract;
        @JsonProperty private Integer lineNumber;
        @JsonProperty private Float confidence;
        @JsonProperty private Float impact;
        @JsonProperty private Priority priority;

        // custom attributes
        @JsonProperty private String categoryId;
        @JsonProperty private String artifact;
        @JsonProperty private String description;
        @JsonProperty private String comment;
        @JsonProperty private String buildNumber;
        @JsonProperty private CustomStatus customStatus;
        @JsonProperty private Date lastChangeDate;
        @JsonProperty private Date artifactBuildDate;
        @JsonProperty private String textBase64;
    	
    	@Override
    	protected <T> T finish() {
    		StaticVulnerabilityBuilder vb = vulnerabilityHandler.startStaticVulnerability(uniqueId);
    		// Set builtin attributes
            vb.setCategory(category);                             // REST -> issueName
            vb.setFileName(fileName);                             // REST -> fullFileName or shortFileName
            vb.setVulnerabilityAbstract(vulnerabilityAbstract);   // REST -> brief
            vb.setLineNumber(lineNumber);                         // REST -> N/A, UI issue table -> part of Primary Location
            vb.setConfidence(confidence);                         // REST -> confidence
            vb.setImpact(impact);                                 // REST -> impact
            vb.setPriority(priority);                             // REST -> friority, UI issue table -> Criticality
            
            vb.setStringCustomAttributeValue(CustomVulnAttribute.uniqueId, uniqueId);
            vb.setStringCustomAttributeValue(CustomVulnAttribute.categoryId, categoryId);
            vb.setStringCustomAttributeValue(CustomVulnAttribute.artifact, artifact);
            vb.setStringCustomAttributeValue(CustomVulnAttribute.buildNumber, buildNumber);
            vb.setStringCustomAttributeValue(CustomVulnAttribute.customStatus, customStatus.name());
            vb.setStringCustomAttributeValue(CustomVulnAttribute.description, description);
            vb.setStringCustomAttributeValue(CustomVulnAttribute.comment, comment);
            vb.setStringCustomAttributeValue(CustomVulnAttribute.textBase64, textBase64);
            vb.setDateCustomAttributeValue(CustomVulnAttribute.lastChangeDate, lastChangeDate);
            vb.setDateCustomAttributeValue(CustomVulnAttribute.artifactBuildDate, artifactBuildDate);
    		vb.completeVulnerability();
    		return null;
    	}
    }
}
