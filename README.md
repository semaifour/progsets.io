# README #

 Progsets is declarative programming gateway to Apache Spark for querying and joining disparate datasets.  
 Progsets integrated with Apache Zeppelin via Remote Interpreter for visualization  

## Quick summary
Repository consists of three maven projects namely 
* progsets-common  -- common libraries
* progesets-spark  -- apache spark based input/output/transform procedures
* progsets-psql    -- spring boot based REST service to submit 'PSQL'/'PLPSQL' statements. 
* progsets-zeppelin -- Remote Zeppelin Interpreter for visualizations in zeppelin
## Version
*  0.0.1 - matsya the fish

### How do I get set up? ###

## Checkout the repository progsets-parent
*	$git clone https://github.com/semaifour/progsets.io.git
*	$git fetch && git checkout master 

## Dependencies
*	1. Eclipse IDE + Spring + Maven + Git
*	2. JDK 1.8	

## Build & Deployment instructions
*	1. Import all maven modules and build them independently (progsets-common, progsets-spark, progsets-zeppelin, progsets-psql )
*	2. Run 'progsets-psql-xxx.jar' as Spring Boot Application
	   Example: java -Dps.app=app -Dps.env=env -Dps.auth=admin:admin123 -jar procesets-psql-xxx.jar
*   3. Invoke REST Service at "http://localhost:8174/progsets/rest/psql/exe"

Here, 
	app-env-application.properties is the .properties file to be loaded
	"Basic admin:admin123" is the string to be used as the HTTP Authorization Header


## How to run & test?
*	$curl -XPOST  
		  -H "Authorization: Basic admin:admin123"
		  -H"Content-Type:text/psql" 
		  -d"myview = imongo?uri=mongodb://host:port&database=mymongogb&collection=user|return?view=myview&as=map" 
		  http://localhost:8175/procesets/rest/psql/exe
	
### Contribution guidelines ###

* Writing tests
* Code review
* Other guidelines

### Who do I talk to? ###

* Repo owner or admin
* Other community or team contact