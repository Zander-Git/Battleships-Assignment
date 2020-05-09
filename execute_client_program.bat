:: Updated bat files to resolve typing issues in the command line

:: Compile the client program
call ant -f build_simple_client.xml compile jar


:: run the client program.
:: pass IP address and port as arguments.
java -jar build_client/jar/MyClientUI.jar
pause;