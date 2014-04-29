/*jslint node:true*/

(function() {

    'use strict';

    var gulp = require('gulp'),
        sass = require('gulp-sass'),
        markdown = require('gulp-markdown'),
        cssmin = require('gulp-cssmin'),
        clean = require('gulp-clean'),
        jshint = require('gulp-jshint'),
        concat = require('gulp-concat'),
        uglify = require('gulp-uglify'),
        filesize = require('gulp-filesize'),
        gccompiler = require('gulp-closure-compiler'),

        stacks = {
            app: {
                css: ['fonts.css',
                    'normalize.css',
                    'font-awesome.min.css',
                    'anim.css',
                    'main.css',
                    'basic-form.css',
                    'table.css',
                    'app.css'],

                js: ['vendor/*.js',
                    'core/home.js']
            },
            landing: {
                css: ['fonts.css',
                    'normalize.css',
                    'font-awesome.min.css',
                    'anim.css',
                    'main.css',
                    'basic-form.css',
                    'landing.css'],

                js: ['vendor/*.js',
                    'core/landing.js']
            }
        },

        getStackFiles = function(stack, type) {
            if (!stacks[stack]) {
                new Error('Stack ' + stack + ' does not exists.');
            }
            if (!stacks[stack][type]) {
                new Error('Stack ' + stack + ' type ' +
                         type + ' does not exists.');
            }
            var arr = [], i, root;

            if (type === 'css') {
                root = 'src/static/css/';
            } else if (type === 'js') {
                root = 'src/static/js/';
            }

            for (i = 0; i < stacks[stack][type].length; i++) {
                arr.push(root + stacks[stack][type][i]);
            }

            return arr;
        };




    gulp.task('lint', function() {
        return gulp.src('src/static/js/core/*.js')
            .pipe(jshint())
            .pipe(jshint.reporter('default'))
            .pipe(gccompiler({
              compilerPath: '../lib/gclosure-compiler.jar',
              fileName: 'src/static/js/core/*.js'
            }));
    });

    gulp.task('clean', function() {
        return gulp.src('static')
            .pipe(clean());
    });

    gulp.task('sass', function() {
        return gulp.src('src/static/scss/*.scss')
            .pipe(sass())
            .pipe(gulp.dest('src/static/css'));
    });

    gulp.task('build-css', ['sass'], function() {
        for (var k in stacks) {
            gulp.src(getStackFiles(k, 'css'))
                .pipe(cssmin())
                .pipe(concat(k + '.css'))
                .pipe(gulp.dest('static/css/'))
                .pipe(filesize());
        }

        return true;
    });

    gulp.task('blog', function() {
        gulp.src('src/blog/*')
            .pipe(markdown())
            .pipe(gulp.dest('src/views/blog'));
    });

    gulp.task('build-js', function() {
        for (var k in stacks) {
            gulp.src(getStackFiles(k, 'js'))
                .pipe(concat(k + '.js'))
                .pipe(gulp.dest('static/js/'))
                .pipe(uglify())
                .pipe(gulp.dest('static/js/'))
                .pipe(filesize());
        }

        return true;
    });

    gulp.task('fonts', function() {
        return gulp.src([
            'src/static/fonts/*'
        ])
            .pipe(gulp.dest('static/fonts/'));
    });

    gulp.task('img', function() {
        return gulp.src([
            'src/static/img/*'
        ])
            .pipe(gulp.dest('static/img/'));
    });

    gulp.task('private', function() {
        return gulp.src([
            'private/*',
            'private/**/*.*'
        ])
            .pipe(gulp.dest('static/private/'));
    });

    gulp.task('copy', ['img', 'fonts', 'private'], function() {
        return gulp.src([
            'src/static/favicon.ico',
            'src/static/robots.txt',
            'src/static/apple-touch-icon.png'
        ])
            .pipe(gulp.dest('static/'));
    });

    gulp.task('default', ['blog', 'sass',
        'build-css', 'build-js', 'copy']);

    gulp.task('watch', function() {
        gulp.watch('src/static/js/core/*.js', ['build-js']);
        gulp.watch('src/static/js/vendor/*.js', ['build-js']);
        gulp.watch(['src/static/css/*.css', 'src/static/scss/*.scss'],
                    ['build-css']);
    });

}());
