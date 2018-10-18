/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.sling.cms.insights;

/**
 * Represents a message which may be assigned a status and text
 */
public class Message {

    public enum STYLE {
        DANGER, DEFAULT, SUCCESS, WARNING
    }

    public static Message danger(String text) {
        return new Message(text, STYLE.DANGER);
    }

    public static Message defaultMsg(String text) {
        return new Message(text, STYLE.DEFAULT);
    }

    public static Message success(String text) {
        return new Message(text, STYLE.SUCCESS);
    }

    public static Message warn(String text) {
        return new Message(text, STYLE.WARNING);
    }

    private STYLE style;

    private String text;

    public Message(String text, STYLE style) {
        this.text = text;
        this.style = style;
    }

    public STYLE getStyle() {
        return style;
    }
    
    public String getStyleClass() {
        String styleClass = "";
        if (style != STYLE.DEFAULT) {
            styleClass = "is-" + style.toString().toLowerCase();
        }
        return styleClass;
    }

    public String getText() {
        return text;
    }

    public void setStyle(STYLE style) {
        this.style = style;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Message [style=" + style + ", text=" + text + "]";
    }
}
