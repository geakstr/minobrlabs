var gulp = require('gulp');

gulp.task('default', ['move-common-web', 'watch']);

gulp.task('watch', function() {
  gulp.watch('common/web/*.*', ['move-common-web']);
});

gulp.task('move-common-web', function() {
  gulp.src('common/web/*.*').pipe(gulp.dest('desktop/osx/minobrlabs/minobrlabs/web/'));

  gulp.src('common/web/index.html').pipe(gulp.dest('mobile/minobrlabs'));
  gulp.src('common/web/*.js').pipe(gulp.dest('mobile/iOS/Resources/'));
  gulp.src('common/web/*.js').pipe(gulp.dest('mobile/Droid/Assets/'));
});
