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
 <div class="taxonomy">
     <fieldset disabled="disabled" class="taxonomy__template is-hidden">
         <a class="button taxonomy__item">
            <input type="hidden" name="${properties.name}" value="" />
            <span class="taxonomy__title">
            </span>
            <span class="jam jam-close"></span>
        </a>
     </fieldset>
    <div class="taxonomy__field field has-addons">
        <div class="control is-expanded">
             <input class="input" type="text" ${required} ${disabled} list="taxonomy-${fn:replace(resource.name,':','-')}" autocomplete="off" />
         </div>
         <div class="control">
             <button class="taxonomy__add button">
                 <span class="jam jam-plus"></span>
             </button>
         </div>
     </div>
     <div class="taxonomy__container">
         <c:forEach var="item" items="${editProperties[properties.name]}">
             <a class="button taxonomy__item">
                 <input type="hidden" name="${properties.name}" value="${item}" />
                 <span class="taxonomy__title">
                     ${sling:encode(sling:getResource(resourceResolver,item).valueMap['jcr:title'],'HTML')}
                 </span>
                 <span class="jam jam-close"></span>
             </a>
         </c:forEach>
     </div>
     
    <sling:getCAConfigResource resource="${slingRequest.requestPathInfo.suffixResource}" bucket="site" name="settings" var="sitesettings" />
    <datalist id="taxonomy-${fn:replace(resource.name,':','-')}">
        <c:set var="query" value="SELECT * FROM [sling:Taxonomy] WHERE ISDESCENDANTNODE([${not empty properties.basePath ? properties.basePath : sitesettings.valueMap.taxonomyroot}])" />
        <c:forEach var="taxonomy" items="${sling:findResources(resourceResolver,query,'JCR-SQL2')}">
            <option value="${taxonomy.path}">${taxonomy.valueMap['jcr:title']}</option>
        </c:forEach>
    </datalist>
</div>