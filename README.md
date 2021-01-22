
## websocket-client为java客户端
  使用`Java-WebSocket`  使用`Scheduled`做简单的心跳
## 使用方法
其他客户端如果想要请求，则需要严格按照`Message`实体类传输 实体类中的 `path`对应注解的 `value`

#### 第三方引入说明 
mvn install 本项目到本地仓库
引入后 需要配置 `@ComponentScan(value = "cn.beichenhpy")` 才可以使用。
Message数据结构如下：
```json
{
	fromUser: "bigscreen",
	toUser: "test",
	msgType: "0",
	content: {
		path: "/test",
		msgQuery: {
			queryContent: {
				test: "sss"
			},
			queryType: "test"
		},
		socketResult: {
			
		}
	}
}
```
