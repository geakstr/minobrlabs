var gulp = require('gulp');

gulp.task('default', ['move-common-web', 'watch']);

gulp.task('watch', function() {
  gulp.watch('common/web/**', ['move-common-web']);
});

gulp.task('move-common-web', function() {
  gulp.src('common/web/**')
      .pipe(gulp.dest('desktop/osx/minobrlabs/minobrlabs/web/'));

  gulp.src('common/web/*.html')
      .pipe(gulp.dest('mobile/minobrlabs'));

  gulp.src('common/web/vendor/**')
      .pipe(gulp.dest('mobile/iOS/Resources/vendor'))
      .pipe(gulp.dest('mobile/Droid/Assets/vendor'));

  gulp.src('common/web/js/*.js')
      .pipe(gulp.dest('mobile/iOS/Resources/js'))
      .pipe(gulp.dest('mobile/Droid/Assets/js'));

  gulp.src('common/web/css/*.css')
      .pipe(gulp.dest('mobile/iOS/Resources/css'))
      .pipe(gulp.dest('mobile/Droid/Assets/css'));
});
