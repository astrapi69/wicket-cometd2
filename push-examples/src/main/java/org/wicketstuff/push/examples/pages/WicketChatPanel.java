package org.wicketstuff.push.examples.pages;

import org.wicketstuff.push.IChannelService;
import org.wicketstuff.push.examples.application.ExampleApplication;

public class WicketChatPanel extends WicketAbstractChatPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WicketChatPanel(String id, final String channel) {
		super(id, channel);
	}

	protected IChannelService getChannelService() {		
		return ((ExampleApplication) ExampleApplication.get()).getTimerChannelService();
	}

}
