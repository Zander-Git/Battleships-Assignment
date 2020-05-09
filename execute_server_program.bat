:: Updated bat files to resolve typing issues in the command line

:: Compile the client program
::call ant -f build_simple_client.xml compile jar

:: compile the server program
call ant -f build_simple_server.xml compile jar

:: Run the server program, pass port as an argument.
java -jar build_server/jar/MyServerUI.jar

pause;