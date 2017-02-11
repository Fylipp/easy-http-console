**EasyHttpConsole** is a simple library for easily adding console interfaces that can be accessed via the web to applications. The user interface is served via HTTP and the 
   commands and responses are sent other websockets.
   
## How do I use it?
This is a simple code snippet to get you started:
```java
EasyHttpCLI cli = new EasyHttpCLI();
cli.registerCommandHandler("ping", cmd -> cmd.getSource().getConnection().send("Pong."));
cli.registerCommandHandler("stop", cmd -> {
   try {
       cmd.getSource().getHttpConsole().close();
   } catch (Exception e) {
       e.printStackTrace();
   }
});
cli.start();
```
After running your program, you can open your browser and go to `localhost:8080`. You should then see a console and an input field. You can send commands by hitting enter after you finished entering them. If you want to change the port or even the host of your server you can simply specify them as constructor arguments.
![Screenshot of the example](http://imgur.com/oRUxFnB.png)

## How do I get it?
You can obtain pre-built JARs in the `Releases` tab or clone the repository and compile it yourself.
 
Alternatively you can add it as a Maven dependency. **Note:** The Maven dependency is a 'fake' dependency which uses [JitPack](https://jitpack.io/) to fetch the git repository 
and use it. Because of this you have to add a custom repository to your POM.
```xml
<dependencies>
    <dependency>
        <groupId>com.github.Fylipp</groupId>
        <artifactId>EasyHttpConsole</artifactId>
        <version>v1.0.0</version>
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
**EasyHttpConsole** is released under the [MIT license](https://tldrlegal.com/license/mit-license) (note that the summary is 
not a replacement of the full license and holds no legal value). The full license can be found in the `LICENSE` file which is located in the root folder of the repository.