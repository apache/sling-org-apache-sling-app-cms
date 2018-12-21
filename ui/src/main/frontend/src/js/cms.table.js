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
(function (rava, $) {
    'use strict';
    rava.bind(".table", {
        callbacks : {
            created: function () {
                var table = this,
                    sort = this.dataset.sort !== 'false',
                    paginate = this.dataset.paginate !== 'false';
                $(this).DataTable({
                    sort: sort,
                    paginate: paginate
                }).on('page.dt', function () {
                    rava.findAll('tr.is-selected').forEach(function (tr) {
                        tr.classList.remove('is-selected');
                    });
                    document.querySelector('.actions-target').innerHTML = '';
                });
            }
        }
    });

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
}(window.rava = window.rava || {}, window.jQuery = window.jQuery || {}));