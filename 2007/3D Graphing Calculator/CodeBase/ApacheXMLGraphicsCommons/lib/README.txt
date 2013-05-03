
Information on Apache XML Graphics Commons dependencies
==========================================================

$Id$

The Apache Licenses can also be found here:
http://www.apache.org/licenses/


Normal Dependencies
----------------------

- Apache Jakarta Commons IO

    commons-io-*.jar
    http://jakarta.apache.org/commons/io/
    (I/O routines)
    
    Apache License v2.0



Special Dependencies
-----------------------

The special dependencies are the whole JAXP set which required for users
on JDK 1.3.x which doesn't include JAXP. But it is also interesting for
users on JDKs >= 1.4 if they want to replace the default JAXP 
implementation delivered by the JDK.

Replacing the default implementations involves understanding the 
"Endorsed Standards Override Mechanism".
More information can be found here:
http://java.sun.com/j2se/1.4.2/docs/guide/standards/index.html

- JAXP API

    xml-apis.jar
    http://xml.apache.org/commons/components/external/
    (the JAXP API, plus SAX and various W3C DOM Java bindings,
    maintained in XML Commons Externals)
    
    Apache License v2.0 (applies to the distribution)
    SAX is in the public domain
        http://www.saxproject.org/copying.html
    W3C Software Notice and License (applies to the various DOM Java bindings)
    W3C Document License (applies to the DOM documentation)
        http://www.w3.org/Consortium/Legal/copyright-software
        http://www.w3.org/Consortium/Legal/copyright-documents
        http://www.w3.org/Consortium/Legal/

- Apache Xerces-J

    xercesImpl-*.jar
    http://xerces.apache.org
    (JAXP-compliant XML parser and DOM Level 3 implementation)
    
    Apache License v2.0

- Apache Xalan-J

    xalan-*.jar and serializer-*.jar
    http://xalan.apache.org
    (JAXP-compliant XSLT and XPath implementation)
    
    Apache License v2.0 (applies to Xalan-J)
    Apache License v1.1 (applies to Apache BCEL and Apache REGEXP bundled in the JAR)
    Historical Permission Notice and Disclaimer (applies to CUP Parser Generator)
        http://www.opensource.org/licenses/historical.php
        (see xalan.runtime.LICENSE.txt)






Additional development-time dependencies
-------------------------------------------

- Apache Ant

    (not bundled, requires pre-installation)
    http://ant.apache.org
    (XML-based build system
    
    Apache License V2.0

- JUnit

    (not bundled, provided by Apache Ant or your IDE)
    http://www.junit.org
    Common Public License V1.0