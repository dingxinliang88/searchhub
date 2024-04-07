## Canal 同步 ES 踩坑

1）启动脚本，因为 JDK 版本太高，需要去除一些 JVM 参数 `-XX:+AggressiveOpts -XX:-UseBiasedLocking`

2）实例名称不要改，还是 example

3）canal adapter 配置文件格式（缩进）要正确

4）ES 需要指定集群名称，canal 配置需要

5）业务 id 丢失问题，查两遍 id，一个作为 _id，一个作为业务 id

> 参考文章：https://blog.csdn.net/qq991658923/article/details/132099010