# 📌 Aladin Study Cafe 

## 스터디카페 이용, 관리앱

## 🚀 v2.0 리팩터링

프로젝트를 진행하면서 코드 정리에 어려움을 격어 아키텍처 관련 학습 후 배운 점을 적용하여 리팩터링 중입니다.

### v1.0 기존 프로젝트 링크

https://github.com/ch-yang1273/StudyCafe_SpringBoot/tree/v1.0

### 리팩터링 방향
- 각 레이어의 역할을 구분 (Controller, Service, Repository, Domain)
- 패키지 간 순환이 발생하지 않도록 의존성을 정리
- 테스트하기 좋은 코드를 작성하고, 단위 테스트 작성
- GitHub Actions를 사용하여 CI/CD 적용

### 기간
- 2023년 3월 11일 ~ 진행 중

### 참고 자료
- 도메인 주도 개발 시작하기 (최범균)
- 190620 우아한객체지향 (조영호)
- 단위 테스트 (블라디미르 코리코프)

### [아키텍처 관련 수정 내용]

✂ 패키지 구조 정리

   ![패키지 연관 관계](https://user-images.githubusercontent.com/61798028/229710399-aedc6bd2-9404-45fe-b250-bad310850e1c.jpg)
   
   - layer 단위로 구분했던 패키지 구조를 도메인 단위로 변경
   - Entity의 연관 관계에 맞춰 패키지 간 의존성 제한

✂ 패키지 의존성 순환 해결
   - Follow 도메인 추가
     
     User가 사용 중인 Cafe 정보를 User Entity가 갖고 있고, Cafe 대부분 서비스는 User를 의존
     
     -> User가 사용 중인 Cafe 정보를 Follow Entity에 이관하여 Cafe에 대한 의존성을 제거

   - 좌석 예약 기능을 Seat에서 Reservation으로 이동
     
     Reservation이 예약한 Seat 정보를 포함하고, Seat의 서비스가 Reservation을 생성하면서 의존성이 순환

     -> Reservation 서비스에서 예약을 진행하고, Seat의 상태를 업데이트하면서 단방향 의존성으로 변경

✂ CQRS 패턴 적용
   - 명령은 void를 반환하고, 조회는 DTO를 반환
   - Read와 CUD를 분리하지는 않았음

### [테스트 관련 수정 내용]

✂ 테스트하기 좋은 코드 작성

  - 외부 의존성이 있는 코드와 비즈니스 로직을 분리하고, 단위 테스트 작성
  - [실행 코드 예시](https://github.com/ch-yang1273/StudyCafe_SpringBoot/blob/master/src/main/java/asc/portfolio/ascSb/reservation/domain/ReservationLifecycleManager.java)
  - [테스트 코드 예시](https://github.com/ch-yang1273/StudyCafe_SpringBoot/blob/master/src/test/java/asc/portfolio/ascSb/reservation/domain/ReservationValidatorTest.java)

✂ 시간 메서드 테스트 (생성자, 정적 메서드)
   ```java
   // 기존 코드 (v1.0 JwtTokenProvider.java) 
   public String createAccessToken(String subject) {
       Date now = new Date();
       Date expireDate = new Date(now.getTime() + expireTime);
       ...
   }
   ```
   - 코드의 ```new Date()```와 같은 시간 메서드는 테스트에서 제어가 어려움
   - 생성자와 Static 메서드는 Mocking에도 추가적인 라이브러리(mockito-inline, PowerMokito)가 필요

   ```java
   // 수정 코드 (v2.0 JwtTokenService.java)
   public String createAccessToken(TokenPayload payload, Date now) {
           Date expireDate = new Date(now.getTime() + expireTime);
           ...
   }
   ```
- 시간 메서드 의존성을 상위 레이어로 빼내어, Mocking 없이 단위 테스트 가능
- 시간 메서드는 CurrentTimeProvider 인터페이스로 추상화하여 사용
- 통합 테스트에서는 mockito-inline 라이브러리 없이 CurrentTimeProvider를 Mocking

### [Github Actions를 사용하여 CI/CD 적용]

### [@EC2 퍼블릭 IPv4 링크](http://3.34.210.88:8080/)

Github Actions를 이용하여 CI/CD를 적용했습니다.

- Master Branch 외의 Branch에 Push 시, Github Actions를 통해 테스트를 진행
- Master Branch에 Push 시, 테스트를 진행하고, AWS CodeDeploy를 통해 배포

🔨 CI (All branch)
```mermaid
flowchart LR
    A[Push] --> B[Container Init] --> C[빌드 환경 구성] --> D[빌드 환경 Caching] --> E[단위/통합 테스트]
```
- MariaDB, Redis Container를 Init 하여 단위/통합 테스트 진행
- 빌드 환경 Caching를 통해 빌드 속도를 높임
- [ci.yml](https://github.com/ch-yang1273/StudyCafe_SpringBoot/blob/master/.github/workflows/ci.yml)

🔨 CI/CD (master branch)
```mermaid
flowchart LR
    A[Push] --> B([CI: Test & Build]) --> C[빌드 파일 압축] --> D[AWS 인증 정보 설정] --> E[S3 업로드] --> F[S3에서 EC2로 배포]
```
- Master Branch에서는 CI에 추가로 배포(CD)까지 진행
- 빌드 파일을 S3에 업로드하고, CodeDeploy를 통해 배포
- 배포 후 application.properties를 Production 용으로 교체하고 애플리케이션 재시작
- [cicd.yml](https://github.com/ch-yang1273/StudyCafe_SpringBoot/blob/master/.github/workflows/cicd.yml)
- [application-start.sh](https://github.com/ch-yang1273/StudyCafe_SpringBoot/blob/master/scripts/application-start.sh)

### 남은 작업
- 단위 테스트 추가
- Ticket 도메인 코드 정리
- Orders, Product 도메인 코드 정리
- 각 도메인에서 VO 세분화
- Repository Entity의 User와 Cafe에 대한 의존성 제거 (연관 관계가 많음)
- Admin 기능과 User 서비스 기능은 애플리케이션을 분리하는 것이 좋겠음

*****

## 🌟 v1.0

- Notion : [협업노션, 자세한 개발 과정이 나와있습니다.](https://furry-ocean-0ef.notion.site/d484ac9d91d84327a01a10238da944fe)

## 1. 제작 기간 & 참여 인원

- 2022년 11월 01일 ~ 2023년 2월 9일
- ch-yang1273
- padonan

## 2. 사용 기술

### 📝 Back-end
- springBoot ‘2.7.5’
- jdk 11
- gradle '7.5.1'
- h2 ‘2.1.214’
- QueryDsl '5.0.0'
- jwt '0.11.5'
- swagger '1.6.13'
- bootpay.backend.java // 결제 모듈
- junit '5'

### 📝 Front-end
Git : (https://github.com/padonan/asc_flutter)
- flutter ‘3.3.4’
- dart ‘2.18.2’
- flutter_native_splash: ^2.2.13
- intl: ^0.17.0
- qr_flutter: ^4.0.0
- dio: ^4.0.6
- flutter_secure_storage: ^6.0.0
- jwt_decoder: ^2.0.1
- bootpay: ^4.4.3
- flutter_form_builder: ^7.7.0
- toast: ^0.3.0
- url_launcher: ^6.1.7
- logger: ^1.1.0
- flutter_local_notifications: ^12.0.4
- build_runner: ^2.3.2
- json_serializable: ^6.5.4

## 3. ERD 구조

<img width="944" alt="20221227_194215" src="https://user-images.githubusercontent.com/98295182/209654739-6369b254-2c20-4edc-81ff-8618606fd360.png">

## 4. 핵심 기능

✨ 핵심 서비스는 스터디카페 이용, 결제권 구입, ADMIN의 스터디카페 관리입니다.

![로그인,회원가입](https://user-images.githubusercontent.com/98295182/209656957-abae63fe-bf1c-4300-ba21-f12f71c6e04a.gif)
![ezgif-2-cdb0ac2ff9](https://user-images.githubusercontent.com/98295182/209657000-303d2060-c12c-48aa-817e-ec53915c3384.gif)
![결제1](https://user-images.githubusercontent.com/98295182/209657016-8dac02b4-e134-42c8-bbca-8efe1b50e3be.gif)

![매출](https://user-images.githubusercontent.com/98295182/209657030-a6567c92-ded2-4b59-a743-908164ee65e8.gif)


