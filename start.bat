@echo off
cd /d "%~dp0"
where py >nul 2>nul
if errorlevel 1 goto python
py -3 pet.py
if errorlevel 1 pause
exit /b
:python
where python >nul 2>nul
if errorlevel 1 goto missing
python pet.py
if errorlevel 1 pause
exit /b
:missing
echo Please install Python 3.10+ from python.org with Tcl/Tk and PATH enabled.
pause
