@echo off

for %%f in (*.css) do (
	"%JAVA_HOME%\bin\javafxpackager" -createbss -srcfiles "%cd%\%%f" -outdir . -outfile %%~nf
	echo Compiled %%f to %%~nf.bss
)

echo.
echo Press any key to exit . . .
pause>nul