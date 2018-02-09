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
const gulp        = require('gulp');
const sass        = require('gulp-sass');
const header      = require('gulp-header');
const cleanCSS   = require('gulp-clean-css');
var concatCss = require('gulp-concat-css');
var concat = require('gulp-concat');
var rename = require('gulp-rename');
var uglify = require('gulp-uglify');
let sourcemaps = require('gulp-sourcemaps');
var merge = require('merge-stream');
var order = require("gulp-order");

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

var scssStream = gulp.src('./src/scss/*.scss')
	.pipe(sass().on('error', sass.logError))
	.pipe(concat('scss-files.scss'))
    .pipe(sourcemaps.init())
    .pipe(cleanCSS())
    .pipe(header(apache2License));

var cssStream = gulp.src('./node_modules/summernote/dist/summernote-lite.css');

gulp.task('styles', function() {
	 var mergedStream = merge(cssStream, scssStream)
     	.pipe(concat('styles.min.css'))
        .pipe(gulp.dest('./dist/jcr_root/etc/clientlibs/sling-cms/css'))
	 	.pipe(rename('bundle.css'))
	 	.pipe(gulp.dest('./dist/jcr_root/content/starter/css'));
	 return mergedStream;
});

var vendorJSStream = gulp.src([
	'./node_modules/jquery/dist/jquery.min.js',
	'./node_modules/handlebars/dist/handlebars.min.js',
	'./node_modules/summernote/dist/summernote-lite.js']);

var jsStream = gulp.src([
		'./src/js/scripts.js'
	])
	.pipe(uglify())
    .pipe(header(apache2License));

gulp.task('js', function() {
	var mergedStream = merge(jsStream, vendorJSStream)
		.pipe(order([
			'node_modules/jquery/**/*.js',
			'node_modules/handlebars/**/*.js',
			'node_modules/summernote/**/*.js',
			'src/js/*.js',
		]))
		.pipe(concat('scripts.min.js'))
		.pipe(gulp.dest('./dist/jcr_root/etc/clientlibs/sling-cms/js'));
});

gulp.task('assets', function() {
	gulp.src('./src/{fonts,img}/**/*')
		.pipe(gulp.dest('./dist/jcr_root/etc/clientlibs/sling-cms'))
		.pipe(gulp.dest('./dist/jcr_root/content/starter'));
	gulp.src('./node_modules/summernote/dist/font/*')
		.pipe(gulp.dest('./dist/jcr_root/etc/clientlibs/sling-cms/css/font'));
});


gulp.task('default', ['styles', 'js', 'assets'], function() {});