package org.wicketstuff.push.examples.pages;

import org.apache.wicket.request.mapper.parameter.PageParameters;

public class WicketTimerChatPanelPage extends ExamplePage {
	private static final long serialVersionUID = 1L;

	public WicketTimerChatPanelPage(PageParameters parameters) {
		add(new WicketChatPanel("chatPanel", "chatroom1"));
		add(new WicketChatPanel("chatPanel2", "chatroom2"));
	}

}
