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
var gulp        = require('gulp');
var sass        = require('gulp-sass');
var header      = require('gulp-header');
var cleanCSS   = require('gulp-clean-css');
var concat = require('gulp-concat');
var rename = require('gulp-rename');
var uglify = require('gulp-uglify');
var sourcemaps = require('gulp-sourcemaps');
var streamqueue = require('streamqueue');
var saveLicense = require('uglify-save-license');


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

gulp.task('styles', function() {
	 return streamqueue ({objectMode: true},
			gulp.src('./src/scss/*.scss')
				.pipe(sass().on('error', sass.logError))
				.pipe(concat('scss-files.scss'))
			    .pipe(sourcemaps.init())
			    .pipe(cleanCSS())
			    .pipe(header(apache2License)),
			gulp.src('./node_modules/summernote/dist/summernote-lite.css')
			    .pipe(cleanCSS()),
			gulp.src([
				'./node_modules/jam-icons/css/jam.min.css',
				'./node_modules/js-autocomplete/auto-complete.css'
			])
	 	)
     	.pipe(concat('styles.min.css'))
        .pipe(gulp.dest('./dist/jcr_root/static/clientlibs/sling-cms/css'))
	 	.pipe(rename('bundle.css'))
	 	.pipe(gulp.dest('./dist/jcr_root/content/starter/css'));
});

gulp.task('js', function() {
	return gulp.src([
			'./node_modules/jquery/dist/jquery.js',
			'./node_modules/handlebars/dist/handlebars.js',
			'./node_modules/summernote/dist/summernote-lite.js',
			'./node_modules/js-autocomplete/auto-complete.js',
			'./src/js/scripts.js'
			
		])
		.pipe(uglify({
            output: {
                comments: saveLicense
            }
        }))
 		.pipe(concat('scripts-all.min.js'))
	    .pipe(header(apache2License))
		.pipe(gulp.dest('./dist/jcr_root/static/clientlibs/sling-cms/js'));
});

gulp.task('assets', function() {
	gulp.src('./src/{fonts,img}/**/*')
		.pipe(gulp.dest('./dist/jcr_root/static/clientlibs/sling-cms'))
		.pipe(gulp.dest('./dist/jcr_root/content/starter'));
	gulp.src('./src/img/sling-logo.svg')
		.pipe(gulp.dest('./dist/jcr_root/content/starter'));
	gulp.src('./node_modules/summernote/dist/font/*')
		.pipe(gulp.dest('./dist/jcr_root/static/clientlibs/sling-cms/css/font'));
	gulp.src('./node_modules/jam-icons/fonts/*')
		.pipe(gulp.dest('./dist/jcr_root/static/clientlibs/sling-cms/fonts'));
});


gulp.task('default', ['styles', 'js', 'assets'], function() {});