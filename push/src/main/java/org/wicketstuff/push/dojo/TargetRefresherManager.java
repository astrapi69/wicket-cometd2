/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.wicketstuff.push.dojo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxRequestTarget.IJavaScriptResponse;
//import org.apache.wicket.ajax.AjaxRequestTarget.IJavascriptResponse;
import org.apache.wicket.ajax.AjaxRequestTarget.IListener;
import org.apache.wicket.behavior.Behavior;
//import org.apache.wicket.behavior.IBehavior;

/**
 * A Manager to deal with {@link AjaxRequestTarget} and makeWidget in dojo. Only
 * top level dojoComponents needs to be rerender during a response to a Ajax
 * Query
 * 
 * @author Vincent Demay
 */
public class TargetRefresherManager implements IListener {
	private static TargetRefresherManager instance;
	private Map<String, Component> dojoComponents;

	private TargetRefresherManager() {
		dojoComponents = new HashMap<String, Component>();
	}

	public static TargetRefresherManager getInstance() {
		if (instance == null) {
			instance = new TargetRefresherManager();
		}
		return instance;
	}

	public void onAfterRespond(final Map<String, Component> map,
			final IJavaScriptResponse response) {
		// we need to find all dojoWidget that should be reParsed
		final Map<String, Component> real = new HashMap<String, Component>();
		String requires = "";

		for (final Component c : dojoComponents.values()) {
			for (final Behavior behavior : c.getBehaviors()) {
				if (behavior instanceof AbstractRequireDojoBehavior) {
					requires += ((AbstractRequireDojoBehavior) behavior)
							.getRequire();
				}
			}
		}
		dojoComponents = real;

		if (generateReParseJs() != null) {
			response.addJavaScript(requires + generateReParseJs());
		}
		instance = null;

	}

	public void onBeforeRespond(final Map<String, Component> map,
			final AjaxRequestTarget target) {
		// Null op
	}

	/**
	 *
	 */
	public void addComponent(final Component component) {
		dojoComponents.put(component.getMarkupId(), component);
	}

	private String generateReParseJs() {
		if (!dojoComponents.isEmpty()) {
			final Iterator<Component> it = dojoComponents.values().iterator();
			String parseJs = "[";
			while (it.hasNext()) {
				final Component c = it.next();
				parseJs += "'" + c.getMarkupId() + "',";
			}
			parseJs = parseJs.substring(0, parseJs.length() - 1);
			parseJs += "]";
			return "djConfig.searchIds = " + parseJs
					+ ";dojo.hostenv.makeWidgets();";
		} else {
			return null;
		}
	}

}
