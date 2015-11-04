var gulp = require('gulp');

gulp.task('default', ['move-common-web', 'watch']);

gulp.task('watch', function() {
  gulp.watch('common/web/**', ['move-common-web']);
});

gulp.task('move-common-web', function() {
  gulp.src('common/web/**')
      .pipe(gulp.dest('desktop/osx/minobrlabs/minobrlabs/web/'));

  gulp.src('common/web/html/*.html')
      .pipe(gulp.dest('mobile/minobrlabs'));

  gulp.src('common/web/vendor/**')
      .pipe(gulp.dest('mobile/iOS/Resources/web/vendor'))
      .pipe(gulp.dest('mobile/Droid/Assets/web/vendor'));

  gulp.src('common/web/js/*.js')
      .pipe(gulp.dest('mobile/iOS/Resources/web/js'))
      .pipe(gulp.dest('mobile/Droid/Assets/web/js'));

  gulp.src('common/web/css/*.css')
      .pipe(gulp.dest('mobile/iOS/Resources/web/css'))
      .pipe(gulp.dest('mobile/Droid/Assets/web/css'));
});
