/*******************************************************************************
 * (c) Copyright 2020 Micro Focus or one of its affiliates
 *
 * Permission is hereby granted, free of charge, to any person obtaining a 
 * copy of this software and associated documentation files (the 
 * "Software"), to deal in the Software without restriction, including without 
 * limitation the rights to use, copy, modify, merge, publish, distribute, 
 * sublicense, and/or sell copies of the Software, and to permit persons to 
 * whom the Software is furnished to do so, subject to the following 
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be included 
 * in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY 
 * KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE 
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR 
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN 
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS 
 * IN THE SOFTWARE.
 ******************************************************************************/
package com.example.ssc.parser.sample;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import com.example.ssc.parser.sample.alternative.SampleParserPlugin;
import com.fortify.plugin.api.ScanBuilder;
import com.fortify.plugin.api.ScanData;
import com.fortify.plugin.api.ScanEntry;
import com.fortify.plugin.api.StaticVulnerabilityBuilder;
import com.fortify.plugin.api.VulnerabilityHandler;

class SampleParserPluginTest {
	private final ScanData scanData = new ScanData() {
		
		@Override
		public String getSessionId() {
			return UUID.randomUUID().toString();
		}
		
		@Override
		public List<ScanEntry> getScanEntries() {
			return null;
		}
		
		@Override
		public InputStream getInputStream(Predicate<String> matcher) throws IOException {
			return ClassLoader.getSystemResourceAsStream("fixed-sample-scan.json");
		}
		
		@Override
		public InputStream getInputStream(ScanEntry scanEntry) throws IOException {
			return ClassLoader.getSystemResourceAsStream("fixed-sample-scan.json");
		}
	};
	
	private final ScanBuilder scanBuilder = (ScanBuilder) Proxy.newProxyInstance(
			SampleParserPluginTest.class.getClassLoader(), 
			  new Class[] { ScanBuilder.class }, new InvocationHandler() {
				
				@Override
				public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
					System.err.println(method.getName()+": "+(args==null?null:Arrays.asList(args)));
					return null;
				}
			});
	
	private final VulnerabilityHandler vulnerabilityHandler = new VulnerabilityHandler() {
		
		@Override
		public StaticVulnerabilityBuilder startStaticVulnerability(String instanceId) {
			System.err.println("startStaticVulnerability: "+instanceId);
			return (StaticVulnerabilityBuilder) Proxy.newProxyInstance(
					SampleParserPluginTest.class.getClassLoader(), 
					  new Class[] { StaticVulnerabilityBuilder.class }, new InvocationHandler() {
						
						@Override
						public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
							System.err.println(method.getName()+": "+(args==null?null:Arrays.asList(args)));
							return null;
						}
					}); 
		}
	};
	
	@Test
	void testParseScan() throws Exception {
		new SampleParserPlugin().parseScan(scanData, scanBuilder);
		// TODO Check actual output
	}
	
	@Test
	void testParseVulnerabilities() throws Exception {
		new SampleParserPlugin().parseVulnerabilities(scanData, vulnerabilityHandler);
		// TODO Check actual output
	}

}
