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
(function (nomnom, $) {
    'use strict';
    nomnom.decorate('.taxonomy', {
        events: {
            '.taxonomy__add, .taxonomy__add *': {
                click: function (event) {
                    event.preventDefault();
                    event.stopPropagation();
                    var $ctx = $(this),
                        $span = $('<span/>').html($ctx.find('.taxonomy__template').html()),
                        val = $ctx.find('.taxonomy__field input').val(),
                        found = false,
                        title = $ctx.find('option[value="' + val + '"]').text();
                    $ctx.find('.taxonomy__item input').each(function (idx, el) {
                        if ($(el).val() === val) {
                            found = true;
                        }
                    });
                    if (found) {
                        return false;
                    }
                    $span.find('input').val(val);

                    if (title !== '') {
                        $span.find('.taxonomy__title').text(title);
                        $('.taxonomy__container').append($span);
                        $ctx.find('.taxonomy__field input').val('');
                    }
                }
            }
        }
    });

    nomnom.decorate('.taxonomy__item, .taxonomy__item *', {
        events: {
            click: function () {
                $(this).remove();
                return false;
            }
        }
    });

}(window.nomnom = window.nomnom || {}, window.jQuery || {}));