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
nomnom.decorate(".table .load-versions", {
   
    initCallback: function() {
        var $ctr = $(this);
        var $table = $ctr.closest('.table');
        $.getJSON($ctr.data('url'),function(res){
            $table.dataTable().api().destroy();
            var source   = $('#'+$ctr.data('template')).html();
            var template = Handlebars.compile(source);
            $ctr.append(template(res));
        });
    }
    
});




nomnom.decorate(".table", {
    initCallback: function(){
        var $table = $(this);
        var sort = $table.data('sort') !== 'false';
        var paginate = $table.data('paginate') !== 'false';
        $table.DataTable({
            sort: sort,
            paginate: paginate
        });
    }
});

nomnom.decorate("table tbody tr",{
    events :{
        click: function(event){
            var $current = $(this);
            $('.actions-target > *').appendTo('tr.is-selected .cell-actions');
            $current.parent().find('tr').removeClass('is-selected');
            $current.addClass('is-selected');
            $current.find('.cell-actions > *').appendTo('.actions-target')
        }
    }
});