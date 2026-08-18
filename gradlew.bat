@ECHO OFF
SET APP_HOME=%~dp0
java -jar "%APP_HOME%gradle\wrapper\gradle-wrapper.jar" %*
if %ERRORLEVEL% NEQ 0 exit /b %ERRORLEVEL%
