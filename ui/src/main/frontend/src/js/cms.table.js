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

/* eslint-env browser, es6 */
(function (rava) {
    'use strict';
    
    var urlParams = new URLSearchParams(window.location.search),
        resourceParam = urlParams.get('resource'),
        searchParam = urlParams.get('search');
    
    rava.bind(".table tbody tr", {
        events: {
            click: function () {
                this.closest('.table').querySelectorAll('tr.is-selected').forEach(function (tr) {
                    tr.classList.remove('is-selected');
                });
                this.classList.add('is-selected');
                document.querySelector('.actions-target').innerHTML = this.querySelector('.cell-actions').innerHTML;
            }
        }
    });
    
    rava.bind(".table", {
        callbacks : {
            created: function () {
                var table = this;
                if (table.closest('.table__wrapper')) {
                    var search = table.closest('.table__wrapper').querySelector('input[name=search]'),
                        filter = function (event) {
                            event.stopPropagation();
                            event.preventDefault();
                            var value = this.value.toLowerCase();
                            table.querySelectorAll('tbody tr').forEach(function (row) {
                                if (row.innerText.toLowerCase().indexOf(value) === -1 && !row.querySelector('td[data-value="' + resourceParam + '"]')) {
                                    row.classList.add('is-hidden');
                                } else {
                                    row.classList.remove('is-hidden');
                                }
                            });
                        };
                    search.addEventListener('keyup', filter);
                    search.addEventListener('change', filter);
                    
                    if (resourceParam) {
                        table.querySelectorAll('tbody tr').forEach(function (row) {
                            if (row.querySelector('td[data-value="' + resourceParam + '"]')) {
                                row.classList.remove('is-hidden');
                                row.click();
                            } else {
                                row.classList.add('is-hidden');
                            }
                        });
                        table.closest('.table__wrapper').querySelector('input[name=search]').value = resourceParam;
                    } else if (searchParam) {
                        table.closest('.table__wrapper').querySelector('input[name=search]').value = searchParam;
                        filter(new Event('fake'));
                    }
                }
            }
        }
    });

}(window.rava = window.rava || {}));