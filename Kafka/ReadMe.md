# 아파치 Kafka
## Kafka란?
- Apache Kafka는 LinkedIn에서 개발된 분산 메시징 시스템으로써 2011년에 오픈소스로 공개되었다.  
- 대용량의 실시간 로그처리에 특화된 아키텍처 설계를 통하여 기존 메시징 시스템보다 우수한 TPS를 보여주고 있다.  
- Kafka는 pub-sub모델의 메세지 큐이고, 분산환경에 특화되어 설계되어 있다는 특징을 가짐으로써, 기존의 RabbitMQ와 같은 다른 메세지큐와의 성능 차이가 난다.   
- 그 외에도 클러스터 구성, fail-over, replication와 같은 여러 가지 특징들을 가지고 있다.
## 메시징 모델?
메시징 모델은 크게 큐(queue) 모델과 발행-구독(publish-subscribe) 모델로 나뉜다
1. 큐 모델
    - 큐 모델은 메시지가 쌓여있는 큐로부터 메시지를 가져와서 consumer pool에 있는 consumer 중 하나에 메시지를 할당하는 방식이다 (push)
2. pub-sub 모델  
    - pub-sub은 메세지를 특정 수신자에게 직접적으로 보내주는 시스템이 아니다.
    - publisher는 메세지를 topic을 통해서 카테고리화 한다.  
    - 분류된 메세지를 받기를 원하는 receiver는 그 해당 topic을 구독(subscribe)함으로써 메세지를 읽어 올 수 있다.  
    즉, publisher는 topic에 대한 정보만 알고 있고, 마찬가지로 subscriber도 topic만 바라본다.  
    publisher 와 subscriber는 서로 모르는 상태다.
