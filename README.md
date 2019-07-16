# Introduction

This is a simple application that reads an accesslog of certain format, and then finds the IP addresses that has made a number of requests within a given time period.

# How to build

## Full Testing

This is the ideal build step. But this requires you to have `docker-compose` installed in your machine. To run all tests while building, with one terminal, do the following

```
cd ./infra
docker-compuse up -d
cd ..
sh mvnw clean install
```

## Unit Testing only

To run only the unit tests (if you do not have `docker-compose` installed), do the following:

```
sh mvnw clean install
```

All tests that requires a running MYSQL Database will automatically skip if it cannot find a MYSQL Database (specifically, it will look for a MYSQL DB in 127.0.0.1:3307)

# How to run

Once the source code is built, a `parser.jar` will be created in the path `./target/parser.jar`

Go to target (i.e. `cd ./target`), and then from there, copy `.env-sample` and create a `.env` file (i.e. `cp .env-sample .env`). Replace the details of the `.env` file with the details to connect to your MySQL database. If you are running the docker-compose file that is in `./infra`, then copying the `.env-sample` into `.env` should be enough.

## Sample Usages

### Analyzing an accesslog on an hourly basis

Command:
```
java -cp "parser.jar" com.ef.Parser --accesslog=/path/to/file --startDate=2017-01-01.13:00:00 --duration=hourly --threshold=100

# or

java -jar "parser.jar" --accesslog=/path/to/file  --startDate=2017-01-01.13:00:00 --duration=hourly --threshold=100
```

Explanation:

This command will read the accesslog located in `/path/to/file`, load the data into a MYSQL database, then query it starting from `2017-01-01.13:00:00` and look for IP addresses that are in the accesslog equal to or more than `100` (_the threshold_) times within an hour. And this does so for every hour. 

For example, it will check for ip address from `2017-01-01.13:00:00` to `2017-01-01.14:00:00`, and from `2017-01-01.14:00:00` to `2017-01-01.15:00:00`, and so on and so forth. And for each of those periods, it will look for ip address that appear in those period for `100` or more times.

Sample Result:

