package com.vaadin.zindex.client.ui;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.PopupPanel;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.BrowserInfo;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.ui.VWindow;

public class VZIndexWindow extends VWindow implements Paintable {

	/**
	 * iframe used to hide a part of the applet
	 */
	private Element iFrame;

	@Override
	public void updateFromUIDL(final UIDL uidl,
			final ApplicationConnection client) {
		super.updateFromUIDL(uidl, client);
		// Nothing to do with Firefox
		if (!BrowserInfo.get().isFirefox()) {
			if (isVisible()) {
				if (iFrame == null) {
					// iframe creation
					iFrame = DOM.createIFrame();
					final Style iFrameStyle = iFrame.getStyle();
					iFrameStyle.setZIndex(1);
					iFrameStyle.setPosition(Position.ABSOLUTE);
					iFrameStyle.setOpacity(0);
					iFrameStyle.setBorderStyle(BorderStyle.NONE);
				}
				if (!iFrame.hasParentElement()) {
					// iframe attachement
					getElement().getParentElement().insertBefore(iFrame,
							getElement());
				}
				// update iframe position
				updateIFramePosition();
			} else if (iFrame != null) {
				iFrame.removeFromParent();
			}
		}
	}

	@Override
	public void onBrowserEvent(final Event event) {
		super.onBrowserEvent(event);
		// update iframe position on events
		updateIFramePosition();
	}

	@Override
	public boolean onEventPreview(Event event) {
		final boolean eventPreview = super.onEventPreview(event);
		// update iframe position on events
		updateIFramePosition();
		return eventPreview;
	}

	@Override
	public void onClose(final CloseEvent<PopupPanel> event) {
		super.onClose(event);
		iFrame.removeFromParent();
	}

	/**
	 * adapt iframe position and size so as to hide the applet only where the
	 * popup is displaid
	 */
	private void updateIFramePosition() {
		final Style iFrameStyle = iFrame.getStyle();
		iFrameStyle.setVisibility(Visibility.VISIBLE);
		iFrameStyle.setTop(getAbsoluteTop(), Unit.PX);
		iFrameStyle.setLeft(getAbsoluteLeft(), Unit.PX);
		iFrameStyle.setHeight(getOffsetHeight(), Unit.PX);
		iFrameStyle.setWidth(getOffsetWidth(), Unit.PX);
	}
}
