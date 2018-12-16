# 对象序列化与反序列化

在simpletx中，提供了三种序列化方式，分别是

- ``com.xiaojiezhu.simpletx.common.codec.JdkObjectCodec`` JDK序列化
- ``com.xiaojiezhu.simpletx.common.codec.JSONObjectCodec`` Fastjson序列化
- ``com.xiaojiezhu.simpletx.common.codec.KryoObjectCodec`` Kryo序列化

除了这自带的三种序列化方式，开发者也可以编写自己自定义的序列化方式。


## 序列化方式的选择

在这三种提供的序列化方式中，到底该如何选择序列化方式呢?

这里有一个测试类，分别使用这三种序列化方式，进行一百万次的序列化与反序列化。

测试代码如下：

[点击查看测试代码](https://github.com/zxjpro/simpletx/blob/master/simpletx-test/src/test/java/com/xiaojiezhu/simpletx/test/coec/ObjectCodecTest.java)

消耗时间如下

![序列化方式对比](http://wx3.sinaimg.cn/mw690/005ZQTvlly1fy7jse02bzj30tw0kudgo.jpg)

序列化的时间对比

- jdk 用时  25851ms
- fastjson 用时 2066ms
- kryo 用时 3554ms

我们可以看到JDK的序列化速度，简直无法直视，如果有其它的选择，又有谁会选择使用JDK来序列化呢。
但是让我吃惊的是fastjson的序列化速度，竟然是其中最快的，甚至比kryo还要快上一些。而kryo的序列化速度也算中可。

除去序列化的速度来说各自的优缺点的话，jdk比较方便，可以直接序列化与反序列化，只需要加上``Serializable``注解就好了，
而fastjson与kryo都各自要求相应的bean需要有着空参的构造方法。

**在simpletx中，使用的默认序列化方式是fastjson的序列化方式**