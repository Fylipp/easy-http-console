**easy-http-console** is a simple library for easily adding console interfaces that can be accessed via the web to applications. The user interface is served via HTTP and the 
   commands and responses are sent over websockets.
   
## How do I use it?
This is a simple code snippet to get you started:
```java
CommandRegistry cr = new SimpleCommandRegistry();

cr.put("args", cmd -> {
    cmd.respond("Args: " + cmd.getArgsCount());

    for (int i = 0; i < cmd.getArgsCount(); i++) {
        cmd.respond(i + " = \"" + cmd.getArg(i) + "\"");
    }
});
cr.put("ping", cmd -> cmd.respond("Pong."));
cr.put("stop", cmd -> {
    try {
        cmd.getSource().getConnection().getConsole().close();
    } catch (Exception e) {
        e.printStackTrace();
    }
});

Console console = new SimpleConsole<>(new HttpNetModule());
console.messageReceivedEvent().addListener(cr::supplyMessage);
console.start();
```
After running your program, you can open your browser and go to `localhost:8080`. You should then see a console and an input field. You can send commands by hitting enter after you finished entering them. If you want to change the port or even the host of your server you can simply specify them as constructor arguments of the `HttpNetModule`.
![Screenshot of the example](http://i.imgur.com/E2THl4O.png)

## How do I get it?
You can obtain pre-built JARs in the `Releases` tab or clone the repository and compile it yourself.
 
Alternatively you can add it as a Maven dependency. **Note:** The Maven dependency is a 'fake' dependency which uses [JitPack](https://jitpack.io/) to fetch the git repository 
and use it. Because of this you have to add a custom repository to your POM.
```xml
<dependencies>
    <dependency>
        <groupId>com.github.Fylipp</groupId>
        <artifactId>easy-http-console</artifactId>
        <version>v2.0.0</version>
    </dependency>
</dependency>
```
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

## What am I allowed to do with it?
**easy-http-console** is released under the [MIT license](https://tldrlegal.com/license/mit-license) (note that the summary is 
not a replacement of the full license and holds no legal value). The full license can be found in the `LICENSE` file which is located in the root folder of the repository.