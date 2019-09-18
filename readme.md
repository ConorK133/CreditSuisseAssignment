Instructions to run:
1) Open Terminal in base folder
2) use gradle build, "./gradlew build"
3) Run the program using the following command:
	./gradlew run -PappArgs="['*insert filepath here relative to the base folder*']"
	e.g to use the logfile in the base folder you would use:
	./gradlew run -PappArgs="['./logfile.txt']"
4) To see the results, cd to ./hsqldb/lib
5) Open the DB Manager using " java -cp hsqldb.jar org.hsqldb.util.DatabaseManager"
6) In the URL section change it to "jdbc:hsqldb:file:mydb" 
7) Do a select on the Event table