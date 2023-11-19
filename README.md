# JavaArgumentModifier

A simple launch wrapper too to modify the command line argument of your java application

It's useful to modify those external-set unchangeable arguments, 
e.g. modify the `-Xmx` jvm argument for the application hosted with [pterodactyl](https://github.com/pterodactyl/panel)
to a lower value to protect the server from OOM killer

## Usage

Example CLI args: `java -Dfoo=bar -Xmx4G -jar JavaArgumentModifier.jar a -b --cd=e`, where:

- `javaExecutable` is `java`
- `targetJar` is `server.jar`
- `jvmArgs` are `-Dfoo=bar` and `-Xmx4G`
- `runArgs` are `a`, `-b` and `--cd=e`

Then, this tool will modify the CLI args according to the config file (at `java_argument_modifier.json`):

```json5
{
  // replace the java executable. Normally you can just use "java" if that's usable
  "javaExecutable": "/path/to/java",

  // replace the jar file, i.e. the value of the "-jar" argument
  "targetJar": "minecraft_server.jar",  
  
  // replace the jvm args
  // keys are regex, values are the replacement
  "jvmArgs": {  
    // match arg starting with "-Xmx", then replace it with "-Xmx3G"
    "^-Xmx": "-Xmx3G"
  },

  // replace the run args
  // keys are regex, values are the replacement
  "runArgs": {
    // remove the "-b" arg
    "-b": null,
  }
}
```

Finally, the tool will start the target application with command `java -Dfoo=bar -Xmx3G -jar server.jar a --cd=e`
