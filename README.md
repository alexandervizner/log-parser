# Timing log parser

![alt text](src/main/resources/img/magnifying-glass.png "Icon")


The parser is designed for parsing timing logs from web services(based on Struts 1/2).
The target of current implementation wasn't performance but rather flexibility and readability of the solution.
Indeed the measurements shows that timing log gathered within 1 day (24 hours) can be processed in about 60 - 80 sec.

## Requirements, dependencies and compilation of the project 

Notice that Java 1.8 and Maven package manager installed are required. The project was build up with no 3rd party specific libraries excepting,
maven shade plugin that placed into /3rdparty folder of the project. For compilation of the project you need to 
install latest version of [Apache Maven](https://maven.apache.org/download.cgi) (available in /3rdparties too). 
Aftre having all requirements installed navigate to root directory of the project and run the following commands from console:

```shell
mvn clean package
```

This command will create target folder that contains jar package.

## Usage

For launching the utility use the following command:

```shell
java -jar timing-report.jar <options> <path to log file> <number of resources to show>
where options:

-h help message
-d debug mode
<path to log file> full or relative path to log file
<number of resources to show> number of resources to display

```

## Config

Application can be configured using application.properties file. The following settings are configured by default:

```
app.top.max=500
app.keywords=contentId,msisdn,get,update
app.timeout=3000
app.datetimeformat=yyyy-MM-dd'T'hh:mm:ss,SSS
app.debug=false
```

## Implementation note
The main idea of implementation was simple and flexible solution. You need to implement matcher interface with logic inside and pass it to parser.
As result the parser object has collected statistics data into collection. I believe it can be (and should be imho) replaced by DAL to some database.
Ideally implemented reports should be moved into separate web service that consumes data from the database and shares reports through REST API. 

## Testing note
Please notice that the current tests are not covered all the functionality and cases. Main purpose is just check some corner cases.
Sure, normally such project should be covered by UT properly.

## Report

As result of calculations the utility will be depict screen below:

![report](src/main/resources/img/screen.png)  