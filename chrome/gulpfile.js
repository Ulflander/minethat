/*jslint node:true*/

(function() {

    'use strict';

    var gulp = require('gulp'),
        clean = require('gulp-clean'),
        jshint = require('gulp-jshint'),
        concat = require('gulp-concat'),
        uglify = require('gulp-uglify'),
        filesize = require('gulp-filesize'),
        gccompiler = require('gulp-closure-compiler');




    gulp.task('lint', function() {
        return gulp.src([
                'js/*.js'
            ])
            .pipe(jshint())
            .pipe(jshint.reporter('default'));
    });

    gulp.task('gcc', function() {
        gulp.src([
                'js/*.js'
            ])
            .pipe(gccompiler({
              compilerPath: '../lib/gclosure-compiler.jar',
              fileName: 'js/*.js'
            }));
    });
    gulp.task('default', ['lint', 'gcc']);
    gulp.task('build', ['default']);

    gulp.task('watch', ['build'], function() {
        gulp.watch('js/*.js', ['lint', 'gcc']);
    });

}());
