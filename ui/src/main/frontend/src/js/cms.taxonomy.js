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
nomnom.decorate('.taxonomy', class {
    "click::.taxonomy__add"(event){
        event.preventDefault();
        event.stopPropagation();
        var $ctx = $(this);
        var $span = $('<span/>').html($ctx.find('.taxonomy__template').html());
        var val = $ctx.find('.taxonomy__field input').val();
        var found = false;
        $ctx.find('.taxonomy__item input').each(function(idx, el){
            if($(el).val() === val){
                found = true;
            }
        });
        if(found){
            return false;
        }
        $span.find('input').val(val);
        var title = $ctx.find('option[value="'+val+'"]').text();

        if(title !== ''){
            $span.find('.taxonomy__title').text(title);
            $('.taxonomy__container').append($span);
            $ctx.find('.taxonomy__field input').val('');
        }
        return false;
    }
});

nomnom.decorate('.taxonomy__item', class {
    "click::"(){
        $(this).remove();
        return false;
    }
});