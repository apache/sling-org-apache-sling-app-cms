<%-- /*
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
 */ --%>
 <%@include file="/libs/sling-cms/global.jsp"%>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title><fmt:message key="${properties['jcr:title']}" var="title" />${sling:encode(title,'HTML')} :: ${sling:encode(branding.appName,'HTML')}</title>
    <link href="${sling:encode(branding.css, 'HTML_ATTR')}" rel="stylesheet" />
    <link rel="apple-touch-icon" sizes="180x180" href="${sling:encode(branding.appleTouchIcon, 'HTML_ATTR')}" />
    <link rel="icon" type="image/png" sizes="32x32" href="${sling:encode(branding.favicon32, 'HTML_ATTR')}" />
    <link rel="icon" type="image/png" sizes="16x16" href="${sling:encode(branding.favicon16, 'HTML_ATTR')}" />
    <link rel="shortcut icon" href="${sling:encode(branding.favicon, 'HTML_ATTR')}" />
    <meta name="apple-mobile-web-app-title" content="${sling:encode(branding.appName,'HTML_ATTR')}" />
    <meta name="application-name" content="${sling:encode(branding.appName,'HTML_ATTR')}" />
    <link rel="mask-icon" href="${sling:encode(branding.appleMaskIcon, 'HTML_ATTR')}" color="${branding.tileColor}" />
    <meta name="msapplication-TileColor" content="${sling:encode(branding.tileColor, 'HTML_ATTR')}" />
    <meta name="theme-color" content="${sling:encode(branding.tileColor, 'HTML_ATTR')}" />
    <meta name="msapplication-config" content="${sling:encode(branding.browserConfig, 'HTML_ATTR')}">
    <link rel="manifest" href="${sling:encode(branding.webManifest, 'HTML_ATTR')}">
</head>