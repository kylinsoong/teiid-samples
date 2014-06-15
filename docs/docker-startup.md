# Installation

[10-minute tutorial and installation](http://www.docker.com/tryit/)

For install docker to Red Hat Linux 6.5:

~~~
sudo yum -y install docker-io
~~~

# Dockerizing Applications

### Down ubuntu from the docker registry server

~~~
docker pull ubuntu:14.04
~~~

### Docker run with commands

~~~
docker run ubuntu:14.04 echo "Kylin Soong"
docker run ubuntu:14.04 uname -a
docker run ubuntu:14.04 ifconfig
docker run ubuntu:14.04 hostname
~~~

> The `docker run` will avtive the ubuntu container, correspondingly, the ubuntu container will stop after the commands finished. The following stripts show run these commands in a bash, then exit the bash:

~~~
[root@kylin docker]# docker run -t -i ubuntu:14.04 bash
root@f019af40c291:/# echo "Kylin Soong"
Kylin Soong
root@f019af40c291:/# uname -a
Linux f019af40c291 2.6.32-431.17.1.el6.x86_64 #1 SMP Fri Apr 11 17:27:00 EDT 2014 x86_64 x86_64 x86_64 GNU/Linux
root@f019af40c291:/# ifconfig
eth0      Link encap:Ethernet  HWaddr fe:a8:f3:f6:6e:53  
          inet addr:172.17.0.2  Bcast:0.0.0.0  Mask:255.255.0.0
          inet6 addr: fe80::fca8:f3ff:fef6:6e53/64 Scope:Link
          UP BROADCAST RUNNING  MTU:1500  Metric:1
          RX packets:6 errors:0 dropped:0 overruns:0 frame:0
          TX packets:6 errors:0 dropped:0 overruns:0 carrier:0
          collisions:0 txqueuelen:1000 
          RX bytes:468 (468.0 B)  TX bytes:468 (468.0 B)

lo        Link encap:Local Loopback  
          inet addr:127.0.0.1  Mask:255.0.0.0
          inet6 addr: ::1/128 Scope:Host
          UP LOOPBACK RUNNING  MTU:16436  Metric:1
          RX packets:0 errors:0 dropped:0 overruns:0 frame:0
          TX packets:0 errors:0 dropped:0 overruns:0 carrier:0
          collisions:0 txqueuelen:0 
          RX bytes:0 (0.0 B)  TX bytes:0 (0.0 B)

root@f019af40c291:/# hostname
f019af40c291
root@f019af40c291:/# exit
exit
[root@kylin docker]# 
~~~

> Note that, the above script show docker container active a ubuntu bash, execute the commends just like previous `docker run`.


The following show useing docker to run the container and put it in the background:

~~~
[root@kylin docker]# docker run -d ubuntu:14.04 sh -c "while true; do echo Kylin Soong; sleep 1; done"
2b4ff4f9584fca157abb7119b6fb6ab18355698270729aef84147c606f691325
~~~

The `docker ps` command queries the Docker daemon for information about all the container it knows about:

~~~
[root@kylin docker]# docker ps
CONTAINER ID        IMAGE               COMMAND                CREATED             STATUS              PORTS               NAMES
2b4ff4f9584f        ubuntu:14.04        sh -c 'while true; d   3 minutes ago       Up 3 minutes                            high_lalande
~~~

The `docker logs` command output the echo output:

~~~
[root@kylin docker]# docker logs high_lalande
Kylin Soong
Kylin Soong
Kylin Soong
Kylin Soong
~~~

The `docker stop` command to stop the container:

~~~
[root@kylin docker]# docker stop 2b4ff4f9584f
2b4ff4f9584f
~~~