```
 * 192.168.151.234 did 248 call(s) from 2017-01-01 22:00:00 to 2017-01-01 23:00:00
 * 192.168.180.110 did 247 call(s) from 2017-01-01 16:00:00 to 2017-01-01 17:00:00
 *  192.168.184.25 did 247 call(s) from 2017-01-01 18:00:00 to 2017-01-01 19:00:00
 * 192.168.236.106 did 245 call(s) from 2017-01-01 20:00:00 to 2017-01-01 21:00:00
 *   192.168.14.80 did 245 call(s) from 2017-01-01 18:00:00 to 2017-01-01 19:00:00
 *  192.168.73.200 did 240 call(s) from 2017-01-01 22:00:00 to 2017-01-01 23:00:00
 * 192.168.119.134 did 237 call(s) from 2017-01-01 14:00:00 to 2017-01-01 15:00:00
 * 192.168.159.230 did 236 call(s) from 2017-01-01 23:00:00 to 2017-01-02 00:00:00
 *  192.168.14.145 did 235 call(s) from 2017-01-01 21:00:00 to 2017-01-01 22:00:00
 *  192.168.113.80 did 234 call(s) from 2017-01-01 16:00:00 to 2017-01-01 17:00:00
 *    192.168.97.7 did 234 call(s) from 2017-01-01 22:00:00 to 2017-01-01 23:00:00
 *  192.168.238.69 did 233 call(s) from 2017-01-01 18:00:00 to 2017-01-01 19:00:00
 * 192.168.106.134 did 232 call(s) from 2017-01-01 15:00:00 to 2017-01-01 16:00:00
 *    192.168.88.0 did 231 call(s) from 2017-01-01 14:00:00 to 2017-01-01 15:00:00
 * 192.168.253.195 did 229 call(s) from 2017-01-01 17:00:00 to 2017-01-01 18:00:00
 * 192.168.122.135 did 229 call(s) from 2017-01-01 23:00:00 to 2017-01-02 00:00:00
 *   192.168.5.107 did 229 call(s) from 2017-01-01 19:00:00 to 2017-01-01 20:00:00
 * 192.168.185.253 did 227 call(s) from 2017-01-01 17:00:00 to 2017-01-01 18:00:00
 * 192.168.235.156 did 224 call(s) from 2017-01-01 18:00:00 to 2017-01-01 19:00:00
 * 192.168.247.235 did 224 call(s) from 2017-01-01 14:00:00 to 2017-01-01 15:00:00
 *  192.168.246.75 did 222 call(s) from 2017-01-01 19:00:00 to 2017-01-01 20:00:00
 *  192.168.129.60 did 222 call(s) from 2017-01-01 18:00:00 to 2017-01-01 19:00:00
 * 192.168.177.190 did 219 call(s) from 2017-01-01 20:00:00 to 2017-01-01 21:00:00
 * 192.168.181.166 did 217 call(s) from 2017-01-01 20:00:00 to 2017-01-01 21:00:00
 * 192.168.246.179 did 217 call(s) from 2017-01-01 16:00:00 to 2017-01-01 17:00:00
 * 192.168.206.198 did 215 call(s) from 2017-01-01 14:00:00 to 2017-01-01 15:00:00
 *  192.168.225.93 did 215 call(s) from 2017-01-01 20:00:00 to 2017-01-01 21:00:00
 *   192.168.51.91 did 214 call(s) from 2017-01-01 18:00:00 to 2017-01-01 19:00:00
 *  192.168.77.101 did 214 call(s) from 2017-01-01 13:00:00 to 2017-01-01 14:00:00
 *  192.168.11.231 did 211 call(s) from 2017-01-01 15:00:00 to 2017-01-01 16:00:00
 * 192.168.147.152 did 211 call(s) from 2017-01-01 21:00:00 to 2017-01-01 22:00:00
 * 192.168.101.116 did 210 call(s) from 2017-01-01 14:00:00 to 2017-01-01 15:00:00
 * 192.168.228.188 did 209 call(s) from 2017-01-01 13:00:00 to 2017-01-01 14:00:00
 *  192.168.41.139 did 209 call(s) from 2017-01-01 20:00:00 to 2017-01-01 21:00:00
 * 192.168.132.251 did 208 call(s) from 2017-01-01 20:00:00 to 2017-01-01 21:00:00
 * 192.168.224.201 did 208 call(s) from 2017-01-01 18:00:00 to 2017-01-01 19:00:00
 *   192.168.67.28 did 206 call(s) from 2017-01-01 23:00:00 to 2017-01-02 00:00:00
 *  192.168.114.17 did 204 call(s) from 2017-01-01 19:00:00 to 2017-01-01 20:00:00
 *  192.168.99.166 did 202 call(s) from 2017-01-01 17:00:00 to 2017-01-01 18:00:00
 *  192.168.224.77 did 201 call(s) from 2017-01-01 16:00:00 to 2017-01-01 17:00:00
 *  192.168.109.43 did 200 call(s) from 2017-01-01 16:00:00 to 2017-01-01 17:00:00
```

### Analyzing an accesslog on a daily basis

Command:
```
java -cp "parser.jar" com.ef.Parser --startDate=2017-01-01.13:00:00 --duration=daily --threshold=250

# or

java -jar "parser.jar" --accesslog=/path/to/file  --startDate=2017-01-01.13:00:00 --duration=daily --threshold=250
```

Explanation:

This command will read the accesslog located in `/path/to/file`, load the data into a MYSQL database, then query it starting from `2017-01-01.13:00:00` and look for IP addresses that are in the accesslog equal to or more than `250` (_the threshold_) times within a day. And this does so for every day. 

For example, it will check for ip address from `2017-01-01.13:00:00` to `2017-01-02.13:00:00`, and from `2017-01-02.13:00:00` to `2017-01-03.13:00:00`, and so on and so forth. And for each of those periods, it will look for ip address that appear in those period for `250` or more times.

Sample Result:

```
 * 192.168.129.191 did 350 call(s) from 2017-01-01 13:00:00 to 2017-01-02 13:00:00
 *   192.168.38.77 did 336 call(s) from 2017-01-01 13:00:00 to 2017-01-02 13:00:00
 * 192.168.143.177 did 332 call(s) from 2017-01-01 13:00:00 to 2017-01-02 13:00:00
 * 192.168.199.209 did 308 call(s) from 2017-01-01 13:00:00 to 2017-01-02 13:00:00
 * 192.168.203.111 did 300 call(s) from 2017-01-01 13:00:00 to 2017-01-02 13:00:00
 *  192.168.51.205 did 296 call(s) from 2017-01-01 13:00:00 to 2017-01-02 13:00:00
 * 192.168.162.248 did 281 call(s) from 2017-01-01 13:00:00 to 2017-01-02 13:00:00
 *  192.168.62.176 did 279 call(s) from 2017-01-01 13:00:00 to 2017-01-02 13:00:00
 *  192.168.52.153 did 273 call(s) from 2017-01-01 13:00:00 to 2017-01-02 13:00:00
 *   192.168.33.16 did 265 call(s) from 2017-01-01 13:00:00 to 2017-01-02 13:00:00
 *   192.168.31.26 did 263 call(s) from 2017-01-01 13:00:00 to 2017-01-02 13:00:00
 *  192.168.219.10 did 254 call(s) from 2017-01-01 13:00:00 to 2017-01-02 13:00:00
```

