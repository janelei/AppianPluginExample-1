# Appian World Plug-in Example #
This repository is an example of an Appian OSGI plug-in.  It includes modules for a Custom Data Type, a Custom Smart Service and a Custom Expression Function.  The code for each module is documented and should be easily understandable.  For more information on Appian Plug-ins, see https://forum.appian.com/suite/wiki/66/Developer_Guide

This plug-in provides functionality for creating humorous Appian MadLibs. This plug-in contains the following modules:

* A CDT structure for saving the user's choice of nouns and adjectives 
* A custom function for generating the MadLib text
* A custom smart service for creating a file containing the MadLib text

The template text for the MadLib is stored in the madlib.properties file.

## Repository Structure ##
This repository demonstrates a standard Appian Developer Edition folder structure:

* __bin__: ignored from version control (see .gitignore) but used in a local working directory to store compiled classes
* __etc__: miscellaneous files, usually includes sample app , generated plug-in JAR file and any XSDs
* __lib__: jars needed for runtime
* __lib-compile__: jars needed for standalone compilation but not at runtime because they're injected by the plug-in framework.  Includes Appian public API jars.
* __src__
  * __main__
     * __java__: all Java sourcefiles
     * __resources__: i18n properties files and smart service icons
* __appian-plugin.xml__: plug-in definition
* __build.xml__: ant build file used by Appian Developer Edition

When you first create a project using the Appian Developer Edition, this folder structure will be created automatically for you.

## How to use this plug-in ##
Deploy the generated JAR file and then import the sample application (both are located in the \etc folder).  Then click the Tempo action "Generate Appian MadLib" to walk through a quick example.


## Legal Mumbujumbo ##
Copyright © 2012, Jed Fonner

Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted, provided that the above copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.