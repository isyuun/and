cd /d "%~dp0"
@echo OFF
call .build.title.bat
title %TITLE%%~n0
REM [ant����]
REM cmd /c ant debug
REM copy /Y ".\bin\*-debug.apk" .\
REM [gradle����]
@echo ON
cmd /c ..\gradlew --stacktrace -x test :%TITLE%:assembleDebug %1
copy /Y ".\build\outputs\apk\debug\*-debug.apk" "..\.APK\debug\"
cmd /c .beep.bat
REM pause
