var gulp = require('gulp'),
    concat = require('gulp-concat'),
    jshint = require('gulp-jshint'),
    iife = require('gulp-iife'),
    minifyCSS = require('gulp-minify-css'),
    rename = require('gulp-rename'),
    uglify = require('gulp-uglify');

var images = [
    'resources/img/404.png',
    'resources/img/circle_arrow_back.png',
    'resources/img/gtas_logo-compressed.jpg',
];

var cssFiles = [
    'resources/css/style-icomoon.css',
    'resources/bower_components/bootstrap/dist/css/bootstrap.css',
    'resources/css/gtas.css',
    'resources/bower_components/bootstrap-select/dist/css/bootstrap-select.min.css',
    'resources/bower_components/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css',
    'resources/bower_components/seiyria-bootstrap-slider/dist/css/bootstrap-slider.min.css',
    'resources/bower_components/selectize/dist/css/selectize.bootstrap3.css',
    'resources/bower_components/bootstrap-datepicker/dist/css/bootstrap-datepicker.min.css',
    'resources/bower_components/angular-ui-grid/dist/css/ui-grid.css',
    'resources/css/query-builder.default.css',
    'resources/bower_components/angular-material/angular-material.min.css'
];

var fontFiles = [
    'resources/fonts/icomoon.woff',
    'resources/fonts/icomoon.ttf',
    'resources/fonts/icomoon.svg',
    'resources/fonts/icomoon.eot'
];

var jsFiles = [
    'resources/bower_components/angular/angular.js',
    'resources/bower_components/angular-ui-router/release/angular-ui-router.js',
    'resources/bower_components/ui-router-extras/release/ct-ui-router-extras.js',
    'resources/bower_components/spring-security-csrf-token-interceptor/dist/spring-security-csrf-token-interceptor.min.js',
    'resources/bower_components/moment/min/moment.min.js',
    'resources/bower_components/jquery/dist/jquery.js',
    'resources/bower_components/bootstrap/dist/js/bootstrap.min.js',
    'resources/bower_components/bootstrap-select/dist/js/bootstrap-select.min.js',
    'resources/bower_components/bootbox/bootbox.js',
    'resources/bower_components/seiyria-bootstrap-slider/dist/bootstrap-slider.min.js',
    'resources/bower_components/selectize/dist/js/standalone/selectize.min.js',
    'resources/bower_components/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js',
    'resources/bower_components/jquery-extendext/jQuery.extendext.min.js',
    'resources/bower_components/pdfmake/build/pdfmake.min.js',
    'resources/bower_components/pdfmake/build/vfs_fonts.js',
    'resources/bower_components/angular-ui-grid/ui-grid.js',
    'resources/bower_components/angular-material/angular-material.min.js',
    'resources/bower_components/angular-aria/angular-aria.min.js',
    'resources/bower_components/angular-animate/angular-animate.min.js',
    'resources/bower_components/angular-messages/angular-messages.min.js',
    'resources/js/query-builder.js',
    'app.js',
    'resources/js/JqueryQueryBuilderService.js',
    'factory/GridFactory.js',
    'factory/ModalGridFactory.js',
    'factory/QueryBuilderFactory.js',
    'factory/JqueryBuilderWidget.js',
    'dashboard/DashboardController.js',
    'flights/FlightsService.js',
    'pax/PaxController.js',
    'pax/PaxDetailController.js',
    'pax/PaxMainController.js',
    'pax/PaxService.js',
    'pax/PaxFactory.js',
    'query-builder/QueryService.js',
    'risk-criteria/RiskCriteriaController.js',
    'watchlists/WatchListService.js',
    'watchlists/WatchListController.js',
    'admin/AdminController.js',
    'admin/UserController.js',
    'admin/UserService.js'
];

//will concat and minify CSS
gulp.task('minify-css', function () {
    'use strict';
    return gulp.src(cssFiles)
        .pipe(concat('style.css'))
        .pipe(gulp.dest('dist/css'))
        .pipe(minifyCSS())
        .pipe(rename('style.min.css'))
        .pipe(gulp.dest('dist/css'));
});

gulp.task('pub', function () {
    'use strict';
    return gulp.src(cssFiles)
        .pipe(concat('style.css'))
        .pipe(gulp.dest('dist/css'))
        .pipe(minifyCSS())
        .pipe(rename('style.min.css'))
        .pipe(gulp.dest('dist/css'));
});

// Lint Task
gulp.task('lint', function () {
    'use strict';
    return gulp.src('./query-builder/QueryBuilderController.js')
        .pipe(jshint())
        .pipe(jshint.reporter('gulp-jshint-html-reporter', {
            filename: __dirname + '/jshint-output.html',
            createMissingFolders : false
        }));
});

// Concatenate & Minify JS
gulp.task('scripts', function () {
    'use strict';
    return gulp.src(jsFiles)
        .pipe(iife())
        .pipe(concat('all.js'))
        .pipe(gulp.dest('dist/js'))
        .pipe(rename('all.min.js'))
        .pipe(uglify())
        .pipe(gulp.dest('dist/js'));
});

gulp.task('deployJS', function () {
    'use strict';
    return gulp.src(jsFiles)
        .pipe(gulp.dest('dist/js'));
});

gulp.task('deployFonts', function () {
    'use strict';
    return gulp.src(fontFiles)
        .pipe(gulp.dest('dist/fonts'));
});

gulp.task('deployCSS', function () {
    'use strict';
    return gulp.src(cssFiles)
        .pipe(gulp.dest('dist/css'));
});

gulp.task('deployImages', function () {
    'use strict';
    return gulp.src(images)
        .pipe(gulp.dest('dist/img'));
});

gulp.task('deploy', ['deployImages', 'deployCSS', 'deployJS', 'deployFonts']);

// Watch Files For Changes
gulp.task('watch', function () {
    'use strict';
    gulp.watch('js/*.js', ['lint', 'scripts']);
});


// Default Task
//gulp.task('default', ['lint', 'scripts', 'watch']);
gulp.task('default', ['scripts', 'minify-css']);
