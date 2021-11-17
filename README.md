# 极简验证-服务端 java sdk

使用极简验证客户端验证完成后需要提交到服务器端再次验证合法性，
可以根据[服务端集成文档](https://www.jijiancode.com/doc/guide/end.html)自己调用 API，
如果后端使用了 Java 语言，也可以直接使用该SDK。

SDK 依赖 `org.json:json`, 如果使用 `maven` 可以添加依赖：
```xml
<dependency>
    <groupId>org.json</groupId>
    <artifactId>json</artifactId>
    <version>20190116</version>
</dependency>
```

如果使用的 gradle 可以添加依赖：
```gradle
implementation 'org.json:json:20190116'
```

// 导入包


调用 `JJCode.verify` 方法进行验证：

```java
VerifyState vs = JJCode.verify(appId, userSecret, mobile, requestToken);
if(vs.getStatus() == 1) {
    // 验证成功
} else {
    // 验证失败
}
```

参数说明：

| 参数名称 | 参数类型 | 参数说明 |
| --- | --- | --- |
| `appId` | `String` | 应用ID |
| `userSecret` | `String` | 用户密钥Token，在控制台查看 |
| `mobile` | `String` | 手机号 |
| `requestToken` | `String` | 请求ID，客户端验证后提交 |