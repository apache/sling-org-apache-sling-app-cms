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
 */ --%><?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/libs/sling-cms/global.jsp"%>
<c:set var="site" value="${sling:adaptTo(resource,'org.apache.sling.cms.SiteManager').site}" />
<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
	<c:set var="query" value="SELECT * FROM [sling:Page] WHERE ISDESCENDANTNODE([${site.path}]) AND [jcr:content/published]=true AND ([jcr:content/hideInSitemap] IS NULL OR [jcr:content/hideInSitemap] <> true)" />
	<c:forEach var="pageRsrc" items="${sling:findResources(resourceResolver,query,'JCR-SQL2')}">
		<c:set var="page" value="${sling:adaptTo(pageRsrc,'org.apache.sling.cms.PageManager').page}" />
		<url>
			<loc>${site.url}${fn:replace(page.publishedPath,'index.html','')}</loc>
			<changefreq>monthly</changefreq>
		</url>
	</c:forEach>
	<url>
		<loc>${site.url}/</loc>
		<changefreq>always</changefreq>
	</url>
</urlset>
