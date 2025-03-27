## 동시성(**Concurrency**)이란?

- 동시성이란 <code style="color : purple">**빠른 작업 전환을 통해 여러 작업이 동시에 실행되는 것 처럼 보이는 성질입니다.**</code>
- 주로 많은 사용자의 요청을 처리하는 웹서버에 사용됩니다.
    - 요청을 줄 세우고 하나 요청받고 하나 끝난 후 다음 요청을 처리하는 것 보다 작업 대기 동안 작업 전환을 통해 처리하면 전체 처리 속도가 향상됩니다.
- 동시성과 병렬성(Parallelism)은 다른 개념입니다.
    - 작업 전환을 통해 동시에 실행 되는 것 처럼 보이는 vs 여러 작업자(코어)를 이용해 실제로 동시에 처리

## 동시성의 장점

- 여러 작업이 동시에 실행되는 것 처럼 보이기에 사용성이 좋아집니다.
    - 운영체제 GUI
- I/O와 같은 대기 시간이 많은 작업에는 대기 중에 작업 전환을 통해서 성능이 빨라질 수 있습니다.
    - 웹 서버, 웹 브라우저

## 동시성의 단점

- 동시성이 고려되면 구현 복잡도가 상대적으로 증가합니다.
- 공유 자원 접근에 대한 경계가 필요합니다.
    - 예를 들어, 멀티 스레딩으로 동시성을 구현한다면 스레드가 프로세스의 공유 자원에 접근할 때 경계가 필요합니다.

## 동시성을 가질 때 공유 자원 접근이 왜 위험한가?

- <code style="color : red">**접근 자체는 위험하지 않으나 접근하고 공유 자원을 변경하는 행위는 문제**가 될 수 있습니다.</code>

### 경합

- 작업들 간에 필요한 공유 자원을 얻기 위한 경쟁이 필연적으로 발생합니다.

### 일관성 파괴

- 경합 상태가 존재할 때 두 작업자가 전부 값을 획득한 상태에서 한 작업자가 상태를 바꿨을 때 다음 작업자가 바꾼 상태는 원하는 결과가 아닐 수도 있습니다.
  - 예를 들어, 잔액이 1000원인 계좌에서 두 작업자가 500원을 인출하려고 할 때, 두 작업자가 동시에 인출을 시도하면 두 작업자가 500원을 인출하고 남은 잔액은 0원이 아닐 수 있습니다.

## 공유 자원에 대한 접근과 변경을 안전하게 할 순 없는가?

- 공유 자원에 동시에 접근해서 변경하지 못하게 해야합니다.
- How? <code style="color : red">**여러 작업자가 동시에 공유 자원을 접근하면 안되는 부분인 임계구역을 지정하고 순차적으로 접근하도록 변경 필요**</code>
    - 상호 배제, 진행, 한정된 대기가 필요합니다.
    - 상호배제 - 다른 작업자가 이미 임계구역에서 실행 중인 작업자의 구역에서 실행될 수 없습니다
    - 진행 - 임계구역에서 작업자가 없다면 다음 임계구역 작업자를 선정하는데 참여 가능하고 무한정 지연될 수 없습니다.
    - 한정된 대기 - 임계구역에 진입을 요청한 후 무한정 대기하는 것이 아닌 다른 작업자들의 임계구역 진입 허용 횟수에 제한이 있습니다.

## 동시성 제어를 위한 다양한 동기화 기법 비교