Note: 
In both cases, if you do not pass an `--accesslog=...` parameter, it will run the query against the existing contents of the database. And if you do pass a `--accesslog=...`, it will clear the database first, input the contents of that `accesslog` into the database, and then query it for the result.

# Design

The maven build creates a fat jar with the compiled classes and all dependencies combined inside `parser.jar`. 

The main entry point of the application is `com.ef.Parser`. Upon startup, it will run its database migration script (using `Flyway`) to create the tables needed by this applcation. 

And then it will accept the commandline parameters through `ParserCli` (through the library `picocli`). 

`ParserCli` would then delegate the logic unto `ParserService` to do the actual parsing of the actual accesslog file, and querying for the ip addresses.

To be specific, this is what `ParserService` will do

1. if `accesslog` is provided, then it will clear the whole `access_log_entry` table.
2. if `accesslog` is provided, it will parse that file every X lines (by default, X is configured to be 10,000 lines). Then for every X lines, it will convert them all into an object list of `AccessLogEntry`. Once converted into a `List` of `AccessLogEntry`, they would all then be committed into the MYSQL database with one commit (the technology used is `MyBatis`)
3. Then the `period_summary` table would be cleared. This is because the `period_summary` table can be recreated pretty fast.
4. Next, `access_log_entry` table would be queried, and the result would be a `List` of `PeriodSummary`
5. The `List` of `PeriodSummary` would then be stored into the database, and printed into the console. 

