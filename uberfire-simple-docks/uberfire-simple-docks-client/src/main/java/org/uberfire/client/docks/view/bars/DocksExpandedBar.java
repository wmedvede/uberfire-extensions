/*
 * Copyright 2015 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.uberfire.client.docks.view.bars;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.*;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.github.gwtbootstrap.client.ui.resources.ButtonSize;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import org.uberfire.client.docks.view.menu.MenuBuilder;
import org.uberfire.client.resources.WebAppResource;
import org.uberfire.client.workbench.docks.UberfireDockPosition;
import org.uberfire.mvp.ParameterizedCommand;
import org.uberfire.workbench.model.menu.*;
import org.uberfire.workbench.model.menu.MenuItem;

import java.util.ArrayList;
import java.util.List;

import static com.github.gwtbootstrap.client.ui.resources.ButtonSize.MINI;

public class DocksExpandedBar
        extends Composite implements ProvidesResize, RequiresResize {

    private UberfireDockPosition position;

    @UiField
    FlowPanel titlePanel;

    @UiField
    FlowPanel targetPanel;

    Button collapse;

    Heading title;

    @Override
    public void onResize() {

    }

    interface ViewBinder
            extends
            UiBinder<Widget, DocksExpandedBar> {

    }

    private ViewBinder uiBinder = GWT.create(ViewBinder.class);

    private static WebAppResource CSS = GWT.create(WebAppResource.class);

    public DocksExpandedBar(UberfireDockPosition position) {
        initWidget(uiBinder.createAndBindUi(this));
        this.position = position;
    }

    public void setup(String titleString,
                      ParameterizedCommand<String> deselectCommand) {
        clear();
        createTitle(titleString);
        createButtons(titleString, deselectCommand);
        setupComponents();
        setupCSS();
    }


    private void setupComponents() {
        if (position == UberfireDockPosition.SOUTH) {
            titlePanel.add(collapse);
            titlePanel.add(title);
        } else if (position == UberfireDockPosition.WEST) {
            titlePanel.add(title);
            titlePanel.add(collapse);
        } else if (position == UberfireDockPosition.EAST) {
            titlePanel.add(collapse);
            titlePanel.add(title);
        }
    }

    public void addMenus(Menus menus, MenuBuilder menuBuilder) {
        for (MenuItem menuItem : menus.getItems()) {
            final Widget result = menuBuilder.makeItem(menuItem, true);
            if (result != null) {
                final ButtonGroup bg = new ButtonGroup();
                bg.addStyleName(CSS.CSS().dockExpandedContentButton());
                bg.add(result);
                titlePanel.add(bg);
            }
        }
    }

    private void createTitle(String titleString) {
        title = new Heading(5);
        title.setText(titleString);
    }

    private void createButtons(final String identifier,
                               final ParameterizedCommand<String> deselectCommand) {

        collapse = GWT.create(Button.class);
        collapse.setSize(ButtonSize.MINI);
        collapse.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                deselectCommand.execute(identifier);
            }
        });
    }

    private void setupCSS() {

        if (position == UberfireDockPosition.SOUTH) {
            titlePanel.addStyleName(CSS.CSS().dockExpandedContentPanelSouth());
            title.addStyleName(CSS.CSS().dockExpandedLabelSouth());
            collapse.setIcon(IconType.CHEVRON_DOWN);
            collapse.addStyleName(CSS.CSS().dockExpandedButtonSouth());
        } else if (position == UberfireDockPosition.WEST) {
            title.addStyleName(CSS.CSS().dockExpandedLabelWest());
            collapse.setIcon(IconType.CHEVRON_LEFT);
            collapse.addStyleName(CSS.CSS().dockExpandedButtonWest());
        } else if (position == UberfireDockPosition.EAST) {
            title.addStyleName(CSS.CSS().dockExpandedLabelEast());
            collapse.setIcon(IconType.CHEVRON_RIGHT);
            collapse.addStyleName(CSS.CSS().dockExpandedButtonEast());
        }
        setupDockContentSize();
    }

    public void setupDockContentSize() {
        //  goTo( PlaceRequest place, HasWidgets addTo ) lost widget size
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                targetPanel.setSize((getOffsetWidth()) + "px", getOffsetHeight() + "px");
            }
        });
    }


    public void setPanelSize(int width,
                             int height) {
        targetPanel.setPixelSize(width, height);
    }

    public FlowPanel targetPanel() {
        return targetPanel;
    }

    public void clear() {
        targetPanel.clear();
        titlePanel.clear();
    }

    public UberfireDockPosition getPosition() {
        return position;
    }


}
