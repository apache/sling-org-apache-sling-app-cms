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
<body>
	<sling:call script="/libs/sling-cms/components/editor/scripts/init.jsp" />
	<div class="title">
		<div class="logo">
			<a href="/">
				<img border="0" alt="Apache Sling" src="/static/clientlibs/reference/img/sling.svg"/>
			</a>
		</div>
		<div class="header">
			<a href="http://www.apache.org">
				<img border="0" alt="Apache" src="/static/clientlibs/reference/img/apache.png"/>
			</a>
		</div>
	</div>
	<div class="menu">
		<sling:include path="/content/apache/sling-apache-org/index/jcr:content/menu" resourceType="sling-cms/components/general/container" />
		<a href="http://apache.org/foundation/contributing.html">
            <img border="0" alt="Support the Apache Software Foundation!" src="/static/clientlibs/reference/img/SupportApache-small.png" width="115px">
        </a>
	</div>
	<div class="main">
		<div class="pagenav">
			<sling:include path="breadcrumb" resourceType="reference/components/general/breadcrumb" />               
			<div class="tags">
				<sling:include path="tags" resourceType="reference/components/general/tags" />
			</div>                
		</div>
		<h1 class="pagetitle">
			${sling:encode(resource.valueMap['jcr:title'],'HTML')}
		</h1>
		<div class="row">
			<sling:call script="content.jsp" />
		</div>
		<div class="footer">
			<div class="revisionInfo">
				Last modified by <span class="author">${sling:encode(resource.valueMap['jcr:lastModifiedBy'],'HTML')}</span>
				on <span class="comment"><fmt:formatDate type="both" dateStyle="long" timeStyle="long" value = "${resource.valueMap['jcr:lastModified'].time}" /></span>
			</div>
			<p>
				Apache Sling, Sling, Apache, the Apache feather logo, and the Apache Sling project logo are trademarks of The Apache Software Foundation. All other marks mentioned may be trademarks or registered trademarks of their respective owners.
			</p>
			<p>
				<jsp:useBean id="date" class="java.util.Date" />
				Copyright © 2011-<fmt:formatDate value="${date}" pattern="yyyy" /> The Apache Software Foundation.
			</p>
		</div>
	</div>
	<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
	<sling:call script="/libs/sling-cms/components/editor/scripts/finalize.jsp" />
</body>