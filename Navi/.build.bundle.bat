cd /d "%~dp0"
@echo OFF
call .build.title.bat
title %TITLE%%~n0
REM [antºôµå]
REM cmd /c ant debug
REM copy /Y ".\bin\*-debug.apk" .\
REM [gradleºôµå]
@echo ON
cmd /c ..\gradlew --stacktrace -x test :%TITLE%:bundleRelease %1
copy /Y ".\build\outputs\bundle\release\*-release.aab" "..\.APK\release\"
cmd /c ..\gradlew --stacktrace -x test :%TITLE%:bundleDebug %1
copy /Y ".\build\outputs\bundle\debug\*-debug.aab" "..\.APK\debug\"
cmd /c .beep.bat
REM pause
