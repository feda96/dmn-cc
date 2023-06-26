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



import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
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

		// Parse DMN model
		InputStream inputStream = getClass().getResourceAsStream("CC.dmn");
		// creating a decisionList where all decisions are added
		List<DmnDecision> decisionList = dmnEngine.parseDecisions(inputStream);
		// Set input variables
		VariableMap variables = Variables.createVariables();
		// Manual input of the event names we are using to do conformance checking
		variables.putValue("event1", "a");
		variables.putValue("event2", "b");
		
		try (// parsing a CSV file into buffered reader class constructor
				BufferedReader csvread = new BufferedReader(
						// CSV files (event logs) for each different constraint pattern 
//						new FileReader("C:\\DMN CC\\dmn-cc\\Event Logs\\DECLARE Constraints\\Participation.csv"))) 
// 						new FileReader("C:\\DMN CC\\dmn-cc\\Event Logs\\DECLARE Constraints\\AtMostOne.csv"))) 
//						new FileReader("C:\\DMN CC\\dmn-cc\\Event Logs\\DECLARE Constraints\\Init.csv"))) 
//						new FileReader("C:\\DMN CC\\dmn-cc\\Event Logs\\DECLARE Constraints\\End.csv"))) 	
//						new FileReader("C:\\DMN CC\\dmn-cc\\Event Logs\\DECLARE Constraints\\RespondedExistence.csv"))) 
//						new FileReader("C:\\DMN CC\\dmn-cc\\Event Logs\\DECLARE Constraints\\Response.csv"))) 
//						new FileReader("C:\\DMN CC\\dmn-cc\\Event Logs\\DECLARE Constraints\\AlternateResponse.csv"))) 
//						new FileReader("C:\\DMN CC\\dmn-cc\\Event Logs\\DECLARE Constraints\\ChainResponse.csv"))) 
//						new FileReader("C:\\DMN CC\\dmn-cc\\Event Logs\\DECLARE Constraints\\Precedence.csv")))
//						new FileReader("C:\\DMN CC\\dmn-cc\\Event Logs\\DECLARE Constraints\\AlternatePrecedence.csv")))
//						new FileReader("C:\\DMN CC\\dmn-cc\\Event Logs\\DECLARE Constraints\\ChainPrecedence.csv")))
//						new FileReader("C:\\DMN CC\\dmn-cc\\Event Logs\\DECLARE Constraints\\CoExistence.csv")))
//						new FileReader("C:\\DMN CC\\dmn-cc\\Event Logs\\DECLARE Constraints\\Succession.csv")))
//						new FileReader("C:\\DMN CC\\dmn-cc\\Event Logs\\DECLARE Constraints\\AlternateSuccession.csv")))
//						new FileReader("C:\\DMN CC\\dmn-cc\\Event Logs\\DECLARE Constraints\\ChainSuccession.csv")))
//						new FileReader("C:\\DMN CC\\dmn-cc\\Event Logs\\DECLARE Constraints\\NotChainSuccession.csv")))
//						new FileReader("C:\\DMN CC\\dmn-cc\\Event Logs\\DECLARE Constraints\\NotSuccession.csv")))
//						new FileReader("C:\\DMN CC\\dmn-cc\\Event Logs\\DECLARE Constraints\\NotCoExistence.csv")))
						new FileReader("C:\\DMN CC\\dmn-cc\\Event Logs\\DECLARE Constraints\\TestFile.csv")))
		
		{

			String line = "";
			// Do the first readLine here to "remove" the headline
			csvread.readLine();
			// Set the formatter for the type of timestamp we have in the event log
		    SimpleDateFormat formatter = new SimpleDateFormat("d/MM/yyyy H:mm");
		    // Initialize the list of all events that will be passed to the engine
		    List<Object> eventlist = new ArrayList<>();
		    // Go over every line of the CSV and add one entry to the list for each event
		    // For a different structure of the event log, change the List<Object> event to match your needs.
		    // In case you change the structure of entry 0 being the name and entry 1 being the timestamp,
		    // you also need to change the structure of the DMN decisions. 
			while ((line = csvread.readLine()) != null) {
				String[] setup = line.split(";");
				List<Object> event = new ArrayList<>(Arrays.asList(
						setup[0],
						formatter.parse(setup[1])));
				eventlist.add(event);
			}
			// add the list of events (list of lists) to the variables that will be send to the dmn engine
			variables.putValue("eventlist", eventlist);
		}

		
		// evaluate all decisions
		List<Object> decisionResultsTrue = new ArrayList<>();
		List<Object> decisionResultsFalse = new ArrayList<>();
		//remember the number of ALL decisions
		int allDecisions = decisionList.size();
		while (!decisionList.isEmpty()) {
		// get the result of all of the decisions one by one
		DmnDecisionTableResult results = dmnEngine.evaluateDecisionTable(decisionList.get(0), variables);
		// if the result of the decision is true, add it to the decisionResultsTrue list else to decisionResultsFalse
		if((boolean) results.get(0).get("conformance")) {
			decisionResultsTrue.add(decisionList.get(0).getKey());
		}
		else {
			decisionResultsFalse.add(decisionList.get(0).getKey());
		}
		decisionList.remove(0);
		}
		// sysout the percentage of conformance and non-conformance decisions with the event log
		// also print out the decisions that are conform/non-conform
		System.out.println("Conformance:");
		System.out.println((float)decisionResultsTrue.size()/allDecisions*100+"% ("+decisionResultsTrue.size()+" out of " + allDecisions+")");
		System.out.println(decisionResultsTrue);
		
		System.out.println("non-Conformance");
		System.out.println((float)decisionResultsFalse.size()/allDecisions*100+"%  ("+decisionResultsFalse.size()+" out of " + allDecisions+")");
		System.out.println(decisionResultsFalse);
		
	}

}

