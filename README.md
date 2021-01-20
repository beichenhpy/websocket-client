# websocket-demo
websocket for beichenhpy
## websocket-beichenhpy 为服务端 底层使用netty构建
使用开源包 [netty-websocket-spring-boot-starter](https://github.com/YeautyYE/netty-websocket-spring-boot-starter)

感谢 `YeautyYE`
## websocket-client为java客户端
  使用`Java-WebSocket`  使用`Scheduled`做简单的心跳
## index.html 为模拟web客户端
  使用浏览器自带的`websocket` 实现通讯
## 使用方法
注解 `@WebSocketMsg`为path 无 `/`
其他客户端如果想要请求，则需要严格按照`Message`实体类传输 实体类中的 `path`对应注解的 `value`
方法形参请使用`Message`中定义的 `SocketQuery`传输 内容可以自行更改
`SocketResult`可用来放查询出的结果



#### 简单的实现WebSocket的客户端与服务端，发现自己基础知识还有需要恶补
