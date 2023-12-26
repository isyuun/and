cd /d "%~dp0"
@echo OFF
REM [¸±¸®½ººôµå]
cmd /c .\TEST\.build.release.bat
REM [µð¹ö±×ºôµå]
cmd /c .\TEST\.build.debug.bat
REM [¸±¸®½ººôµå]
cmd /c .\MAP\.build.release.bat
REM [µð¹ö±×ºôµå]
cmd /c .\MAP\.build.debug.bat
REM [¸±¸®½ººôµå]
cmd /c .\Navi\.build.release.bat
REM [µð¹ö±×ºôµå]
cmd /c .\Navi\.build.debug.bat
pause
