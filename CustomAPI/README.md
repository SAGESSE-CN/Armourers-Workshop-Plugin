# customapi [![Build Status](https://ci.codemc.io/job/WesJD/job/customapi/badge/icon)](https://ci.codemc.io/job/WesJD/job/customapi/)
customapi is a library. 

## Requirements
Java 8 and Bukkit / Spigot. Most server versions in the [Spigot Repository](https://hub.spigotmc.org/nexus/) are supported.

### My version isn't supported
If you are a developer, submit a pull request adding a wrapper module for your version. Otherwise, please create an issue
on the issues tab. 

## Usage

### As a dependency

customapi requires the usage of Maven or a Maven compatible build system. 
```xml
<dependency>
    <groupId>moe.plushie.armourers_workshop</groupId>
    <artifactId>customapi</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>

<repository>
    <id>codemc-snapshots</id>
    <url>https://repo.codemc.io/repository/maven-snapshots/</url>
</repository>
```

It is best to be a good citizen and relocate the dependency to within your namespace in order 
to prevent conflicts with other plugins. Here is an example of how to relocate the dependency:
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
            <version>${shade.version}</version> <!-- The version must be at least 3.3.0 -->
            <executions>
                <execution>
                    <phase>package</phase>
                    <goals>
                        <goal>shade</goal>
                    </goals>
                    <configuration>
                        <relocations>
                            <relocation>
                                <pattern>moe.plushie.armourers_workshop.customapi</pattern>
                                <shadedPattern>[YOUR_PLUGIN_PACKAGE].customapi</shadedPattern> <!-- Replace [YOUR_PLUGIN_PACKAGE] with your namespace -->
                            </relocation>
                        </relocations>
                        <filters>
                            <filter>
                                <artifact>*:*</artifact>
                                <excludeDefaults>false</excludeDefaults>
                                <includes>
                                    <include>[YOUR_PLUGIN_PACKAGE].customapi</include>
                                </includes>
                            </filter>
                        </filters> 
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```
Note: In order to solve `<minimizeJar>` removing customapi `VerionWrapper`s from the final jar and making the library unusable,
ensure that your `<filters>` section contains the example `<filter>` as seen above.

### A Common Use Case Example
```java

```
                                                                                                                                                                                                                                                                              

## Development 
We use Maven to handle our dependencies. Run `mvn clean install` using Java 17 to build the project.

### Spotless
The project utilizes the [Spotless Maven Plugin](https://github.com/diffplug/spotless/tree/main/plugin-maven) to
enforce style guidelines. You will not be able to build the project if your code does not meet the guidelines.
To fix all code formatting issues, simply run `mvn spotless:apply`.

## License
This project is licensed under the [MIT License](LICENSE).