| 메커니즘                       | 특징                                                                           | 장점                                                            | 단점                  |
|----------------------------|------------------------------------------------------------------------------|---------------------------------------------------------------|---------------------|
| **Mutex**                  | • 상호 배제 보장<br>• 이진 세마포어의 특수한 형태<br>• 자원에 대한 접근을 한 번에 하나의 스레드로 제한<br>• 락을 획득한 스레드만 해제 가능 | • 구현이 단순하고 이해하기 쉬움<br>• 임계 영역 보호에 효율적                   | • 스핀락 구현 시 CPU 자원 낭비 가능<br>• 경쟁 상황에서 성능 저하<br>• 락을 가진 스레드가 블록되면 다른 스레드도 대기 |
| **Semaphore**              | • 여러 개의 자원에 대한 접근 제어<br>• 카운터 기반 동기화 메커니즘<br>• P(감소)/V(증가) 연산으로 접근 제어<br>• 이진 세마포어와 카운팅 세마포어로 구분 | • 여러 스레드의 동시 접근 허용 가능<br>• 생산자-소비자 문제 해결에 적합 | • Mutex보다 복잡한 구현<br>• 락을 획득하지 않고도 해제 가능 |
| **Monitor**                | • 상위 수준 동기화<br>• 데이터와 해당 데이터에 접근하는 프로시저 캡슐화<br>• Java의 synchronized 블록/메소드가 대표적 | • 사용이 직관적이고 오류 가능성 낮음<br>• 캡슐화로 코드 안정성 향상<br>• 상호 배제 자동 보장 | <br>• 과도한 동기화 시 성능 저하<br>• 세밀한 락 제어가 어려울 수 있음 |
| **Compare And Swap (CAS)** | • 원자적 명령어를 사용한 락프리 동기화<br>• 메모리 값을 비교한 후 조건부 교체<br>• 하드웨어 수준의 원자성 보장         | • 락 없이 동시성 제어 가능<br>• 대기 상태가 없어 교착 상태 방지<br>• 높은 처리량과 낮은 지연시간 | • 복잡한 동기화에는 구현이 어려움 |
| **Read-Write Lock**        | • 읽기와 쓰기 작업 구분하여 제어<br>• 다수의 읽기 작업 동시 허용<br>• 쓰기 작업은 배타적 접근<br>• 읽기 많고 쓰기 적은 상황에 최적화 | • 읽기 위주 워크로드에서 성능 향상<br>• 리소스 활용도 증가<br>• 데이터 일관성 유지하며 동시성 최대화<br>• 처리량 증가 | • 구현이 복잡<br>• 쓰기 작업 대기 시 읽기 편향 발생 가능 |

## 자바에서 동기화를 위한 동기화 구현

### synchronized
* 자바에서 제공하는 가장 간단한 동기화 방법입니다.
* 메소드나 블록에 synchronized 키워드를 사용하여 간단하게 동기화 처리할 수 있습니다.
* synchronized를 통해 메소드나 블록에 대한 상호 배제를 달성할 수 있습니다.
* 세밀한 제어나 다양한 동기화 방법을 사용할 수 없습니다.

```java
public synchronized void method() {
    // 동기화 처리할 코드
}
```

### ReentrantLock
* synchronized와 유사한 기능을 제공하는 클래스입니다.
* Lock 인터페이스를 구현한 클래스로 락을 획득하고 해제하는 메소드를 제공합니다.
* synchronized와 달리 락을 획득한 스레드가 락을 해제하지 않고 다른 스레드가 락을 획득할 수 있습니다.

```java
Lock lock = new ReentrantLock();

lock.lock();
try {
    // 동기화 처리할 코드
} finally {
    lock.unlock();
}
```

### ConcurrentDataStructure
* 자바에서 제공하는 동시성을 지원하는 자료구조입니다.
* 동시성을 지원하기 위해 내부적으로 동기화 처리가 되어 있습니다.
* ConcurrentHashMap, CopyOnWriteArrayList, BlockingQueue 등이 있습니다.
* 기존에 자바에서 제공하는 자료구조(ArrayList, HashMap)와 달리 동시성을 고려하여 구현되어 있어 멀티 스레드 환경에서 안전하게 사용 가능합니다.

```java
ConcurrentMap<String, String> map = new ConcurrentHashMap<>();
map.put("key", "value");
```

### Atomic
* 자바에서 제공하는 동시성을 지원하는 관련 객체 타입입니다.
* 내부적으로 CAS(Compare And Swap) 연산을 사용하여 동시성을 제어합니다.
* AtomicInteger, AtomicLong, AtomicReference 등이 있습니다.
* 기존 변수와 달리 동시성을 고려하여 구현되어 있어 멀티 스레드 환경에서 안전하게 사용 가능합니다.

```java
AtomicInteger atomicInteger = new AtomicInteger();
atomicInteger.incrementAndGet();
```

## 과제에서 ConcurrentHashMap과 ReentrantLock을 사용한 이유
* 여러 사용자마다 별도의 동시성 제어를 위해서 ConcurrentHashMap을 사용했습니다.
* 각 사용자에 대한 동시성 제어를 위해서 ReentrantLock을 사용했습니다.