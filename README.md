## About

[logstash-logback-encoder](https://github.com/logfellow/logstash-logback-encoder) with shaded Jackson.
Replaces our old fork (<https://github.com/TouK/logstash-logback-encoder-old>) with a simple buiold script that is easier to maintain.

Allows us to use logstash-logback-encoder together with Flink, without creating conflicts when `classloader.resolve-order` is set to `parent-first`.

Note that version 7.3 is the last one that supports Logback 1.2 (and Slf4J 1.7).
Version 7.4 requires Logback 1.3 and Slf4J 2.
Version 8.0 requires Logback 1.5.

## Publishing

These steps require that you have set up required steps for sbt-pgp (GPG key in keyring) and sbt-sonatype (credentials in ~/.sbt/1.0/sonatype.sbt).

1. Update upstream artifact versions in _build.sbt_
2. Update project version in _build.sbt_, if necessary
3. Test publishing:
   ```sh
   sbt publishSigned
   ```
4. Verify that published JAR (in _target/sonatype-staging_) contains ONLY `net.logstash.logback` and `shaded.pl.touk.nussknacker.lle` packages.
5. Publish new version:
   ```sh
   sbt sonatypeBundleRelease
   ```
