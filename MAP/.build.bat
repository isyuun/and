cd /d "%~dp0"
@echo OFF
rd build /s /q
REM [Ŭ���׺���]
REM cmd /c .build.clean.bat
REM [����������]
REM start /d ".\" .build.release.bat
cmd /c .build.release.bat
REM [Ŭ���׺���]
REM cmd /c .build.clean.bat
REM [����׺���]
REM start /d ".\" .build.debug.bat
cmd /c .build.debug.bat
REM [Ŭ���׺���]
REM cmd /c .build.clean.bat
REM pause
