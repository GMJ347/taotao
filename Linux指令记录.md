## nginx

### 安装

- ps -ef | grep nginx
  - UID： 说明该程序被谁拥有
  - PID：就是指该程序的 ID
  - PPID： 就是指该程序父级程序的 ID 
  - C： 指的是 CPU 使用的百分比
  - STIME： 程序的启动时间
  - TTY： 指的是登录终端
  - TIME : 指程序使用掉 CPU 的时间
  - CMD： 下达的指令
- su - root
- userdel -r taotao
- adduser taotao
- passwd taotao
- cd /home/taotao
- pwd
- ifconfig
- ll
- tar xvf nginx.tar.gz
- rm -rf nginx.tar.gz
- ./configure --prefix=/opt/nginx --sbin-path=/user/bin/nginx
- make && make install
- cd /user/bin
- ./nginx
- ps -ef | grep nginx
- ./nginx -s stop
- ./nginx -s reload

### yum配置nginx安装环境

- centos6.8 官方已经不再提供yum镜像源，自己绑定三方镜像源https://my.oschina.net/u/4340589/blog/4817423
- yum -y install pcre-devel
- yum -y install openssl openssl-devel

### 使用nginx

- cd /user/bin
- ./nginx
- ps -ef | grep nginx
- ./nginx -s stop
- ./nginx -s reload
- service iptables stop 关闭防火墙

### FastDFS

- ll /etc/init.d/ | grep fdfs
- cd /etc/fdfs
- cp tracker.conf.sample tracker.conf
- vim tracker.conf
- mkdir -p /taotao/fdfs/tracker
- /etc/init.d/fdfs_trackered start
- cp storage.conf.sample storage.conf
- vim storage.conf
- mkdir -p /taotao/fdfs/stroage
- /etc/init.d/fdfs_storaged start
- cp client.conf.sample client.conf
- vim client.conf
- /usr/bin/fdfs_upload_file client.conf /tmp/1.png
- pkill -9 fdfs


