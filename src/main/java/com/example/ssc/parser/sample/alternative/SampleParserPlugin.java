package com.example.ssc.parser.sample.alternative;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.ssc.parser.sample.alternative.parser.FindingsParser;
import com.example.ssc.parser.sample.alternative.parser.ScanParser;
import com.fortify.plugin.api.ScanBuilder;
import com.fortify.plugin.api.ScanData;
import com.fortify.plugin.api.ScanParsingException;
import com.fortify.plugin.api.VulnerabilityHandler;
import com.fortify.plugin.spi.ParserPlugin;

public class SampleParserPlugin implements ParserPlugin<CustomVulnAttribute> {
    private static final Logger LOG = LoggerFactory.getLogger(SampleParserPlugin.class);

    @Override
    public void start() throws Exception {
        LOG.info("SampleParserPlugin plugin is starting");
    }

    @Override
    public void stop() throws Exception {
        LOG.info("SampleParserPlugin plugin is stopping");
    }

    @Override
    public Class<CustomVulnAttribute> getVulnerabilityAttributesClass() {
        return CustomVulnAttribute.class;
    }

    @Override
    public void parseScan(final ScanData scanData, final ScanBuilder scanBuilder) throws ScanParsingException, IOException {
        new ScanParser(scanBuilder).parse(scanData);
    }

	@Override
	public void parseVulnerabilities(ScanData scanData, VulnerabilityHandler vulnerabilityHandler) throws ScanParsingException, IOException {
		new FindingsParser(vulnerabilityHandler).parse(scanData);
	}
}
