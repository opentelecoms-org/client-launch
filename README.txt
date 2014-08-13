
Introduction
------------

This project provides a library JAR that can be used by any
application for launching real-time communication (RTC)
sessions.

Typical examples are launching a phone call or chat session.

The library attempts to launch the session in the following ways:

 * trying to executed a registered URI protocol handler, if one exists
 * trying to find a locally installed softphone such as Jitsi
 * trying to launch Jitsi using WebStart
 * trying to launch other softphones (see below to add support
   for additional softphones)

Dependencies
------------

 * The slf4j-api JAR is required at build and runtime.
 * An slf4j logging implementation should be present in your application,
   for example, if you use log4j already, you may want to add slf4j-log4j12
   to your runtime classpath.

Using it
--------

If you are using Maven, you can obtain the JAR by adding the following
declaration to the dependencies section of your pom.xml:

  <dependency>
    <groupId>org.opentelecoms.client.launch</groupId>
    <artifactId>launch</artifactId>
    <version>1.0.0</version>
  </dependency>

Here is a simple example of using the launcher from Java:

 Launcher launcher = Launcher.getInstance();

 URI myFriend = new URI("sip", "bob@example.org", null);
 launcher.launch(myFriend, SessionType.ANY);

The second argument of the Launcher.launch method can be used to
specify a preference for voice, video or chat.  If specified, the
launcher will try to use a client that supports the specified method.
It will also attempt to tweak the URI to match the medium required,
for example, adding the query parameter "message" to an XMPP URI.
Otherwise, the client may choose to start the session using any of
those methods.

Make sure you include a logging implementation such as slf4j-simple
in your application classpath so that you can see the log messages.

Adding support for other softphones
-----------------------------------

Additional softphones can be supported very easily.  The ServiceLoader
pattern is used to provide a modular/plugin approach.

 * provide a class that implements the interface
   org.opentelecoms.client.launch.Client
   (see the example org.opentelecoms.client.launch.Jitsi)

 * add the implementation class name to the file
   src/main/resources/META-INF/services/org.opentelecoms.client.launch.Client

Pull requests adding support for new softphones are welcome.

License
-------

Copyright (C) 2014, Daniel Pocock http://danielpocock.com

  (MIT license)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.

