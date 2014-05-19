package org.wicketstuff.push.cometd;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.util.string.StringValue;
import org.wicketstuff.push.IChannelListener;
import org.wicketstuff.push.dojo.DojoPackagedTextTemplate;

public class CometdBehavior extends CometdAbstractBehavior {
	private static final long serialVersionUID = 1L;

	private final IChannelListener listener;

	public CometdBehavior(final String channelId, final IChannelListener listener) {
		super(channelId);
		this.listener = listener;
	}

	@Override
	public final String getCometdInterceptorScript() {
		
		final Map<String, Object> map = new HashMap<String, Object>();
		map.put("behaviorMarkupId", getBehaviorMarkupId());
		map.put("url", getCallbackUrl().toString());
		
		return new DojoPackagedTextTemplate(CometdBehavior.class, "CometdDefaultBehaviorTemplate.js").asString(map);
	}

	@Override
	public final CharSequence getPartialSubscriber() {
		return "'onEventFor" + getBehaviorMarkupId() + "'";
	}

	@Override
	protected final void respond(final AjaxRequestTarget target) {
		final Map<String, String[]> map = getParameterMap(RequestCycle.get().getRequest());
		final Map<String, String> eventAttribute = new HashMap<String, String>();

		for (final Map.Entry<String, String[]> entry : map.entrySet()) {
			eventAttribute.put(entry.getKey(), entry.getValue()[0]);
		}

		final CometdTarget cTarget = new CometdTarget(target);
		listener.onEvent(getChannelId(), eventAttribute, cTarget);

	}
	


	/**
	 * Gets a map with all parameters. Looks in the query
	 * and post parameters. Migration method from 1.4.* to 1.5.*.
	 * 
	 * @param request
	 *            the request
	 * @return a map with all parameters.
	 */
	public static Map<String, String[]> getParameterMap(Request request) {
		IRequestParameters parameters = request.getRequestParameters();
		final Map<String, String[]> map = new HashMap<String, String[]>();
		Set<String> parameterNames = parameters.getParameterNames();
		for (String parameterName : parameterNames) {
			List<StringValue> parameterValues = parameters.getParameterValues(parameterName);
			String[] stringArray = new String[parameterValues.size()];
			if(parameterValues != null && !parameterValues.isEmpty()) {				
				for (int i = 0; i < parameterValues.size(); i++) {
					stringArray[i] = parameterValues.get(i).toString();
				}
			}
			map.put(parameterName, stringArray);
		}
		return map;
	}

}
