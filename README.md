
## websocket-client为java客户端
  使用`Java-WebSocket`  使用`Scheduled`做简单的心跳
## index.html 为模拟web客户端
  使用浏览器自带的`websocket` 实现通讯
## 使用方法
其他客户端如果想要请求，则需要严格按照`Message`实体类传输 实体类中的 `path`对应注解的 `value`

#### 第三方引入说明 
mvn install 本项目到本地仓库
引入后 需要配置 `@ComponentScan(value = "cn.beichenhpy")` 才可以使用。。



#### 简单的实现WebSocket的客户端与服务端，发现自己基础知识还有需要恶补
