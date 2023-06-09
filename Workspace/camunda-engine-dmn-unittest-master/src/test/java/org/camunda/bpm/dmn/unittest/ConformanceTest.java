/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.dmn.unittest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.time.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;


import org.camunda.bpm.dmn.engine.DmnDecision;
import org.camunda.bpm.dmn.engine.DmnDecisionRuleResult;
import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.dmn.engine.test.DmnEngineRule;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.junit.Rule;
import org.junit.Test;

public class ConformanceTest {

	@Rule
	public DmnEngineRule rule = new DmnEngineRule();

	@Test
	public void shouldEvaluateDecision() throws IOException, ParseException {

		// Get DMN engine
		DmnEngine dmnEngine = rule.getDmnEngine();

		// Parse decision
		InputStream inputStream = getClass().getResourceAsStream("CC.dmn");
		// chose which decision from the CC.dmn you want to execute
//		DmnDecision decision = dmnEngine.parseDecision("participationDecision", inputStream);
//		DmnDecision decision = dmnEngine.parseDecision("atmostoneDecision", inputStream);
//		DmnDecision decision = dmnEngine.parseDecision("initDecision", inputStream);
//		DmnDecision decision = dmnEngine.parseDecision("endDecision", inputStream);
//		DmnDecision decision = dmnEngine.parseDecision("respondedExistenceDecision", inputStream);
//		DmnDecision decision = dmnEngine.parseDecision("responseDecision", inputStream);
//		DmnDecision decision = dmnEngine.parseDecision("alternateResponseDecision", inputStream);
//		DmnDecision decision = dmnEngine.parseDecision("chainresponseDecision", inputStream);
//		DmnDecision decision = dmnEngine.parseDecision("precedenceDecision", inputStream);
//		DmnDecision decision = dmnEngine.parseDecision("alternatePrecedenceDecision", inputStream);
//		DmnDecision decision = dmnEngine.parseDecision("chainPrecedenceDecision", inputStream);
		DmnDecision decision = dmnEngine.parseDecision("coExistenceDecision", inputStream);

		// Set input variables
		VariableMap variables = Variables.createVariables();

		try (// parsing a CSV file into buffered reader class constructor
				BufferedReader csvread = new BufferedReader(
						// taking different CSV files (event logs) for each different constraint pattern to make sure
						// to test relevant things
						
//						new FileReader("C:\\DMN CC\\dmn-cc\\Event Logs\\DECLARE Constraints\\Participation.csv"))) 
						// multiple "Send invoice"
// 						new FileReader("C:\\DMN CC\\dmn-cc\\Event Logs\\DECLARE Constraints\\AtMostOne1.csv"))) 
						// no "Send invoice"
//						new FileReader("C:\\DMN CC\\dmn-cc\\Event Logs\\DECLARE Constraints\\AtMostOne2.csv"))) 
//						new FileReader("C:\\DMN CC\\dmn-cc\\Event Logs\\DECLARE Constraints\\Init.csv"))) 
//						new FileReader("C:\\DMN CC\\dmn-cc\\Event Logs\\DECLARE Constraints\\End.csv"))) 	
//						new FileReader("C:\\DMN CC\\dmn-cc\\Event Logs\\DECLARE Constraints\\RespondedExistence.csv"))) 
//						new FileReader("C:\\DMN CC\\dmn-cc\\Event Logs\\DECLARE Constraints\\Response.csv"))) 
//						new FileReader("C:\\DMN CC\\dmn-cc\\Event Logs\\DECLARE Constraints\\AlternateResponse.csv"))) 
//						new FileReader("C:\\DMN CC\\dmn-cc\\Event Logs\\DECLARE Constraints\\ChainResponse.csv"))) 
//						new FileReader("C:\\DMN CC\\dmn-cc\\Event Logs\\DECLARE Constraints\\Precedence.csv")))
//						new FileReader("C:\\DMN CC\\dmn-cc\\Event Logs\\DECLARE Constraints\\AlternatePrecedence.csv")))
//						new FileReader("C:\\DMN CC\\dmn-cc\\Event Logs\\DECLARE Constraints\\ChainPrecedence.csv")))
						new FileReader("C:\\DMN CC\\dmn-cc\\Event Logs\\DECLARE Constraints\\CoExistence.csv")))
		{
			
			String line = "";
			// Do the first readLine here to "remove" the headline
			csvread.readLine();
			// Set the formatter for the type of timestamp we have in the event log
		    SimpleDateFormat formatter = new SimpleDateFormat("d/MM/yyyy H:mm");
		    // Initialize the list of all events that will be passed to the engine
		    List<Object> eventlist = new ArrayList<>();
		    // Go over every line of the CSV and add one entry to the list for each event
			while ((line = csvread.readLine()) != null) {
				String[] setup = line.split(";");
				List<Object> event = new ArrayList<>(Arrays.asList(
						setup[0],
						formatter.parse(setup[1])));
				eventlist.add(event);
			}
			// add the list of events (list of lists) to the variables that will be send to the dmn engine
			variables.putValue("eventlist", eventlist);
			System.out.println(eventlist);
		}

		// Evaluate decision with id from file defined above while "Parse decision"
		DmnDecisionTableResult results = dmnEngine.evaluateDecisionTable(decision, variables);

		// Check that one rule has matched and print the result of that rule
		assertThat(results).hasSize(1);
		System.out.println(results);

		DmnDecisionRuleResult result = results.getSingleResult();
		assertThat(result).containsOnly(entry("conformance", true)
//        entry("reason", "you took too much man, you took too much!")
		);

//// 	  Change input variables
//		variables.putValue("status", "gold");
//
////    Evaluate decision again
//		results = dmnEngine.evaluateDecisionTable(decision, variables);
//
////    Check new result
//		assertThat(results).hasSize(1);
//
//		result = results.getSingleResult();
//		assertThat(result).containsOnly(entry("result", "ok"), entry("reason", "you get anything you want"));
	}

}
