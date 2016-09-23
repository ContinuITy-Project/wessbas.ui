
INTRODUCTION
------------

This projects transforms logfiles to WESSBAS-DSL instances, JMeter Load Test Scripts,
and PCM Usage Models. The transformation is configured using a UI. 

Input: logfiles, i.e. HTTP weblogs, kieker logs,...

Output: WESSBAS-DSL, JMeter Load Test Scripts, PCM Usage Models 

  
SYSTEM REQUIREMENTS
-------------------

The project has been developed with the use of the following tools:

  - JDK 1.7
  - Eclipse Modeling Tools  
  - Netbeans 
  - WESSBAS Projects: 
      * wessbas.dsl
      * wessbas.behaviorModelExtractor
      * wessbas.dslModelGenerator
      * wessbas.testPlanGenerator*
      * wessbas.commons

WESSBAS Log Format
------------------

With protocol information:
Session id;"use case 1":start timestamp:end timestamp:url:port:host ip:protocol:method:query string:encoding;...;"use case n":start timestamp:end timestamp:url:port:host ip:protocol:method:query string:encoding

or without protocol information:

Session id;"use case 1":start timestamp:end timestamp;...;"use case n":start timestamp:end timestamp

A mixture is not support currently.

The part :url:port:host ip:protocol:method:query string:encoding are protocol specific information and are required for
generation of executable testcases. These information are optional. 

Example: 

sh0Qe7VkIWailcOJCrWQmaIs.undefined;"login":1434390882048832140:1434390882067651830:/specj-web/app:8080:
192.168.1.1:HTTP/1.1:GET:uid=463633&submit=Log+in&action=login:<no-encoding>;

TODO:
---------------

- order files in folder, i.e. folder for the configuration files, result files
- force to use different pm output folder than input folder
- add console output to ui
