## Java问题定位和性能优化

### 参考文档

[JVM故障分析及性能优化系列之一：使用jstack定位线程堆栈信息](https://www.javatang.com/archives/2017/10/19/33151873.html)




### 基本概念

在对Java内存泄漏进行分析的时候，需要对jvm运行期间的内存占用、线程执行等情况进行记录的dump文件，常用的主要有thread dump和heap dump。

- **thread dump** 主要记录JVM在某一时刻各个线程执行的情况，以栈的形式显示，是一个文本文件。通过对thread dump文件可以分析出程序的问题出现在什么地方，从而定位具体的代码然后进行修正。thread dump需要结合占用系统资源的线程id进行分析才有意义。
- **heap dump** 主要记录了在某一时刻JVM堆中对象使用的情况，即某个时刻JVM堆的快照，是一个二进制文件，主要用于分析哪些对象占用了太对的堆空间，从而发现导致内存泄漏的对象。

上面两种dump文件都具有实时性，因此需要在服务器出现问题的时候生成，并且多生成几个文件，方便进行对比分析。下面我们先来说一下如何生成 thread dump。

### 使用[jstack](http://docs.oracle.com/javase/8/docs/technotes/tools/unix/jstack.html)生成thread dump

#### ps/top指令

利用ps和top指令可以获取静态/动态显示进程数据

ps指令

```shell
# ps指令总结
ps -A #显示所有的进程，和 -e 的效果一样；
ps -a #显示所有进程，包括其他用户的进程；
ps -u #有效的用户id或者是用户名；
ps -x #显示没有控制终端的进程，同时显示各个命令的具体路径
ps -e #显示所有的进程，和 -A 的效果一样；
ps -f #全部列出，通常和其他选项联用；通常与 -e 一起用

# 常用指令
ps -aux
ps -ef| grep
```

top指令

[Linux top命令详解](https://www.cnblogs.com/baichunyu/p/15356162.html)

[Linux之top命令](https://blog.csdn.net/weixin_37335761/article/details/122793401)

```shell
# top指令总结
top -c #显示进程详细的信息，显示整个命令行而不只是显示命令名
top -H #显示线程详细的信息
top -p pid #显示pid对应进程详细的信息，通过指定PID来仅仅监控某个进程的状态
top -d	#指定每两次屏幕信息刷新之间的时间间隔，如希望每秒刷新一次，则使用：top -d 1
top -bn1 #只输出一次结果，而不是显示一个动态的结果
top -i	#使top不显示任何闲置或者僵死的进程
top -n #更新的次数，完成后将会退出 top
top -b #批次档模式，搭配 "n" 参数一起使用，可以用来将 top 的结果输出到档案内

# 进入top指令界面后，可以进行的操作
# 1 对于多核服务器，可以显示各个CPU占用资源的情况
# shift + h (H) 显示所有的线程信息
# shift + w 将当前top命令的设置保存到 ~/.toprc 文件中，这样不用每次都执行快捷键了
# i 可以隐藏闲置或僵死的进程，效果跟输入top -i是一样的
# u + 用户名，则可以查看相应的用户进程
# f 键可以改变排序的列
# x 粗体显示当前排序的列
# 快速切换排序列的方式：比如大写字母M以%MEM列排序，大写字母N以PID列排序，大写字母P以%CPU列排序，大写字母T以TIME+列排序。大写字母R可以将当前的排序结果反转。
# c 来显示完整的执行命令(效果跟top -c相同),显示进程的路径
# b 高亮显示当前运行进程
# shift + > / shift + < 可以向右或左改变排序列
# k 可以在不退出top命令的情况下杀死某个正在运行的进程：

# 常用指令
top -p pid1 -p pid2
top -bn1 -H -p pid #显示pid进程的线程信息，并只输出单一静态结果
ps -mp <pid> -o THREAD,tid,time | sort -k2r
```



#### jstack指令

JDK5开始提供的内置工具，可以打印指定进程中线程运行的状态，包括线程数量、是否存在死锁、资源竞争情况和线程的状态等等。

```shell
# jstach指令参数选项
-l #长列表，打印关于锁的附加信息
-m #打印java和jni框架的所有栈信息
-A 10 #参数用来指定显示行数，否则只会显示一行信息。

# 常用指令
# thread id在栈信息中是以十六进制的形式显示的，因此需要使用将top指令拿到的线程id转成十六进制的值
# 在linuxn直接执行 printf “%x\n” tid 将tid转换为16进制的数字：
jstack -l <pid> | grep <thread-hex-id> -A 10
```

**注意：**需要在多个时间段提出多个 Thread Dump信息，然后综合进行对比分析，单独分析一个文件是没有意义的。



### Thread Dump日志结构解析

