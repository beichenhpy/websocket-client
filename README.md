
## websocket-client为java客户端
  使用`Java-WebSocket`  使用`Scheduled`做简单的心跳
## 使用方法
其他客户端如果想要请求，则需要严格按照`Message`实体类传输 实体类中的 `path`对应注解的 `value`

#### 第三方引入说明 
mvn install 本项目到本地仓库
Message数据结构如下：
```json
{
  "fromUser": "bigscreen",
  "toUser": "test",
  "msgType": "0",
  "content": {
    "path": "/test",
    "msgQuery": {
      "queryContent": {
        "test": "sss"
      },
      "queryType": "test"
    },
    "socketResult": {
    }
  }
}
```
### 配置文件说明
```yaml
ws-client:
  reconnect-time: 5000
  reflection-path: 'cn.beichenhpy' #注解需要扫描的目录
  web-socket-server-uri: 'ws://localhost:9999/beichenhpy/ws/backend?req=ok' #websocketServerUri
```
