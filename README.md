# PIM Service Alternate solution. 
Plain HTTP client connect with KIE-server and trigger migration API

This is a PIM client tool which calls directly KIE Server migration APIS to migrate process instances from one container to another version.
This tool is build with Apache HTTP client. It support both basic and "cert" based authentication.

PIM migration is based on plan configuration data configured in plan.json file which is 
placed in resource folder of this project. You can have more than one plan for the same container. 

Here are some example plan configuration.

#container level plan with single process id , which migrates all process instances . execution : M , multi thread other wise single thread 

[
	
	{
	"planId" : "101",
	"execution" :"M",
	"sourceContainer":"pimdemoprocess_1.0.0",
	"targetContainer":"pimdemoprocess_1.0.1",
	"processId":"pimdemoprocess.rhpimprocess"
	}
	
]
 
 #container level plan with multiple process ids which migrates all process instances .  
 
[
	
	{
	"planId" : "101",
	"sourceContainer":"pimdemoprocess_1.0.0",
	"targetContainer":"pimdemoprocess_1.0.1",
	"processId":"pimdemoprocess.rhpimprocess"
	},
	{
	"planId" : "103",
	"sourceContainer":"pimdemoprocess_1.0.0",
	"targetContainer":"pimdemoprocess_1.0.1",
	"processId":"pimdemoprocess.rhpimprocess1" 
	}
	
]

#container level plan with single process id with specific instance ids, 

[
	
	{
	"planId" : "101",
	"execution" :"M",
	"sourceContainer":"pimdemoprocess_1.0.0",
	"targetContainer":"pimdemoprocess_1.0.1",
	"processId":"pimdemoprocess.rhpimprocess"
	}
	
]

## Running
## prerequisite, you much have both MAVEN and JDK installed on your machine .

To get the application with cecrt authendication :
Navigate to project folder and run the following mvn commnads. 
```
$ mvn clean compile assembly:single # Build and assenble all dependency as a one bundle .
$ java -D<pathtokyestore fielname> -D<pathtokyestore password> -jar target/pimservice_client-1.0-jar-with-dependencies.jar
```

To get the application with basic authendication (loacal testing) :
```
$ mvn clean compile assembly:single # Build and assenble all dependency as a one bundle .
$ java -DAUTH="basic" -jar target/pimservice_client-1.0-jar-with-dependencies.jar 
