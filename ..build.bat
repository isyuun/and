cd /d "%~dp0"
@echo OFF
REM [����������]
cmd /c .\TEST\.build.release.bat
REM [����׺���]
cmd /c .\TEST\.build.debug.bat
REM [����������]
cmd /c .\MAP\.build.release.bat
REM [����׺���]
cmd /c .\MAP\.build.debug.bat
REM [����������]
cmd /c .\Navi\.build.release.bat
REM [����׺���]
cmd /c .\Navi\.build.debug.bat
pause
