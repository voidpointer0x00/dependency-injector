<div align=center>
  <h1>The Dependency Injector</h1>
  <p>A small and simple to use DI library for Spigot plugins</p>
</div>
<div align=center>

[![JitPack][JitPackBadge]][JitPackUrl]
[![Tests][TestsBadge]][TestsUrl]
[![CodeQL][CodeQLBadge]][CodeQLUrl]
[![WTFPL][LicenseBadge]](LICENSE)

</div>

### API usage
Example can be found [here](https://github.com/NyanGuyMF/dependency-injector/tree/master/src/test/java/voidpointer/spigot/framework/di)

Generally you just annotate your `static` fields with [`@Dependency`][DependencyUrl] in `JavaPlugin` class,
add [`@Autowired`][AutowiredUrl] annotation to the fields *(they also **must** be* `static`*)* that you want
to injectand invoke [`Injector#inject(JavaPlugin)`][InvokeUrl] method on your plugin object.

```java
public final class AwesomePlugin extends JavaPlugin {
    @Dependency private static Database database;

    @Override public void onLoad() {
        database = new HibernateDatabase();
    }

    @Override public void onEnable() {
        Injector.inject(this);
    }
}

public final class PlayerService {
    @Autowired private static Database db;

    public CompletableFuture<DbResult> addPlayer(final DbPlayer dbPlayer) {
        return CompletableFuture.supplyAsync(() -> db.createOrUpdate(dbPlayer));
    }
}
```

### Maven
```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>

<dependency>
  <groupId>com.github.NyanGuyMF</groupId>
  <artifactId>dependency-injector</artifactId>
  <version>1.0.0</version>
</dependency>
```
### Gradle
```gradle
repositories {
  maven { url 'https://jitpack.io' }
}

dependencies {
  implementation 'com.github.NyanGuyMF:dependency-injector:1.0.0'
}
```
[DependencyUrl]: https://github.com/NyanGuyMF/dependency-injector/blob/940ceed8fcc17ceaac79a460beb3642fc13835e7/src/main/java/voidpointer/spigot/framework/di/Dependency.java#L24
[AutowiredUrl]: https://github.com/NyanGuyMF/dependency-injector/blob/940ceed8fcc17ceaac79a460beb3642fc13835e7/src/main/java/voidpointer/spigot/framework/di/Autowired.java#L24
[InvokeUrl]: https://github.com/NyanGuyMF/dependency-injector/blob/940ceed8fcc17ceaac79a460beb3642fc13835e7/src/main/java/voidpointer/spigot/framework/di/Injector.java#L34

[JitPackBadge]: https://jitpack.io/v/NyanGuyMF/dependency-injector.svg
[TestsBadge]: https://github.com/NyanGuyMF/dependency-injector/actions/workflows/tests.yml/badge.svg
[CodeQLBadge]: https://github.com/NyanGuyMF/dependency-injector/actions/workflows/codeql-analysis.yml/badge.svg
[LicenseBadge]: https://img.shields.io/github/license/NyanGuyMF/dependency-injector.svg

[JitPackUrl]: https://github.com/NyanGuyMF/dependency-injector
[TestsUrl]: https://github.com/NyanGuyMF/dependency-injector/actions/workflows/tests.yml
[CodeQLUrl]: https://github.com/NyanGuyMF/dependency-injector/actions/workflows/codeql-analysis.yml
