package org.wicketstuff.push.timer;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.util.time.Duration;
import org.wicketstuff.push.IPushService;
import org.wicketstuff.push.IPushTarget;

/**
 * An implementation of IPushService based on a polling mechanism.
 * 
 * This class is thread safe, and can be safely reused.
 * 
 * @author Xavier Hanin
 */
public class TimerPushService implements IPushService {
	private static final long serialVersionUID = 1L;

	private final Duration duration;

	/**
	 * Constructs a TimerPushService with the given polling interval.
	 * 
	 * @param duration
	 *            the polling interval, must not be null
	 */
	public TimerPushService(final Duration duration) {
		if (duration == null) {
			throw new IllegalArgumentException("duration must not be null");
		}
		this.duration = duration;
	}

	public IPushTarget installPush(final Component component) {
		final TimerChannelBehavior tcb = new TimerChannelBehavior(duration);
		component.add(tcb);
		return tcb.newPushTarget();
	}

	public void uninstallPush(final Component component) {
		for (final Behavior behavior : component.getBehaviors()) {
			if (behavior instanceof TimerChannelBehavior) {
				component.remove(behavior);
			}
		}
	}

	/**
	 * Returns the polling interval
	 * 
	 * @return the polling interval
	 */
	public Duration getDuration() {
		return duration;
	}

}
