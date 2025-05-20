## data-cache

> 数据缓存解决方案，主要解决以下问题
> 1. 缓存穿透问题（布隆过滤器）
> 2. 缓存击穿问题
     > 延迟双删（该demo中采用的是延迟三删除，操作数据库前删除一次，操作缓存前删除一次，操作缓存后延迟一秒再删除一次。）
     > 延迟删除采用的是RabbitMQ延迟消息机制

在扩展时只需要增加对应的数据缓存工具类即可，注入缓存工具类统一采用反射的方式找到对应的工具类。大大降低了开发的复杂度。
下面是缓存的执行流程

### 初始化数据

![](https://resource.liulingfengyu.cn/img/open-source/初始化数据-数据缓存.jpg)

### 分页查询

![](https://resource.liulingfengyu.cn/img/open-source/分页查询-数据缓存.jpg)

### 根据id查询

![](https://resource.liulingfengyu.cn/img/open-source/根据id查询-数据缓存.jpg)

### 新增流程

![](https://resource.liulingfengyu.cn/img/open-source/新增流程-数据缓存.jpg)

### 修改流程

![](https://resource.liulingfengyu.cn/img/open-source/修改流程-数据缓存.jpg)

### 删除流程

![](https://resource.liulingfengyu.cn/img/open-source/删除流程-数据缓存.jpg)