## kafka의 기본 구성 요소와 동작
Kafka는 **발행-구독(publish-subscribe)** 모델을 기반으로 동작하며 크게 producer, consumer, broker로 구성된다.  
앞서 설명한 publisher는 producer가 되고, subscriber는 cunsumer가 된다.  
![카프카 구성요소 이미지](https://t1.daumcdn.net/cfile/tistory/253BF244550914E21A)

Kafka의 broker는 topic을 기준으로 메시지를 관리한다.

Kafka는 확장성(scale-out)과 고가용성(high availability)을 위하여 broker들이 클러스터로 구성되어 동작하도록 설계되어있다.  
심지어 broker가 1개 밖에 없을 때에도 클러스터로써 동작한다. 
클러스터 내의 broker에 대한 분산 처리는 아래의 그림과 같이 Apache ZooKeeper가 담당한다.

![카프카 주키퍼 구성](https://t1.daumcdn.net/cfile/tistory/270D49435509151E2A)

### 기본적인 동작 과정  
1. Producer는 특정 topic의 메시지를 생성한 뒤 해당 메시지를 broker에 전달한다.
2. Broker가 전달받은 메시지를 topic별로 분류하여 쌓아놓는다.
3. 해당 topic을 구독하는 consumer들이 메시지를 가져가서 처리하게 된다.

## 기존 메시징 시스템과의 차이점
1. 기존 범용 MQ보다는 TPS가 우수, 특화된 시스템이기에 기존 범용 MQ보다는 제공되는 다양한 시스템이 없음.
2. 분산 시스템으로 설계되었기 때문에, 기존 메시징 시스템에 비해 분산 및 복제 구성을 쉽게 가능.
3. JMS API, AMQP 프로토콜을 사용하지않고 TCP를 사용하기에 프로토콜에 의한 오버헤드 감소
4. 기존 MQ는 Producer가 Broker에게 다수의 메시지를 전송할 때 각 메시지를 전송했으나 Kafka는 다수의 메시지를 batch형태로 전송할 수 있어 TCP/IP 라운드 트립 횟수 감소
5. 메시지를 **기본적으로** 메모리에 저장하는 기존 MQ에 비해 파일 시스템에 저장
    - 설정없이 영속성이 보장이된다
    - 기존 MQ는 처리되지 않고 남아있는 메시지 수가 많을수록 성능 저하가 있었으나 Kafka는 그렇지 않다.
    - 많은 메시지를 쌓아둘 수 있기 때문에 실시간 처리뿐만 아니라 주기적인 batch작업에 사용할 데이터를 쌓아두는 용도로도 사용이 가능.
    - 기존 MQ는 broker가 Consumer에게 메시지 push 해주는 방식에 비해, Kafka는 반대로 직접 가져온다. 따라서 Consumer는 자기가 처리 가능한 양 만큼만 가져올 수 있다.
### 기존 MQ와 성능 비교
#### Producer 성능 비교
![Producer 성능 비교](https://t1.daumcdn.net/cfile/tistory/23441B445509177A1F)
#### Consumer 성능 비교
![Consumer 성능 비교](https://t1.daumcdn.net/cfile/tistory/217D7945550917BD0B)
### 파일 시스템을 활용한 고성능 디자인
![카프카의 파일시스템 퍼포먼스](https://t1.daumcdn.net/cfile/tistory/264EE6445509188C1A)  
Kafka는 기존 메시징 시스템과는 달리 메시지를 메모리대신 파일 시스템에 쌓아두고 관리한다.  
기존 메시징 시스템에서는 파일 시스템은 메시지의 영속성을 위해서 성능 저하를 감수하면서도 어쩔 수 없이 사용해야하는 애물단지 같은 존재였다.  
그러나 Kafka는 이런 편견을 깨고 파일 시스템을 메시지의 **주 저장소로** 사용하면서도 기존의 메시징 시스템보다 뛰어난 성능을 보여준다.

일반적으로 하드디스크는 메모리에 비하여 느린것이 당연하다.   
하지만 특정한 조건일 경우에는 하드디스크가 메모리보다 빠른 성능을 보인다.  
그러한 특정한 조건은 디스크의 순차읽기와 메모리 랜덤읽기일 때 발생이 된다.  

Kafka는 메모리에 별도의 캐시를 구현하지 않고 OS의 페이지 캐시에 이를 모두 위임한다.
OS가 알아서 서버의 유휴 메모리를 페이지 캐시로 활용하여 앞으로 필요할 것으로 예상되는 메시지들을 미리 읽어들여(readahead) 디스크 읽기 성능을 향상 시킨다.

Kafka의 메시지는 하드디스크로부터 순차적으로 읽혀지기 때문에 하드디스크의 랜덤 읽기 성능에 대한 단점을 보완함과 동시에 OS 페이지 캐시를 효과적으로 활용할 수 있다.

메시지를 메모리에 저장하지 않기 때문에 메시지가 JVM 객체로 변환되면서 크기가 커지는 것을 방지할 수 있고 JVM의 GC로인한 성능저하 또한 피할 수 있다.

Kafka 프로세스가 직접 캐시를 관리하지 않고 OS에 위임하기 때문에 프로세스를 재시작 하더라도 OS의 페이지 캐시는 그대로 남아있기 때문에 프로세스 재시작 후 캐시를 워밍업할 필요가 없다는 장점도 있다.

### 네트워크 전송할 때의 zero-copy기법
Kafka에서는 파일 시스템에 저장된 메시지를 네트워크를 통해 consumer에게 전송할 때 zero-copy기법을 사용하여 데이터 전송 성능을 향상시켰다.  
일반적으로 파일 시스템에 저장된 데이터를 네트워크로 전송할 땐 아래와 같이 커널모드와 유저모드 간의 데이터 복사가 발생하게 된다.  
![DMA Copy, CPU Copy](https://t1.daumcdn.net/cfile/tistory/2623073E550918D608)

유저모드로 카피된 데이터를 어플리케이션에서 처리한 뒤 처리된 데이터를 네트워크로 전송한다면 위의 그림과 같이 커널모드와 유저모드 간의 데이터 복사는 당연히 필요하다.  
그러나 어플리케이션에서의 별도 처리 없이 파일 시스템에 저장된 데이터 그대로 네트워크로 전송만 한다면 커널모드와 유저모드 간의 데이터 복사는 불필요한 것이 된다.

Zero-copy 기법을 사용하면 위에서 언급한 커널모드와 유저모드 간의 불필요한 데이터 복사를 피할 수 있다.  
이 기법을 사용하면 아래와 같이 파일 시스템의 데이터가 유저모드를 거치지 않고 곧바로 네트워크로 전송된다.  
[벤치마크 결과](https://www.ibm.com/developerworks/library/j-zerocopy/#N1019B) 에 따르면 zero-copy를 사용한 경우가 그렇지 않은 경우보다 전송 속도가 2-4배 빠른 것으로 나타났다.

![Kernel Socket Send, Recv](https://t1.daumcdn.net/cfile/tistory/2157B43D5509190B21)
## Topic과 Partition
    - 메세지는 topic으로 분류되고, topic은 여러개의 파티션으로 나눠 질 수 있다.   
    - 파티션내의 한 칸은 로그라고 불린다. 데이터는 한 칸의 로그에 순차적으로 append된다.  
    - 메세지의 상대적인 위치를 나타내는게 offset이다.

![Topic,Partition,Log](https://miro.medium.com/max/1128/0*dEeuSOb7Z8K7---q.png)
### 왜 Topic은 여러개의 파티션으로 나눌까?
    - 하나의 topic에 하나의 파티션만 가진 상황과 하나의 topic에 여러개의 파티션을 가진 두가지를 비교해 보면 된다.   
     - 메세지는 카프카의 해당 토픽에 쓰여진다. 쓰는 과정, 읽는 과정도 시간이 소비된다.  
    - 만약 몇 천건의 메세지가 하나의 파티션에 순차적으로 append가 진행이 된다면은 당연히 느리게 append가 진행 될것 이다.
    - 만약 여러개의 파티션을 만들어 분산하여 저장할경우 병렬로 처리가 될 것이니 속도는 훨씬 빠르게 될것이다.
    - **(주의)한 번 늘린 파티션은 절대로 줄일 수 없기 때문에 운영중에, 파티션을 늘려야 하는건 충분히 고려해봐야한다.**
#### 어떤 방식으로 쓰여지게 되는가?
![Partition의 분산](https://t1.daumcdn.net/cfile/tistory/2558363F5509180F25)  
Producer가 메시지를 실제로 어떤 partition으로 전송할지는 사용자가 구현한 partition 분배 알고리즘에 의해 결정된다.  
예를 들어 라운드-로빈 방식의 partition 분배 알고리즘을 구현하여 각 partition에 메시지를 균등하게 분배하도록 하거나, 메시지의 키를 활용하여 알파벳 A로 시작하는 키를 가진 메시지는 P0에만 전송하고, B로 시작하는 키를 가진 메시지는 P1에만 전송하는 형태의 구성도 가능하다.
좀 더 복잡한 예로써 사용자 ID의 CRC32값을 partition의 수로 modulo 연산을 수행하여(CRC32(ID) % partition의 수) 동일한 ID에 대한 메시지는 동일한 partition에 할당되도록 구성할 수도 있다.
## Consumer Group이란?
Kafka의 partition은 consumer group당 오로지 하나의 consumer의 접근만을 허용하며, 해당 consumer를 partition owner라고 부른다.  
따라서 동일한 consumer group에 속하는 consumer끼리는 동일한 partition에 접근할 수 없다.

한 번 정해진 partition owner는 broker나 consumer 구성의 변동이 있지 않는한 계속 유지된다.  
Consumer가 추가/제거되면 추가/제거된 consumer가 속한 consumer group 내의 consumer들의 partition 재분배(rebalancing)가 발생하고  
broker가 추가/제거되면 전체 consumer group에서 partition 재분배가 발생한다.

Consumer group을 구성하는 consumer의 수가 partition의 수보다 작으면 하나의 consumer가 여러 개의 partition을 소유하게 되고,  
반대로 consumer의 수가 partition의 수보다 많으면 여분의 consumer는 메시지를 처리하지 않게되므로 partition 개수와 consumer 수의 적절한 설정이 필요하다.

### 예를들어보자!
    - Partition(4개) : Consumer(2개) => 컨슈머당 2개의 파티션을 담당  
    - Partition(4개) : Consumer(4개) => 컨슈머당 1개의 파티션을 담당  
    - Partition(4개) : Consumer(3개) => 컨슈머 1개는 2개를 처리하고 나머지는 1개만처리한다
### 순차적으로 처리가 가능한가?
이처럼 하나의 consumer에 의하여 독점적으로 partition이 액세스 되기 때문에 동일 partition 내의 메시지는 partition에 저장된 순서대로 처리된다.    
만약 특정 키를 지닌 메시지가 발생 시간 순으로 처리되어야 한다면 partition 분배 알고리즘을 적절하게 구현하여 특정 키를 지닌 메시지는 동일한 partition에 할당되어 단일 consumer에 의해 처리되도록 해야한다.  
그러나 다른 partition에 속한 메시지의 순차적 처리는 보장되어 있지 않기 때문에, 특정 topic의 전체 메시지가 발생 시간 순으로 처리되어야 할 경우 해당 topic이 하나의 partition만을 가지도록 설정해야 한다.
## 그럼 도대체 Consumer Group이 존재하는 진짜? 이유는 뭘까?
컨슈머 그룹은 하나의 topic에 대한 책임을 갖고 있다.
같은 그룹내의 어떤 컨슈머가 down된다면, 그 파티션에 대해서는 소비가 이뤄질수가 없게된다.
이런 상황이 오면 (Rebalance)된 상황이라고 한다. 
리밸런스가 된 상황이면, 파티션 재조정을 통해서 다른 컨슈머가 down된 컨슈머를 대신하여 소비를 이어서 하게된다.  
물론 파티션의 offset 정보를 그룹내에서 서로간의 공유하고 있기 때문에, down되기 직전의 offset위치를 알고 그 다음부터 소비하면 문제가 없어지는 것이다.

## Zookeeper, Broker
broker는 카프카의 서버를 칭한다.  
브로커 설정을 함으로써 동일한 노드내에서 여러개의 broker서버를 띄울 수도 있다.  
zookeeper는 이러한 분산 메세지 큐의 정보를 관리해 주는 역할을 한다.  
kafka를 띄우기 위해서는 zookeeper가 반드시 실행되어야 한다.

## Replication
local에 broker3대를 띄우고(replica-factor=3)로 복제되는 경우를 살펴보자.
복제는 수평적으로 스케일 아웃이다.  
broker 3대에서 하나의 서버만 leader가 되고 나머지 둘은 follower 가 된다.  
producer가 메세지를 쓰고, consumer가 메세지를 읽는 건 오로지 leader가 전적으로 역할을 담당한다.
###나머지 follower들의 역할은?
나머지 follower들은 leader와 싱크를 항상 맞춘다. 
해당 option이 있다. 혹시나 leader가 죽었을 경우, 나머지 follower중에 하나가 leader로 선출되어서 메세지의 쓰고/읽는 것을 처리한다.