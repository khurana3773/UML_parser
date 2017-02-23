# UML_parser
This application is designed to convert Java code into UML diagrams, with certain conditions mentioned below.

# Conditions
Default Package: All Java source files will be in the "default" package.  That is, there will be only one directory (i.e. no subdirectories)
Dependencies & Uses Relationships for Interfaces Only:  Do not include dependencies in output UML diagram except in the cases of "interfaces/uses"
Class Declarations with optional Extends and Implements: Make sure to include proper notation for inheritance and interface implementations.  
Only Include Public Methods (ignore private, package and protected scope)
Only Include Private and Public Attributes (ignore package and protected scope)
Java Setters and Getters:  Must Support also Java Style Public Attributes as "setters and getters"
Must Include Types for Attributes, Parameters and Return types on Methods
Classifier vs Attributes Compartment:  If there is a Java source file, then there should be a "UML Class" on the Diagram for it.  That is, if there is no Java source file for a class and the class is part of an instance variable, put the class/property in the attribute compartment
Interfaces - Implements and Uses Notation:  Show Interfaces along with Clients of Interfaces (as dependencies). 
