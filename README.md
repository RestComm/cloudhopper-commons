


[Try Restcomm Cloud NOW for FREE!](https://www.restcomm.com/sign-up/) Zero download and install required.


All Restcomm [docs](https://www.restcomm.com/docs/) and [downloads](https://www.restcomm.com/downloads/) are now available at [Restcomm.com](https://www.restcomm.com).




Cloudhopper by Twitter [![Build Status](https://secure.travis-ci.org/Restcomm/cloudhopper-commons.png?branch=master)](http://travis-ci.org/Restcomm/cloudhopper-commons)
[![FOSSA Status](https://app.fossa.io/api/projects/git%2Bhttps%3A%2F%2Fgithub.com%2FRestComm%2Fcloudhopper-commons.svg?type=shield)](https://app.fossa.io/projects/git%2Bhttps%3A%2F%2Fgithub.com%2FRestComm%2Fcloudhopper-commons?ref=badge_shield)
======================

Common Java libraries used by the Cloudhopper family of mobile messaging applications at Twitter.

cloudhopper-commons-charset
---------------------------
Utility Java classes for converting between charsets (mostly "mobile" in nature) such as Unicode to GSM and vice versa. This library attempts to contain the fastest and most efficient methods for converting charsets to/from byte arrays.

cloudhopper-commons-gsm
-----------------------
Utility Java classes for working with [GSM](http://en.wikipedia.org/wiki/GSM "GSM") mobile technologies (usually SMS or MMS).

cloudhopper-commons-io
----------------------
I/O utilities. Currently useful for file watching, reading and writing.

cloudhopper-commons-locale
--------------------------
Localization utilities for Cloudhopper projects. Country, country code and timezone utilities.

cloudhopper-commons-rfs
-----------------------
Cloudhopper Commons RFS supports copying and moving files to remote filesystems. Various protocols are supported such as FTP, SSL/TLS FTP, Secure FTP, and possibly more in the future.  Each is configured with a simple URL-based syntax. Authentication is supported for each protocol.

cloudhopper-commons-sql
-----------------------
Provides a consistent interface to configure, create, and manage (via JMX) various DataSource providers that may provide Connection pooling. Currently supports C3P0 and Proxool.

cloudhopper-commons-util
------------------------
Utility Java classes commonly used across all Cloudhopper-based projects.

cloudhopper-commons-xbean
-------------------------
The Xbean Java library is a set of utility classes for creating or configuring a Java object from XML. The library is a simple alternative to other XML-to-Java frameworks such as Spring. The library will only map in a single direction - XML -> Java. This limited scope helps keep this library small, fast, and very good at what it was mainly intended for -- application configuration files.

cloudhopper-httpclient-util
---------------------------
Apache Jakarta HttpClient 4.x utility library.

cloudhopper-jetty
-----------------
Wrapper around Jetty HTTP Server that makes configuration easy and possible from a simple few configuration objects.

cloudhopper-sxmp
----------------
Library implementing the SXMP protocol. SXMP is like SMPP, but as readable XML.

cloudhopper-commons-ssl
-----------------------

Utility Java classes for working with SSL in Cloudhopper-based projects. Originally part of cloudhopper-smpp.

