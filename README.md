Flat Code
=========

Flat Code is a Java library designed to work easily with Google Guice,
providing wrappers, data structures, and algorithms to make several
complicated tasks easier.

Building and Installing
-----------------------

### From GitHub

__flatcode__ uses at least JDK 11 and Maven, and this is not meant
to be an exhaustive tutorial on how to use it.  However, following
these instructions will install __flatcode__ into your local
repository using the version number defined in the _pom.xml_.

    $ mvn install
    # or, without tests
    $ mvn install -DskipTests

Using
-----

Once __flatcode__ has been installed into your local repository, you
may use it by defining a dependency on it in your project's _pom.xml_.
Please refer to documentation for Maven for the latest information on
how to do this.  However, if you are using Maven in your project,
very briefly, as of the time of this writing, adding a dependency to
your code for this project would look similar to this (change _version_
to whatever valid version you like):

    <dependencies>
      <dependency>
        <groupId>org.flatcode</groupId>
        <artifactId>flatcode</artifactId>
        <version>1.0-SNAPSHOT</version>
      </dependency>
    </dependencies>
