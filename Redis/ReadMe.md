#Redis
## Redis란 무엇인가?
레디스(Redis)는 고성능 key-value 저장소로서 List, Hash, Set, SortedSet 등 여러 형식의 자료구조를 지원하는 In Memory형태의 NoSQL입니다.  
메모리에 상주하면서 RDBMS의 캐시 솔루션으로서 주로 사용되며 라인, 삼성전자, 네이버, Stackoverflow, 인스타그램 등 여러 IT 대기업에서도 사용하는 검증된 오픈소스 솔루션입니다.  
해당 Redis C언어 기반으로 작성이 되어있습니다.  
Redis는 싱글쓰레드로 실행이 된다.
## Key/Value Store
Redis는 기본적으로 Key/Value 형태이다.  
특정 키 값에 값을 저장하는 구조로 되어 있으며 기본적인 PUT/GET Operation을 지원한다.
이 모든데이터는 기본으로 메모리에 저장이 되어서 매우 빠른속도로 Read/Write가 제공이된다.  
전체 저장 가능한 데이타 용량은 물리적인 메모리 크기를 넘어설 수 있다.
하지만 이로인해 OS의 [Swapping](https://k39335.tistory.com/36) 영역등을 사용하여 확장이 되지만 속도는 매우 급격하게 저하가 된다.  
![Key/Value](https://t1.daumcdn.net/cfile/tistory/1377533B4FFBD9D105)
## 다양한 데이타 타입
단순한 메모리 기반의 Key/Value 라면 이미 memcached가 있습니다.  
하지만 왜 Redis가 유행을 할까요?
Redis는 Key/Value구조이기는 하지만 단순히 Object를 저장하는것이 아닌 자료구조도 저장이 가능하기 때문에 강력합니다.
### String
일반적인 문자열로 최대 512MB까지 저장이 가능하며, Text 뿐만 아닌 Integer나 Jpeg같은 binary도 저장이 가능하다.
### Set
Set은 String의 집합체입니다. 여러개의 값을 하나의 Value 내에 넣을수 있습니다.  
Set간의 연산을 지원을 하는데, 집합인 만큼 교집합, 합집합, 차집합을 매우 빠른 시간내에 추출이 가능합니다.
### Hashs
Hash는 Vlaue내에 Field/String Value 쌍으로 이루어진 테이블을 저장하는 데이터 구조체입니다.
### List
List는 String들의 집합으로 저장되는 데이타 형태는 set과 유사하지만, 일종의 양방향 Linked List라고 생각하면 된다.  
List 앞과 뒤에서 PUSH/POP 연산을 이용해서 데이타를 넣거나 뺄 수 있고, 지정된 INDEX 값을 이용하여 지정된 위치에 데이타를 넣거나 뺄 수 있다.
 
아래는 위에서 설명한 내용의 이미지화이다
![Redis 데이터 타입](https://t1.daumcdn.net/cfile/tistory/202A37504FFBDA6026)
## Persistence(영속화)
Redis는 Disk에 저장하여 영속화가 가능합니다.
Memcached의 경우 메모리에만 데이터를 저장하기 때문에 서버가 Shutdown 된후에 데이터는 복구가 불가능하지만, Redis는 서버가 Shutdown된 후 Start가 되더라도,  
Disk에 저장해놓은 데이터를 다시 메모리에 올릴수가 있습니다.
Redis에서는 2가지 방식의 저장방법을 제공합니다.
### Snapshotting(RDB)
메모리에 있는 내용을 Disk에 전체를 옮겨 담는 방식이다.
#### 방식
1. SAVE 방식  
    Save방식은 Blocking 방식으로 순간적으로 Redis의 모든 동작을 정지시키고, 그때 Disk에 저장합니다.
2. BGSAVE
    BGSAVE는 논-블로킹 방식으로 별도의 Process를 띄운후, 명령어 수행 당시의 메모리 Snapshot을 Disk에 저장하며, 저장 순간에 Redis는 동작을 멈추지 않고 정상적으로 동작한다.
#### 장단점
1. 장점
    메모리를 Snapshot을 작성하여서 재기동시 그대로 Load하면 빠른 속도로 진행된다.
2. 단점
    Snapshot을 추출하는데 시간이 오래 걸리며, Snapshot이후의 데이터는 손실된다.
### AOF(Append On File)
AOF는 Binary Log처럼 Write/Update/Delete 연산 자체를 모두 Log 파일에 기록한다.
서버가 재시작 될때 해당 operation을 순차적으로 실행하여 데이터를 복구한다.
operation 이 발생할때 마다 매번 기록하기 때문에, RDB 방식과는 달리 특정 시점이 아니라 항상 현재 시점까지의 로그를 기록할 수 있으며, 기본적으로 non-blocking 이다.
## 확장방법
Redis는 확장의 방법을 여러가지 제공을한다.
### Replication
Master/Slave개념으로 Insert,Update,Delete는 Master에 하며 Slave에는 복제를 진행한다.
이것은 거의 실시간이며 미리세컨드 (극 낮음)으로 Copy가 진행될것이다
### Sharding
예전에 Redis가 클러스터링을 제공하지 않았었다.  
만약 데이터의 용량이 늘어나면 어떤 방식으로 Redis를 확장을 했을까?  
일반적으로 Sharding이라는 아키텍쳐를 사용하여 확장을 하였다.  
여러가지의 Redis 서버를 구현 후 데이터를 구역별로 나뉘어서 짤라 저장하는것이다.  
데이터 분산에 대한 통제권은 Client가 갖고있으며 Client에서 어플리케이션 로직을 수행한다.  
![Sharding](https://t1.daumcdn.net/cfile/tistory/1866934D4FFBDAB514)
### Cluster
Redis를 클러스터로 묶어서 가용성 및 안정성있는 캐시 매니져로서 사용하고 있습니다.  
Single Instance로서 레디스를 사용할 때는 Sharding이나 Topology로서 커버해야했던 부분을 Clustering을 이용함으로서 어플리케이션을 설계하는 데 좀 더 수월해졌다고 볼 수 있습니다.
![Cluster](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=http%3A%2F%2Fcfile7.uf.tistory.com%2Fimage%2F99ACC94D5CF28B282E0D0D)
## Expriation
Redis는 데이터의 생명주기를 설정하여 삭제를 할 수 있다.
해당 방법은 2가지 방법이 있다 (Active/Passive)
### Active
Active 방식은 Client가 Expired된 데이터에 접근시 그때 지우는 방법이다.
### Passive
Passive 방식은 주기적으로 key들을 Random으로 100개만 스캔하여 지우는 방식이다.


#참고한 자료
https://bcho.tistory.com/654




# REDIS CLUSTER
Cluster 를 이용해서 샤딩을 하고, replica 설정이 가능하다.  
컴파일 기준으로 
http://download.redis.io/releases/redis-6.0.8.tar.gz?_ga=2.206298003.1639891242.1600775998-1422365930.1595511361  
다운로드 후, make && make install을 진행한다음에  
vi ./redis.conf를 하면은 포트 수정이 가능하고, 그 밑에 cluster-enable이 주석이 되어있는데 해제해주면은 클러스터 적용이 가능하다.  
그리고 클러스터 생성은 아래의 명령어로 가능하다.  
redis-cli --cluster create ip:port ip:port ip:port  
그리고 replica를 만들려면 --cluster-replicas 갯수를 하면 아래와 같이 뜬다  
```java
➜  src ./redis-cli --cluster create 127.0.0.1:6300 127.0.0.1:6301 127.0.0.1:6302 127.0.0.1:7300 127.0.0.1:7301 127.0.0.1:7302 --cluster-replicas 1
>>> Performing hash slots allocation on 6 nodes...

Master[0] -> Slots 0 - 5460
Master[1] -> Slots 5461 - 10922
Master[2] -> Slots 10923 - 16383
Adding replica 127.0.0.1:7301 to 127.0.0.1:6300
Adding replica 127.0.0.1:7302 to 127.0.0.1:6301
Adding replica 127.0.0.1:7300 to 127.0.0.1:6302
>>> Trying to optimize slaves allocation for anti-affinity
[WARNING] Some slaves are in the same host as their master
M: 0f6619d731e56c09048f4ab432c3670861d31136 127.0.0.1:6300
   slots:[0-5460] (5461 slots) master
M: a2889b0c93c75afe9e18d7df660b41cb3c3d08e6 127.0.0.1:6301
   slots:[5461-10922] (5462 slots) master
M: 79b39c5961a137977c0ccb6d127ece51483c2adc 127.0.0.1:6302
   slots:[10923-16383] (5461 slots) master
S: d2e609a5789eea76f2d351036e3440c2e53bc386 127.0.0.1:7300
   replicates a2889b0c93c75afe9e18d7df660b41cb3c3d08e6
S: ae90d19c86b0f18d09965a5ac463514bff8d53f3 127.0.0.1:7301
   replicates 79b39c5961a137977c0ccb6d127ece51483c2adc
S: 5583ef36834624ad3747e6c3d4c5c90f01828296 127.0.0.1:7302
   replicates 0f6619d731e56c09048f4ab432c3670861d31136

```
yes를 누르면 클러스터링이 된다.  
그리고 redis-cli -c -p 로 접속하면 된다.  
cluster가 잘 되는지 명령어를 떄리면 아래와 같이 뜬다  
```java
➜  src redis-cli -c -p 6300
127.0.0.1:6300> set a 1
-> Redirected to slot [15495] located at 127.0.0.1:6302
OK
127.0.0.1:6302> set b 1
-> Redirected to slot [3300] located at 127.0.0.1:6300
OK
127.0.0.1:6300> set c 1
-> Redirected to slot [7365] located at 127.0.0.1:6301
OK
127.0.0.1:6301> set d 1
-> Redirected to slot [11298] located at 127.0.0.1:6302
OK
// 6300
127.0.0.1:6300> keys *
1) "b"
// 6301
127.0.0.1:6301> keys *
1) "c"
// 6302
127.0.0.1:6302> keys *
1) "a"
2) "d"
```

만약에 item_{item_id}_{comment}의 경우 item_id와 같은애들은 같은 샤드에 저장하려면은 hashtag 라는 기능을 쓰면 된다  
## hashtag
hashtag는 {} 사이에 데이터를 넣어주면 된다  
예를들어 1번의 아이템의 comment들은 같은 shard에 구성하려면 아래와 같이  
item_{1}_1,item_{1}_2,item_{1}_3,item_{1}_4,item_{1}_5  
아래는 예제다
```java
127.0.0.1:6300> set item_{1}_1 1
-> Redirected to slot [9842] located at 127.0.0.1:6301
OK
127.0.0.1:6301> set item_{1}_2 1
OK
127.0.0.1:6301> set item_{1}_3 1
OK
127.0.0.1:6301> set item_{1}_4 1
OK
127.0.0.1:6301> set item_{3}_1 1
-> Redirected to slot [1584] located at 127.0.0.1:6300
OK
127.0.0.1:6300> set item_{3}_2 1
OK
127.0.0.1:6300> set item_{3}_3 1
OK
127.0.0.1:6300> set item_{2}_3 1
-> Redirected to slot [5649] located at 127.0.0.1:6301
OK
127.0.0.1:6301> set item_{4}_3 1
-> Redirected to slot [14039] located at 127.0.0.1:6302
OK
127.0.0.1:6302> set item_{4}_a5 1
OK
127.0.0.1:6302> set item_{4}_6 1
OK

// 6300
127.0.0.1:6300> keys *
1) "item_{3}_3"
2) "item_{3}_2"
3) "item_{3}_1"
// 6301
127.0.0.1:6301> keys *
1) "item_{1}_2"
2) "item_{1}_3"
3) "item_{1}_4"
4) "item_{2}_3"
5) "item_{1}_1"
// 6302
127.0.0.1:6302> keys *
1) "item_{4}_6"
2) "item_{4}_a5"
3) "item_{4}_3"
```

## 만약에 마스터를 죽였을경우?
마스터를 죽이면은 어느정도 시간이 지난다음에 slave가 master가 승격이 되며, 기존 master가 살아나면 기존 master는 slave로 구성된다  
### 슬레이브 로그
```java
3897:S 22 Sep 2020 21:16:28.646 # Connection with master lost.
3897:S 22 Sep 2020 21:16:28.646 * Caching the disconnected master state.
3897:S 22 Sep 2020 21:16:29.045 * Connecting to MASTER 127.0.0.1:6301
3897:S 22 Sep 2020 21:16:29.045 * MASTER <-> REPLICA sync started
3897:S 22 Sep 2020 21:16:29.045 # Error condition on socket for SYNC: Operation now in progress
3897:S 22 Sep 2020 21:16:46.435 # Error condition on socket for SYNC: Operation now in progress
3897:S 22 Sep 2020 21:16:47.458 * Connecting to MASTER 127.0.0.1:6301
3897:S 22 Sep 2020 21:16:47.458 * MASTER <-> REPLICA sync started
3897:S 22 Sep 2020 21:16:47.458 # Error condition on socket for SYNC: Operation now in progress
3897:S 22 Sep 2020 21:16:47.970 * Marking node a2889b0c93c75afe9e18d7df660b41cb3c3d08e6 as failing (quorum reached).
3897:S 22 Sep 2020 21:16:47.971 # Start of election delayed for 663 milliseconds (rank #0, offset 1362).
3897:S 22 Sep 2020 21:16:47.971 # Cluster state changed: fail
3897:S 22 Sep 2020 21:16:48.480 * Connecting to MASTER 127.0.0.1:6301
3897:S 22 Sep 2020 21:16:48.481 * MASTER <-> REPLICA sync started
3897:S 22 Sep 2020 21:16:48.481 # Error condition on socket for SYNC: Operation now in progress
3897:S 22 Sep 2020 21:16:48.689 # Starting a failover election for epoch 7.
3897:S 22 Sep 2020 21:16:48.691 # Failover election won: I'm the new master.
3897:S 22 Sep 2020 21:16:48.691 # configEpoch set to 7 after successful failover
3897:M 22 Sep 2020 21:16:48.691 * Discarding previously cached master state.
3897:M 22 Sep 2020 21:16:48.691 # Setting secondary replication ID to d8bc351892e69da60406e78f439992423c6c8571, valid up to offset: 1363. New replication ID is 18d91bbdbb5c0a46281711443ffc94b4e97fb0fd
3897:M 22 Sep 2020 21:16:48.691 # Cluster state changed: ok
```
기존 slave인 7300은 master가 된다  
redis-cli -c -p 들어간 후 cluster nodes 치면은 나온다.  
```java
➜  src redis-cli -c -p 7300
127.0.0.1:7300> cluster nodes
d2e609a5789eea76f2d351036e3440c2e53bc386 127.0.0.1:7300@17300 myself,master - 0 1600777523000 7 connected 5461-10922
ae90d19c86b0f18d09965a5ac463514bff8d53f3 127.0.0.1:7301@17301 slave 79b39c5961a137977c0ccb6d127ece51483c2adc 0 1600777523757 3 connected
5583ef36834624ad3747e6c3d4c5c90f01828296 127.0.0.1:7302@17302 slave 0f6619d731e56c09048f4ab432c3670861d31136 0 1600777522000 1 connected
0f6619d731e56c09048f4ab432c3670861d31136 127.0.0.1:6300@16300 master - 0 1600777523000 1 connected 0-5460
79b39c5961a137977c0ccb6d127ece51483c2adc 127.0.0.1:6302@16302 master - 0 1600777524786 3 connected 10923-16383
a2889b0c93c75afe9e18d7df660b41cb3c3d08e6 127.0.0.1:6301@16301 master,fail - 1600776989964 1600776987000 2 disconnected
```  
그리고 기존 6301번을 살리면은 slave가 된다  
```java
d2e609a5789eea76f2d351036e3440c2e53bc386 127.0.0.1:7300@17300 myself,master - 0 1600777569000 7 connected 5461-10922
ae90d19c86b0f18d09965a5ac463514bff8d53f3 127.0.0.1:7301@17301 slave 79b39c5961a137977c0ccb6d127ece51483c2adc 0 1600777570000 3 connected
5583ef36834624ad3747e6c3d4c5c90f01828296 127.0.0.1:7302@17302 slave 0f6619d731e56c09048f4ab432c3670861d31136 0 1600777569000 1 connected
0f6619d731e56c09048f4ab432c3670861d31136 127.0.0.1:6300@16300 master - 0 1600777570000 1 connected 0-5460
79b39c5961a137977c0ccb6d127ece51483c2adc 127.0.0.1:6302@16302 master - 0 1600777571843 3 connected 10923-16383
a2889b0c93c75afe9e18d7df660b41cb3c3d08e6 127.0.0.1:6301@16301 slave d2e609a5789eea76f2d351036e3440c2e53bc386 0 1600777570823 7 connected
```

## cluster lua script
cluster는 key로 기준으로 cluster가 된다.  
첫번쨰 키로 slot이 계산된다 한다.  
만약 자기에 맞지않은 key일경우 에러가 표시된다.  
아래가 그런경우다    
```java
127.0.0.1:7300> EVAL "return redis.call('set','key','value')" 0
(error) ERR Error running script (call to f_1b3037549b5751fc7635c7d7c556cf458cf50c47): @user_script:1: @user_script: 1: Lua script attempted to access a non local key in a cluster node
127.0.0.1:7300> EVAL "return redis.call('set','key1','value')" 0
OK
```
하지만 파라미터로 넘겨서 실행하면은 문제가 없다  
```java
127.0.0.1:6300> EVAL "return redis.call('set',KEYS[1],'value')" 1 'E'
-> Redirected to slot [6241] located at 127.0.0.1:7300
OK
127.0.0.1:7300> EVAL "return redis.call('set',KEYS[1],'value')" 1 'F'
OK
127.0.0.1:7300> EVAL "return redis.call('set',KEYS[1],'value')" 1 'G'
-> Redirected to slot [14371] located at 127.0.0.1:6302
OK
```