# Compiler Construction Group 07

Group members: Selleban Farah




# Requirements
- Latest version of [Apache Ant](https://ant.apache.org/bindownload.cgi).
- CUP Parser Generator for Java (provided within repository).
-  JFlex (provided within repository).
# Cloning and running 
### Cloning

```
git clone <repo_url>
cd compila24-07
```

### Running

```
 ant 
 ant clean
 ant build
 ant run
 ant compile                             
 ant compile-test                       
 ant test                                
 ant compile-runme                       
 ant run-runme                           
 ant list-runme                          

```

# Grammar

The current default grammar is set to unambigious. To change this refer to this part of the build.xml file,  Then set the srcfile within the cupline to **./src/grammars/compila.cup**

```xml
<!-- Generate scanner and parser using JFlex and CUP -->
<target name="generate" depends="mkdir">
    <jflex file="./src/grammars/compila.lex" destdir="src-gen"/>
    <cup srcfile="./src/grammars/unamb_compila.cup" destdir="src-gen" interface="true" />
</target>

```



# Semantic tests

``` 
 ant clean
 ant build                           
 ant compile-test                       
 ant test                                
                        
```

```xml
<!-- Compile test classes -->
<target name="compile-test" depends="mkdir">
	<javac srcdir="${dir.src}" destdir="${dir.classes}" debug="true"
		includes="test/*.java" classpathref="path-cup" />
</target>

<!-- Run tests -->
<target name="test" depends="compile-test">
	<java classname="test.Tester" classpathref="path-run" >
		<arg value="src/tests/semanticanalysis/noerrors"/>
	</java>
</target>

```


# Runing the compiler


```
 ant 
 ant clean
 ant build                          
 ant compile-runme                       
 ant run-runme                           
 ant list-runme                          
```

```xml
<!-- Run compiler on example RunMe -->
<target name="compile-runme" depends="init">
	<java classname="compiler.Compiler" classpathref="path-run">
		<arg value="src/tests/fullprograms/runme.cmp"/>
		<arg value="src/tests/outputs/ast/runme.ast"/>
		<arg value="src/tests/outputs/bin/runme.bin"/>	
	</java>
</target>

<!-- List example runme -->
<target name="list-runme" depends="init">
	<java classname="runtime.VirtualMachine" classpathref="path-run">
		<arg value="-l"/>
		<arg value="src/tests/outputs/bin/runme.bin"/>
	</java>
</target>

<!-- Run vm on example runme -->
<target name="run-runme" depends="init">
	<java classname="runtime.VirtualMachine" classpathref="path-run">
		<arg value="src/tests/outputs/bin/runme.bin"/>
	</java>
</target>

```

