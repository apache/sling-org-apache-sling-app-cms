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

nomnom.decorate('.toggle-hidden' , {
    events : {
        click: function() {
            $($(this).data('target')).toggleClass('is-hidden');
        }
    }
});
    
nomnom.decorate('.toggle-value', {
    initCallback: function(){
        var source = this.getAttribute('data-toggle-source');
        var selector = 'input[name="'+ source +'"], select[name="'+ selector +'"]';
        var $tog = $(this);
        $(selector).change(function(){
            if($(this).val() !== $tog.data('toggle-value')){
                $tog.addClass('is-hidden');
            } else {
                $tog.removeClass('is-hidden');
            }
        });
    }
});