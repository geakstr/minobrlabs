var gulp = require('gulp');

gulp.task('default', ['move-common-web', 'watch']);

gulp.task('watch', function() {
  gulp.watch('common/web/**', ['move-common-web']);
});

gulp.task('move-common-web', function() {
  gulp.src('common/web/**')
      .pipe(gulp.dest('desktop/osx/minobrlabs/minobrlabs/web/'))
      .pipe(gulp.dest('mobile/android/app/src/main/assets/'));
});
