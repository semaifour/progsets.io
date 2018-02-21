# README #

 Procsets is distributed data processing platform for querying heterogeneous datasets.  

## Quick summary
Repository consists of three maven projects namely 
* procsets-common  -- common libraries
* procesets-spark  -- apache spark based input/output/transform procedures
* procsets-psql    -- spring boot based REST service to submit 'PSQL'/'PLPSQL' statements. 
## Version
*  0.0.1 - matsya the fish

### How do I get set up? ###

## Checkout the repository procsets-parent
*	$git clone http://bitbucket.org/...
*	$git fetch && git checkout master 

## Dependencies
*	1. Eclipse IDE + Spring + Maven + Git
*	2. JDK 1.8	

## Build & Deployment instructions
*	1. Import all maven modules and build them independently (procsets-common, procsets-spark, procsets-psql)
*	2. Run 'procesets-psql-xxx.jar' as Spring Boot Application
*	2. Example: java -jar procesets-psql-xxx.jar

## How to run & test?
*	$curl -XPOST  
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