# Technologies Used

 * [Java 11](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html) - I used Java 11 because Java 8's free use ends in December 2020. And practically everybody has skipped Java 9 and 10 and went straight to Java 11.
 * [Micronaut](https://micronaut.io/) - for the dependency injection. This is relatively new but it has proven to be quite promising. This does the dependency injection during compile time which means the startup time of the application is much faster. Traditional dependency injection applications like Spring does the dependency injection at runtime using reflection. The act of using reflection for this greatly increases the startup time and the amount of memory consumed (Note: the resulting parser.jar of this application do has some startup delay. That's because it's connecting to the database. But aside from that, the application does nothing else to slow it down)
 * [Picocli](https://picocli.info/) - This is for the commandline argument, to allow pretty formating and out of the box parameter handling
 * [MyBatis](http://www.mybatis.org/mybatis-3/) - MyBatis differentiates itself as an Object Mapper (and not as an Object Relational Mapper). That is, you use native SQL query to interact with your database, and you just use `MyBatis` to map the resultset into your objects.
 * [FlyWay](https://flywaydb.org/) - FlyWay is a simple database migration tool. Unlike liquibase that can be made database agnostic, FlyWay is usually used when you are just working with one database as the resulting migration script is very straight forward and easy to work with.
 * [SLF4J](https://www.slf4j.org/) - I wanted to use log4j2 for this project but it still does not play nicely with Java 11. So I went for good 'ol SLF4J instead.
 * [Project Lombok](https://projectlombok.org/) - Project Lombok is another library which uses compile time voodoo to improve code readability. It mainly allows you to define your POJOs properties, and it will automatically inject getters, setters, equals, hashCode, toString into your POJOs. Unlike Micronaut which creates new classes at runtime to serve the dependency injection, Lombok actually modifies your POJOs' class files to add those methods.
 * [Junit 5](https://junit.org/junit5/) - Not many people use JUnit 5 yet and it's a shame. Junit 3 & 4 were great technologies of its time, but if you've tried writing tests in other languages, you would feel that java's test structure is a bit outdated. That is till you use Junit 5 ;) 
 * [Assert4j](https://joel-costigliola.github.io/assertj/) - I was a heavy hamcrest user before, because it did allow me to create great junit reports when tests would fail. However, the test code can get unwieldy. With assert4j, you can capitalise on java's new language features to make more maintainable test code, while still having excellent test reports (i.e. test report that will tell you what's wrong without you looking at the code).
 * [Mockito](https://site.mockito.org/) - Good 'ol mockito for mocking tests. It may be a bit old, but it's still the best with what it does in the java world.

# SQL Notes

## Sql Schema

The sql schema can be found in `src/main/resources/databasemigrations` are are executed automatically by `FlyWay` upon startup. The contents of which are as follows:

`V1__create-access-log-entry-schema.sql`

```
create table access_log_entry (
  id int not null primary key auto_increment,
  timestamp datetime(6) not null,
  ip_address varchar(255) not null,
  http_call varchar(255) not null,
  http_status_code int not null,
  user_agent varchar(1024) not null
) ENGINE=InnoDB DEFAULT CHARSET=utf8
```

`V2__create-period-summary-schema.sql`

```
create table period_summary (
  id int not null primary key auto_increment,
  ip_address varchar(255) not null,
  start_period datetime(6) not null,
  end_period datetime(6) not null,
  cnt int not null,
  comment varchar(1024) not null
) ENGINE=InnoDB DEFAULT CHARSET=utf8
```

## SQL Queries

The SQL Queries used by the application are encapsulated in the `XYXMapper` java files which can be found in the `com.ef.repository.mapper` package. 

### Deleting all access_log_entries

```
delete from access_log_entry
```

### Bulk Insert of access_log_entries

```
insert into  access_log_entry (timestamp, ip_address, http_call, http_status_code, user_agent) values 
(?, ?, ?, ?, ?),
(?, ?, ?, ?, ?),
(?, ?, ?, ?, ?),
...
(?, ?, ?, ?, ?)
```

### Querying for ip addresses that goes beyond the threshold on an hourly basis

```
select tmp.ip_address
     , tmp.period as start_period
     , DATE_ADD(tmp.period, interval 1 hour) as end_period
     , tmp.cnt
     , concat(LPAD(tmp.ip_address, 15, ' '), " did ", tmp.cnt, " call(s) from ", tmp.period, " to ", DATE_ADD(tmp.period, interval 1 hour)) as comment
  from (
        select ip_address
             , DATE_FORMAT(timestamp, concat("%Y-%m-%d %H:", LPAD(#{startDateTime.minute}, 2, '0'), ":", LPAD(#{startDateTime.second}, 2, '0'))) as period
             , count(1) as cnt
          from access_log_entry
         where timestamp >= #{startDateTime}
        group by ip_address, period
        having count(1) >= #{threshold}
        order by count(1) desc
       ) tmp
```

### Querying for ip addresses that goes beyond the threshold on a daily basis

```
select tmp.ip_address
     , tmp.period as start_period
     , DATE_ADD(tmp.period, interval 1 daily) as end_period
     , tmp.cnt
     , concat(LPAD(tmp.ip_address, 15, ' '), " did ", tmp.cnt, " call(s) from ", tmp.period, " to ", DATE_ADD(tmp.period, interval 1 daily)) as comment
  from (
        select ip_address
             , DATE_FORMAT(timestamp, concat("%Y-%m-%d ", LPAD(#{startDateTime.hour}, 2, '0'), ":", LPAD(#{startDateTime.minute}, 2, '0'), ":", LPAD(#{startDateTime.second}, 2, '0'))) as period
             , count(1) as cnt
          from access_log_entry
         where timestamp >= #{startDateTime}
        group by ip_address, period
        having count(1) >= #{threshold}
        order by count(1) desc
       ) tmp
```

### Delete All From Period Summary

```
delete from period_summary
```

### Bulk Insert Period Summary

```
insert into period_summary (ip_address, start_period, end_period, cnt, comment) values
(?, ?, ?, ?, ?),
(?, ?, ?, ?, ?),
(?, ?, ?, ?, ?),
...
(?, ?, ?, ?, ?)
```

### Get all Period Summary

```
select * from period_summary
```

### Others

Aside those those, I also wrote and executed some native SQL scripts direct from a `java.sql.Connection`'s `java.sql.PreparedStatement` for testing purposes. These can be found in the following classes:

 * `com.ef.cli.ParserCliTest`
 * `com.ef.repository.AccessLogEntryRepositoryTest`
 * `com.ef.repository.PeriodSummaryRepositoryTest`
  
# Appendix

 * When loading the project into IntelliJ, the following must be done:
   * `Preferences` > `Build, Execution, Deployment` > `Compiler` > `Annotation Processors`
     * Tick on 'Enable annotation processing'
   * `Preferences` > `Build, Execution, Deployment` > `Compiler` > `Java Compiler`
     * In the `Additional commandline parameters` field, add/set `-parameters`
   * `Preferences` > `Plugins`
     * Install the `Lombok` plugin.