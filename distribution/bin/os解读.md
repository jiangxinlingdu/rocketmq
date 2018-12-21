```
#!/bin/sh

# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

export PATH=$PATH:/sbin

# sudo sysctl -w vm.extra_free_kbytes=2000000
# sudo sysctl -w vm.min_free_kbytes=1000000

sudo sysctl -w vm.overcommit_memory=1

默认值为：0
从内核文档里得知，该参数有三个值，分别是：
0：当用户空间请求更多的的内存时，内核尝试估算出剩余可用的内存。
1：当设这个参数值为1时，内核允许超量使用内存直到用完为止，主要用于科学计算
2：当设这个参数值为2时，内核会使用一个决不过量使用内存的算法，即系统整个内存地址空间不能超过swap+50%的RAM值，50%参数的设定是在overcommit_ratio中设定。


sudo sysctl -w vm.drop_caches=1

向/proc/sys/vm/drop_caches文件中写入数值可以使内核释放page cache，dentries和inodes缓存所占的内存。
只释放pagecache：
echo 1 > /proc/sys/vm/drop_caches
只释放dentries和inodes缓存：
echo 2 > /proc/sys/vm/drop_caches
释放pagecache、dentries和inodes缓存：
echo 3 > /proc/sys/vm/drop_caches
这个操作不是破坏性操作，脏的对象（比如脏页）不会被释放，因此要首先运行sync命令。
注：这个只能是手动释放


sudo sysctl -w vm.zone_reclaim_mode=0

zone_reclaim_mode模式是在2.6版本后期开始加入内核的一种模式，可以用来管理当一个内存区域(zone)内部的内存耗尽时，是从其内部进行内存回收还是可以从其他zone进行回收的选项
在申请内存时(内核的get_page_from_freelist()方法中)，内核在当前zone内没有足够内存可用的情况下，会根据zone_reclaim_mode的设置来决策是从下一个zone找空闲内存还是在zone内部进行回收。这个值为0时表示可以从下一个zone找可用内存，非0表示在本地回收。这个文件可以设置的值及其含义如下：
echo 0 > /proc/sys/vm/zone_reclaim_mode：意味着关闭zone_reclaim模式，可以从其他zone或NUMA节点回收内存。
echo 1 > /proc/sys/vm/zone_reclaim_mode：表示打开zone_reclaim模式，这样内存回收只会发生在本地节点内。
echo 2 > /proc/sys/vm/zone_reclaim_mode：在本地回收内存时，可以将cache中的脏数据写回硬盘，以回收内存。
echo 4 > /proc/sys/vm/zone_reclaim_mode：可以用swap方式回收内存。



sudo sysctl -w vm.max_map_count=655360

进程中内存映射区域的最大数量。在调用malloc，直接调用mmap和mprotect和加载共享库时会产生内存映射区域。虽然大多数程序需要的内存映射区域不超过1000个，但是特定的程序，特别是malloc调试器，可能需要很多，例如每次分配都会产生一到两个内存映射区域。默认值是65536。


sudo sysctl -w vm.dirty_background_ratio=50

缺省设置：10。该文件表示废数据到达系统整体内存的百分比,保留过期页缓存的最大值。是以MmeFree+Cached-Mapped的值为基准的，此时触发pdflush进程把废数据写回磁盘。缺省设置：10


sudo sysctl -w vm.dirty_ratio=50

缺省值：40。总内存的最大百分比，系统所能拥有的最大脏页缓存的总量。超过这个值，开启pdflush写入磁盘。如果cached增长快于pdflush，那么整个系统在40%的时候遇到I/O瓶颈，所有的I/O都要等待cache被pdflush进磁盘后才能重新开始。

sudo sysctl -w vm.dirty_writeback_centisecs=360000

表示pdflush进程周期性间隔多久把废数据写回磁盘。缺省值：500（1/100秒）

sudo sysctl -w vm.page-cluster=3

该参数控制一次写入或读出swap分区的页面数量。它是一个对数值，如果设置为0，表示1页；如果设置为1，表示2页；如果设置为2，则表示4页。如果设置为0，则表示完全禁止预读取。
默认值是3（一次8页）。如果swap比较频繁，调整该值的收效不大。
该参数的值越小，在处理最初的页面错误时延迟会越低。但如果随后的页面错误对应的页面也是在连续的页面中，则会有I/O延迟。

sudo sysctl -w vm.swappiness=1

减少系统对于swap频繁的写入，将加快应用程序之间的切换，有助于提升系统性能。默认值为60。

echo 'ulimit -n 655350' >> /etc/profile
echo '* hard nofile 655350' >> /etc/security/limits.conf

echo '* hard memlock      unlimited' >> /etc/security/limits.conf
echo '* soft memlock      unlimited' >> /etc/security/limits.conf

limits.conf文件实际上是linux PAM中pam_limits.so的配置文件，而且只针对于单个会话。
limits.conf的格式如下：
<domain>　　<type>　　<item>　　<value>
domain有好几种格式，具体可以用man limits.conf来查看，不过一般来说，我们都是用的用户名和组名的形式：username|@groupname
设置需要被限制的用户名，组名前面加@和用户名区别。也可以用通配符*来做所有的限制。
type：有soft，hard和-，soft指的是当前系统生效的设置值，软限制也可以理解为警告值。hard表名系统中所能设定的最大值。soft的限制不能比hard限制高，用-表名同时设置了soft和hard的值。
item表明需要限制的使用资源类型
core　　限制内核文件的大小
date　　最大数据大小
fsize　　最大文件大小
memlock　　最大锁定内存地址空间
nofile　　打开文件的最大数目
rss　　最大持久设置大小
stack　　最大栈大小
cpu　　以分钟为单位的最多CPU时间
noproc 进程的最大数目
as　　地址空间限制
maxlogins　　此用户允许登录的最大数目
要是limits.conf文件配置生效，必须确保pam_limits.so文件被加入到启动文件中，要查看/etc/pam.d/login文件中有session required /lib/security/pam_limits.so

ulimit命令，用于shell启动进程所占用的资源限制
一般可以通过使用ulimit命令或者编辑/etc/security/limits.conf重新加载的方式来使限制生效。
通过ulimit比较直接，但只在当前的session有效，limits.conf中可以根据用户和限制项使用户在下次登录中生效。对于limits.conf的设定是通过pam_limits.co的加载生效的，比如/etc/pam.d/sshd这样通过ssh登录时会加载limit又或者在/etc/pam.d/login加载生效。



DISK=`df -k | sort -n -r -k 2 | awk -F/ 'NR==1 {gsub(/[0-9].*/,"",$3); print $3}'`
[ "$DISK" = 'cciss' ] && DISK='cciss!c0d0'
echo 'deadline' > /sys/block/${DISK}/queue/scheduler


Linux IO Scheduler（Linux IO 调度器）
Deadline算法的核心在于保证每个IO请求在一定的时间内一定要被服务到，以此来避免某个请求饥饿。
Deadline算法中引入了四个队列，这四个队列可以分为两类，每一类都由读和写两类队列组成，一类队列用来对请求按起始扇区序号进行排序，通过红黑树来组织，称为sort_list；另一类对请求按它们的生成时间进行排序，由链表来组织，称为fifo_list。每当确定了一个传输方向(读或写)，那么将会从相应的sort_list中将一批连续请求dispatch到requst_queue的请求队列里，具体的数目由fifo_batch来确定。只有下面三种情况才会导致一次批量传输的结束：
1）对应的sort_list中已经没有请求了
2）下一个请求的扇区不满足递增的要求
3）上一个请求已经是批量传输的最后一个请求了。
所有的请求在生成时都会被赋上一个期限值(根据jiffies)，并按期限值排序在fifo_list中，读请求的期限时长默认为为500ms，写请求的期限时长默认为5s，可以看出内核对读请求是十分偏心的，其实不仅如此，在deadline调度器中，还定义了一个starved和writes_starved，writes_starved默认为2，可以理解为写请求的饥饿线，内核总是优先处理读请求，starved表明当前处理的读请求批数，只有starved超过了writes_starved后，才会去考虑写请求。因此，假如一个写请求的期限已经超过，该请求也不一定会被立刻响应，因为读请求的batch还没处理完，即使处理完，也必须等到starved超过writes_starved才有机会被响应。为什么内核会偏袒读请求？这是从整体性能上进行考虑的。读请求和应用程序的关系是同步的，因为应用程序要等待读取的内容完毕，才能进行下一步工作，因此读请求会阻塞进程，而写请求则不一样，应用程序发出写请求后，内存的内容何时写入块设备对程序的影响并不大，所以调度器会优先处理读请求。
默认情况下，读请求的超时时间是500ms，写请求的超时时间是5s。


echo "---------------------------------------------------------------"
sysctl vm.extra_free_kbytes
sysctl vm.min_free_kbytes
sysctl vm.overcommit_memory
sysctl vm.drop_caches
sysctl vm.zone_reclaim_mode
sysctl vm.max_map_count
sysctl vm.dirty_background_ratio
sysctl vm.dirty_ratio
sysctl vm.dirty_writeback_centisecs
sysctl vm.page-cluster
sysctl vm.swappiness

su - admin -c 'ulimit -n'
cat /sys/block/$DISK/queue/scheduler

if [ -d ${HOME}/tmpfs ] ; then
    echo "tmpfs exist, do nothing."
else
    ln -s /dev/shm ${HOME}/tmpfs
    echo "create tmpfs ok"
fi

```

