cd /d "%~dp0"
@echo OFF
REM ������������
set mydir="%~dp0"
SET mydir=%mydir:\=;%

for /F "tokens=* delims=;" %%i IN (%mydir%) DO call :FOLDER %%i
goto :BUILD

:FOLDER
if "%1"=="" (
    REM @echo %TITLE%
    goto :EOF
)

set TITLE=%1
SHIFT

goto :FOLDER

:BUILD
title %TITLE%
@echo [������Ʈ��Ȯ��]
@echo %TITLE%
REM @echo ON
REM pause
