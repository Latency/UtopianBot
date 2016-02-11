#RSBot

RSBot is an immensely popular and successful bot for a Java MMORPG.
The program source code is completely open and free.
Whether you're a Java expert or just keen to explore, the bot is a valuable learning resource.

###Get the code

Download the latest source code with:

    git clone git://github.com/Latency/RSBot.git
	
Keep your source up to date with:

    git pull git://github.com/Latency/RSBot.git master

Subversion is also supported:

    svn checkout http://svn.github.com/Latency/RSBot.git

### Dependencies

 * [Java Development Kit](http://www.oracle.com/technetwork/java/javase/downloads/)
 * [Apache Ant](http://ant.apache.org/bindownload.cgi)
 * [Apache Commons - Collections] (http://commons.apache.org/downloads/download_collections.cgi)
 * [Apache Commons - Lang] (http://commons.apache.org/lang/download_lang.cgi)
 * [Guava release 09] (http://guava-libraries.googlecode.com/files/guava-r09.zip)

### Downloads (optional & unnecessary)
 
  - v2.45 Beta (ftp://67.182.48.192/RSBot/RSBot.jar)

### FTP Repository

 * ftp://67.182.48.192/RSBot/

### Installation Notes

 - To install RSBot & Scripts project within the same build so that you may compile / debug both in real-time
   without downloading the RSBot.jar file and/or running the *.bat files to compile in your scripts.

 - Where your GIT repository or project path is located, open your shell and create a directory junction.
   C:\Users\Latency\Documents\Eclipse\RSBot\>  
   
   Notice that my Eclipse projects are within <b>My Documents</b> and my repository and source code has been made
    in the <b>RSBot</b> folder.  From there, you need to create a precompiled directory that points to the
    RSBot cached directory found here:   <code>C:\Users\Latency\Documents\RSBot\Precompiled</code>

    C:\Users\Latency\Documents\Eclipse\RSBot\>  mklink -J Precompiled C:\Users\Latency\Documents\RSBot\Precompiled
   
  - In your IDE, you must check the 'Output folders' option so that it will build the scripts into the relative
     directory within the RSBot project.

     Click the <b>Edit</b> button to select the directory junction folder <b>Precompiled</b>.
     
  - Import the <b>Scripts</b> GIT repository and select its root folder into your build as source.
    You should have the <b>RSBot\src</b> and <b>Scripts</b> folders selected only.
     
  - Two tabs over in the </b>Configure Build Path</b> properties page you will need to add the libraries found
    in the above links.  You will need to add 3 user libraries and give them a name.
    <code>Apache Commons</code>,
    <code>Guava</code>,
    <code>Javax</code>
    
    You can place multiple .jar's inside each of these, and you should end up with 2x inside the Apache Commons.
    
  - Verify all is correct in the <b>Order and Export</b> tab.  You should see the JRE + the new user libraries you
    added.  Move the <b>Scripts</b> item to the very top, followed by the <b>RSBot\src</b>
    
  - Click okay to all and rebuild your project.  When you run it or debug it, your scripts should now show up in the
    Script selector.
    
    
    Enjoy!
 
##Microsoft Windows

###Compiling

Make sure JDK is installed, then execute the `make.bat` file by *double clicking* it.

### Running

Java is required to run RSBot. You can run the client by *double clicking* the JAR file named `RSBot.jar`.

##Linux, Mac OS X and other Unix systems

### Compiling

Make sure JDK and other needed packages (e.g. `make`) are installed on your system. Open a terminal and navigate to the directory where the source code is located, using the `cd` command. Execute `make` in the terminal to compile RSBot.

### Running

To run RSBot, *double click* on the compiled JAR file, `RSBot.jar`, or execute `java -jar RSBot.jar` in a terminal.

## Legal 

The entire source code is freely available under the GPLv3 terms. Contrary to bogus and automated DMCA claims, no content is under the copyright of Jagex Ltd. The code is provided purely for educational purposes by the authors as freedom of expression.
