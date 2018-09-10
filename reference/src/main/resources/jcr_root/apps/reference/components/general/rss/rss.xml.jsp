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
 */ --%><rss version="2.0" xmlns:atom="http://www.w3.org/2005/Atom" xmlns:content="http://purl.org/rss/1.0/modules/content/" xmlns:dc="http://purl.org/dc/elements/1.1/">
<%@ page language="java" contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/libs/sling-cms/global.jsp"%>
	<c:set var="site" value="${sling:adaptTo(resource,'org.apache.sling.cms.SiteManager').site}" />
	<channel>
		<title>${sling:encode(site.title,'XML')}</title>
		<description>${sling:encode(site.description,'XML')}</description>
		<language>${site.locale.language}</language>
		<link>${site.url}</link>
		<image>
			<url>${site.url}${fn:replace(properties.image,site.path,'')}</url>
			<title>${sling:encode(site.title,'XML')}</title>
			<link>${site.url}</link>
		</image>
		<atom:link href="${site.url}${fn:replace(resource.path,site.path,'')}.xml" rel="self" type="application/rss+xml" />
		<c:set var="query" value="SELECT * FROM [sling:Page] WHERE ISDESCENDANTNODE([${site.path}/${properties.subpath}]) AND [jcr:content/published]=true ORDER BY [jcr:content/publishDate] DESC" />
		<c:forEach var="postRsrc" items="${sling:findResources(resourceResolver,query,'JCR-SQL2')}" end="9">
			<item>
				<c:set var="post" value="${sling:adaptTo(postRsrc,'org.apache.sling.cms.PageManager').page}" />
				<title><sling:encode value="${post.title}" mode="XML" /></title>
				<dc:creator><sling:encode value="${post.properties.author}" mode="XML" /></dc:creator>
				<description><sling:encode value="${post.properties['jcr:description']}" mode="XML" /></description>
				<c:choose>
					<c:when test="${fn:startsWith(post.properties['sling:thumbnail'],'http') && fn:indexOf(post.properties['sling:thumbnail'],'.png') != -1}">
						<c:set var="thumbLink" value="${fn:replace(post.properties['sling:thumbnail'],'https:','http:')}" />
						<c:set var="thumbType" value="image/png" />
					</c:when>
					<c:when test="${fn:startsWith(post.properties['sling:thumbnail'],'http') && fn:indexOf(post.properties['sling:thumbnail'],'.jpg') != -1}">
						<c:set var="thumbLink" value="${fn:replace(post.properties['sling:thumbnail'],'https:','http:')}" />
						<c:set var="thumbType" value="image/jpeg" />
					</c:when>
					<c:when test="${fn:indexOf(post.properties['sling:thumbnail'],'.png') != -1}">
						<c:set var="thumbLink" value="${fn:replace(site.url,'https','http')}${fn:replace(post.properties['sling:thumbnail'],site.path,'')}" />
						<c:set var="thumbType" value="image/png" />
					</c:when>
					<c:otherwise>
						<c:set var="thumbLink" value="${fn:replace(site.url,'https','http')}${fn:replace(post.properties['sling:thumbnail'],site.path,'')}" />
						<c:set var="thumbType" value="image/jpeg" />
					</c:otherwise>
				</c:choose>
				<content:encoded>
					<![CDATA[
						<img src="${thumbLink}" title="${sling:encode(post.title,'XML_ATTR')}" />
						<sling:encode value="${post.properties.snippet}" mode="XML" />
					]]>
				</content:encoded>
				<c:if test="${not empty post.properties['sling:thumbnail']}">
					<enclosure length="0" type="${thumbType}" url="${sling:encode(thumbLink,'XML_ATTR')}" />
				</c:if>
				<fmt:parseDate value="${post.properties.publishDate}" var="publishDate" pattern="yyyy-MM-dd" />
				<pubDate><fmt:formatDate value="${publishDate}" pattern="EEE, dd MMM yyyy HH:mm:ss Z" /></pubDate>
				<link>${site.url}${post.publishedPath}</link>
				<guid isPermaLink="true">${site.url}${post.publishedPath}</guid>
			</item>
		</c:forEach>
	</channel>
</rss>