/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
var gulp = require('gulp');
var sass = require('gulp-sass');
var header = require('gulp-header');
var cleanCSS = require('gulp-clean-css');
var concat = require('gulp-concat');
var rename = require('gulp-rename');
var terser = require('gulp-terser');
var streamqueue = require('streamqueue');
var log = require('fancy-log');

const apache2License = [
'/*',
' * Licensed to the Apache Software Foundation (ASF) under one or more',
' * contributor license agreements.  See the NOTICE file distributed with',
' * this work for additional information regarding copyright ownership.',
' * The ASF licenses this file to You under the Apache License, Version 2.0',
' * (the "License"); you may not use this file except in compliance with',
' * the License.  You may obtain a copy of the License at',
' *',
' *      http://www.apache.org/licenses/LICENSE-2.0',
' *',
' * Unless required by applicable law or agreed to in writing, software',
' * distributed under the License is distributed on an "AS IS" BASIS,',
' * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.',
' * See the License for the specific language governing permissions and',
' * limitations under the License.',
' */',
''
].join('\n');

let srcDir = function(sub) {
    return './src/main/frontend/' + sub;
};
let distDir = function(sub) {
    return './target/frontend/dist/' + sub;
}

gulp.task('styles', function() {
     return streamqueue ({objectMode: true},
            gulp.src(srcDir('scss/*.scss'))
                .pipe(header(apache2License))
                .pipe(sass().on('error', sass.logError))
                .pipe(cleanCSS()),
            gulp.src([
                './node_modules/jam-icons/css/jam.min.css',
                './node_modules/js-autocomplete/auto-complete.css'
            ])
         )
         .pipe(concat('styles.min.css'))
         .pipe(gulp.dest(distDir('jcr_root/static/clientlibs/sling-cms/css')))
         .pipe(rename('bundle.css'))
         .pipe(gulp.dest(distDir('jcr_root/content/starter/css')));
});

gulp.task('cms-assets', function() {
    return gulp.src([srcDir('img/*')])
        .pipe(gulp.dest(distDir('jcr_root/static/clientlibs/sling-cms/img')));
});

gulp.task('cms-fonts', function() {
    return gulp.src(['./node_modules/jam-icons/fonts/*',srcDir('fonts/*')])
        .pipe(gulp.dest(distDir('jcr_root/static/clientlibs/sling-cms/fonts')));
});

gulp.task('cms-js', function() {
    return streamqueue ({objectMode: true},
            gulp.src([
                './node_modules/rava/dist/rava.min.js',
                './node_modules/wysihtml/dist/minified/wysihtml.min.js',
                './node_modules/wysihtml/dist/minified/wysihtml.all-commands.min.js',
                './node_modules/wysihtml/dist/minified/wysihtml.table_editing.min.js',
                './node_modules/wysihtml/dist/minified/wysihtml.toolbar.min.js',
                './node_modules/handlebars/dist/handlebars.min.js',
                './node_modules/js-autocomplete/auto-complete.min.js'
            ]),
            gulp.src([
                './node_modules/sorttable/sorttable.js',
                './node_modules/wysihtml/parser_rules/advanced_and_extended.js'
            ])
            .pipe(terser()),
            gulp.src([
                srcDir('js/cms.js'),
                srcDir('js/cms.*.js')
            ])
            .pipe(terser())
            .pipe(concat('cms.js'))
            .pipe(header(apache2License))
         )
        .pipe(concat('scripts-all.min.js'))
        .pipe(gulp.dest(distDir('jcr_root/static/clientlibs/sling-cms/js')));
});

gulp.task('editor-fonts', function() {
    return gulp.src(['./node_modules/jam-icons/fonts/*','./src/fonts/*'])
        .pipe(gulp.dest(distDir('jcr_root/static/clientlibs/sling-cms-editor/fonts')));
});

gulp.task('editor-js', function() {
    return gulp.src([
            srcDir('js/editor.js')
        ])
        .pipe(terser())
        .pipe(header(apache2License))
        .pipe(concat('editor.min.js'))
        .pipe(gulp.dest(distDir('jcr_root/static/clientlibs/sling-cms-editor/js')));
});

gulp.task('editor-styles', function() {
     return streamqueue ({objectMode: true},
            gulp.src([srcDir('scss/editor.scss')])
                .pipe(sass().on('error', sass.logError))
                .pipe(cleanCSS())
                .pipe(header(apache2License)),
            gulp.src([
                './node_modules/jam-icons/css/jam.min.css'
            ])
         )
         .pipe(concat('editor.min.css'))
         .pipe(gulp.dest(distDir('jcr_root/static/clientlibs/sling-cms-editor/css')));
});

gulp.task('cms-styles', function() {
     return streamqueue ({objectMode: true},
            gulp.src(srcDir('scss/cms.scss'))
                .pipe(sass().on('error', sass.logError))
                .pipe(cleanCSS())
                .pipe(header(apache2License)),
            gulp.src([
                './node_modules/jam-icons/css/jam.min.css',
                './node_modules/js-autocomplete/auto-complete.css'
            ])
         )
         .pipe(concat('styles.min.css'))
         .pipe(gulp.dest(distDir('jcr_root/static/clientlibs/sling-cms/css')));
});

gulp.task('starter-assets', function() {
    return gulp.src(srcDir('img/*'))
        .pipe(gulp.dest(distDir('jcr_root/content/starter/img')));
});

gulp.task('starter-fonts', function() {
    return gulp.src([srcDir('fonts/*')])
        .pipe(gulp.dest(distDir('jcr_root/content/starter/fonts')));
});

gulp.task('starter-logo', function() {
    return gulp.src(srcDir('img/sling-logo.svg'))
        .pipe(gulp.dest(distDir('jcr_root/content/starter')));
});

gulp.task('starter-styles', function() {
    return gulp.src(srcDir('scss/starter.scss'))
        .pipe(sass().on('error', sass.logError))
        .pipe(cleanCSS())
        .pipe(header(apache2License))
        .pipe(rename('bundle.css'))
        .pipe(gulp.dest(distDir('jcr_root/content/starter/css')));
});

gulp.task('cms', gulp.series('cms-styles', 'cms-js', 'cms-assets', 'cms-fonts'));

gulp.task('editor', gulp.series('editor-styles', 'editor-js', 'editor-fonts'));

gulp.task('starter', gulp.series('starter-styles', 'starter-assets', 'starter-fonts', 'starter-logo'));

gulp.task('default', gulp.series('starter', 'cms', 'editor'));