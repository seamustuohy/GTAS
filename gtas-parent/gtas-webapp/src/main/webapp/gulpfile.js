// Include gulp
var gulp = require('gulp');

// Include Our Plugins
var jshint = require('gulp-jshint');
var sass = require('gulp-sass');
var concat = require('gulp-concat');
var uglify = require('gulp-uglify');
var rename = require('gulp-rename');

// Lint Task
gulp.task('lint', function () {
    return gulp.src('./*.js')
        .pipe(jshint())
        .pipe(jshint.reporter('default'));
});

// Compile Our Sass
gulp.task('sass', function () {
    return gulp.src('scss/*.scss')
        .pipe(sass())
        .pipe(gulp.dest('css'));
});

// Concatenate & Minify JS
gulp.task('scripts', function () {
    return gulp.src([
        'resources/bower_components/angular/angular.js',
        'resources/bower_components/angular-ui-router/release/angular-ui-router.js',
        'resources/bower_components/ui-router-extras/release/ct-ui-router-extras.js',
        'resources/bower_components/angular-bootstrap/ui-bootstrap.js',
        'resources/bower_components/angular-bootstrap/ui-bootstrap-tpls.js',
        'resources/bower_components/ng-table/dist/ng-table.js',
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
        'resources/js/query-builder.js',
        'app.js',
        'factory/GridFactory.js',
        'factory/ModalGridFactory.js',
        'factory/QueryBuilderFactory.js',
        'factory/jQueryBuilderFactory.js',
        'factory/jQueryBuilderFactory.js',
        'dashboard/DashboardController.js',
        'flights/FlightsIIController.js',
        'flights/FlightsService.js',
        'pax/PaxController.js',
        'pax/PaxDetailController.js',
        'pax/PaxService.js',
        'pax/PaxFactory.js',
        'query-builder/QueryBuilderController.js',
        'query-builder/QueryBuilderService.js',
        'query-builder/QueryService.js',
        'risk-criteria/RiskCriteriaController.js',
        'risk-criteria/RiskCriteriaService.js',
        'watchlists/WatchListService.js',
        'watchlists/WatchListController.js'])
        .pipe(concat('all.js'))
        .pipe(gulp.dest('dist'))
        .pipe(rename('all.min.js'))
        .pipe(uglify())
        .pipe(gulp.dest('dist'));
});

// Watch Files For Changes
gulp.task('watch', function() {
    gulp.watch('js/*.js', ['lint', 'scripts']);
    gulp.watch('scss/*.scss', ['sass']);
});

// Default Task
gulp.task('default', ['lint', 'sass', 'scripts', 'watch']);