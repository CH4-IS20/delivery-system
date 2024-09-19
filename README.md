## 대규모 AI 시스템 설계 프로젝트
![image](https://github.com/user-attachments/assets/fdfe1353-be66-4fd1-9d18-1cf4296fbfd7)

### 1. 목표
이 프로젝트는 허브 간 물류 및 배송 관리 시스템을 구축하기 위한 마이크로서비스 아키텍처(MSA) 기반의 웹 애플리케이션입니다.
회사, 제품, 주문, 배송, 사용자 관리 등 물류 허브를 중심으로 한 종합적인 물류 관리 기능을 제공하며, 
허브 간의 경로 최적화, 실시간 배송 추적, 자동 재고 관리 등 물류 운영의 효율성을 극대화하는 것을 목표로 합니다.

<BR>

### 2. 개발 환경
<img src="https://img.shields.io/badge/Intellij IDEA-000000?style=flat&logo=Intellij IDEA&logoColor=white"/><img src="https://img.shields.io/badge/postman-FF6C37?style=flat&logo=postman&logoColor=white"/><img src="https://img.shields.io/badge/notion-000000?style=flat&logo=notion&logoColor=white"/><img src="https://img.shields.io/badge/slack-4A154B?style=flat&logo=slack&logoColor=white"/><img src="https://img.shields.io/badge/MSA -535D6C?style=flat&logo=awesomewm&logoColor=white"/><img src="https://img.shields.io/badge/swagger -85EA2D?style=flat&logo=swagger&logoColor=white"/>
<br>
<img src="https://img.shields.io/badge/Zipkin -FE5F50?style=flat&logo=Zipkin&logoColor=white"/>
<br>
<img src="https://img.shields.io/badge/Java 17 -C70D2C?style=flat&logo=java&logoColor=white"/><img src="https://img.shields.io/badge/springboot 3.3-6DB33F?style=flat&logo=springboot&logoColor=white"/>


<BR>

## 👨‍👩‍👧‍👦 Our Team

|김진명|이원영|배지원|
|:---:|:---:|:---:|
|[@JinMyeong Kim](https://github.com/kimjinmyeong)|[@twonezero](https://github.com/TwOneZero)|[@JIWON](https://github.com/Bae-Ji-Won)|
|사용자 관리<br>JWT 인증/인가<br>필요 라이브러리 설정<br>(Zipkin, Prometheus, Grafana)|업체<br>상품<br>주문(배송,배송기록)|허브<br>허브 이동관리<br> 배송 관리자|

<br>

## 문서 자료
[<img src="https://img.shields.io/badge/GitBook -BBDDE5?style=flat&logo=gitbook&logoColor=white"/>](https://jinmyeongs-organization.gitbook.io/ch4_team20-i_s2_0-specification) <br>
[🔗 테이블 명세서](https://docs.google.com/spreadsheets/d/194d8Q6aZg50KfJQ5GEZmOlCkWwcXJTtk3YBZngyrUhg/edit?usp=sharing) <br>
[🔗 API 명세서](https://lemon-postbox-fd8.notion.site/API-106dd9282d17805e86d0d99d24522514?pvs=4)  <br>

<br>

## 인프라 아키텍처
<img src="https://github.com/user-attachments/assets/2f8cd3e0-0fce-49e3-b164-0aaa8abfd212" width="500" height="500" />

<br>

## ERD
<img src="https://github.com/user-attachments/assets/6b5b409a-2ded-4cd9-adaf-12d81a5c496e" width="700" height="500" />

<br>

## 프로젝트 기능 및 서비스 구성
### 🏢 Company
- 업체 생성, 조회, 수정, 삭제 기능을 제공하며, 각 업체는 물류 허브에서 제품을 관리하고 유통합니다.
  <details>
  <summary><b>EndPoint</b></summary>
  <div markdown="1">
    <ul>
      
| 메소드    | 엔드포인트                        | 설명       |
|--------|------------------------------|----------|
| GET    | `/api/companies`             | 회사 목록 조회 |
| POST   | `/api/companies`             | 회사 생성    |
| GET    | `/api/companies/{companyId}` | 특정 회사 조회 |
| PUT    | `/api/companies/{companyId}` | 특정 회사 수정 |
| DELETE | `/api/companies/{companyId}` | 특정 회사 삭제 |
</ul>
  </div>
</details>

<br>

### 🎁 Product
- 제품의 생성, 조회, 수정, 삭제를 통해 제품 카탈로그 및 재고 관리를 지원합니다. 
- 각 제품은 특정 업체와 허브에 속하며, 허브 간 이동 및 재고 변화에 따라 수량이 자동으로 조정됩니다.

  <details>
  <summary><b>EndPoint</b></summary>
  <div markdown="1">
    <ul>
      
| 메소드    | 엔드포인트                                | 설명              |
|--------|--------------------------------------|-----------------|
| GET    | `/api/products`                      | 제품 목록 조회        |
| POST   | `/api/products`                      | 제품 생성           |
| GET    | `/api/products/{productId}`          | 특정 제품 조회        |
| PUT    | `/api/products/{productId}`          | 특정 제품 수정        |
| DELETE | `/api/products/{productId}`          | 특정 제품 삭제        |
| PUT    | `/api/products/{productId}/quantity` | 주문을 위한 제품 수량 수정 |
</ul>
  </div>
</details>

<br>

### 🏭 Hub
- 물류 허브 간 경로를 관리하고 최적화하여, 허브 간 배송 프로세스를 지원합니다.
- 허브 간 경로 설정, 주문 처리, 경로 최적화 등의 기능을 통해 운송 효율성을 증대합니다.

  <details>
  <summary><b>EndPoint</b></summary>
  <div markdown="1">
    <ul>
      
| 메소드    | 엔드포인트                  | 설명          |
|--------|------------------------|-------------|
| GET    | `/api/hubs`            | 허브 목록 조회    |
| POST   | `/api/hubs`            | 허브 생성       |
| POST   | `/api/hubs/order`      | 허브에 주문 생성   |
| GET    | `/api/hubs/{hubId}`    | 특정 허브 조회    |
| PUT    | `/api/hubs/{hubId}`    | 특정 허브 수정    |
| DELETE | `/api/hubs/{hubId}`    | 특정 허브 삭제    |
| GET    | `/api/hub-routes`      | 허브 경로 조회    |
| POST   | `/api/hub-routes`      | 허브 경로 생성    |
| GET    | `/api/hub-routes/list` | 허브 경로 목록 조회 |
</ul>
  </div>
</details>

<br>

### 🚛 Delivery Managers
- 배송 관리자 생성, 조회, 수정, 삭제 기능을 제공하여 배송 현장의 관리와 배송 관리자 배정을 지원합니다.

  <details>
  <summary><b>EndPoint</b></summary>
  <div markdown="1">
    <ul>
      
| 메소드    | 엔드포인트                                        | 설명           |
|--------|----------------------------------------------|--------------|
| GET    | `/api/delivery-managers`                     | 배송 관리자 목록 조회 |
| POST   | `/api/delivery-managers`                     | 배송 관리자 생성    |
| GET    | `/api/delivery-managers/{deliveryManagerId}` | 특정 배송 관리자 조회 |
| PUT    | `/api/delivery-managers/{deliveryManagerId}` | 특정 배송 관리자 수정 |
| DELETE | `/api/delivery-managers/{deliveryManagerId}` | 특정 배송 관리자 삭제 |
</ul>
  </div>
</details>

<br>

### 📃 Order
- 주문의 생성, 조회, 수정, 삭제 기능을 통해 주문 처리 및 물류 흐름 관리를 수행합니다.
- 주문 상태에 따른 자동 재고 조정과 허브 간 이동을 연계하여 전체 주문 과정을 관리합니다.

  <details>
  <summary><b>EndPoint</b></summary>
  <div markdown="1">
    <ul>
      
| 메소드    | 엔드포인트                   | 설명       |
|--------|-------------------------|----------|
| GET    | `/api/orders`           | 주문 목록 조회 |
| POST   | `/api/orders`           | 주문 생성    |
| GET    | `/api/orders/{orderId}` | 특정 주문 조회 |
| PUT    | `/api/orders/{orderId}` | 특정 주문 수정 |
| DELETE | `/api/orders/{orderId}` | 특정 주문 삭제 |
</ul>
  </div>
</details>

<br>

### 📃 Delivery
- 배송 상태와 히스토리를 관리하고, 특정 배송의 상세 이력을 추적할 수 있습니다.
- 배송 상태 모니터링과 문제 발생 시 빠른 대응을 지원합니다.

  <details>
  <summary><b>EndPoint</b></summary>
  <div markdown="1">
    <ul>
### 1. Deliveries 도메인

| 메소드    | 엔드포인트                          | 설명       |
|--------|--------------------------------|----------|
| GET    | `/api/deliveries/{deliveryId}` | 특정 배송 조회 |
| PUT    | `/api/deliveries/{deliveryId}` | 특정 배송 수정 |
| DELETE | `/api/deliveries/{deliveryId}` | 특정 배송 삭제 |

<br>

### 2. Delivery Histories 도메인

| 메소드    | 엔드포인트                                             | 설명              |
|--------|---------------------------------------------------|-----------------|
| GET    | `/api/delivery-histories/deliveries/{deliveryId}` | 특정 배송의 이력 조회    |
| POST   | `/api/delivery-histories/deliveries/{deliveryId}` | 특정 배송에 대한 이력 생성 |
| GET    | `/api/delivery-histories/{deliveryHistoryId}`     | 특정 배송 이력 목록 조회  |
| DELETE | `/api/delivery-histories/{deliveryHistoryId}`     | 특정 배송 이력 목록  삭제 |

</ul>
  </div>
</details>

<br>

### 🔐 Auth / User
- 사용자 로그인, 회원가입과 역할 기반 인증 시스템을 통해 각 사용자(기업, 관리자, 일반 사용자)에게 적합한 권한을 부여합니다.


  <details>
  <summary><b>EndPoint</b></summary>
  <div markdown="1">
    <ul>

### 1. Auth
| 메소드  | 엔드포인트                | 설명   |
|------|----------------------|------|
| POST | `/api/auth/login`    | 로그인  |
| POST | `/api/auth/register` | 회원가입 |

<br>

### 2. User

| 메소드    | 엔드포인트                 | 설명        |
|--------|-----------------------|-----------|
| GET    | `/api/users`          | 사용자 목록 조회 |
| GET    | `/api/users/{userId}` | 특정 사용자 조회 |
| PUT    | `/api/users/{userId}` | 특정 사용자 수정 |
| DELETE | `/api/users/{userId}` | 특정 사용자 삭제 |

</ul>
  </div>
</details>

<br>

### 📩 Slack
- Slack 과의 연동을 통해 물류 관련 알림을 실시간으로 받아볼 수 있어, 신속한 협업과 문제 해결이 가능합니다.


  <details>
  <summary><b>EndPoint</b></summary>
  <div markdown="1">
    <ul>

| 메소드  | 엔드포인트        | 설명          |
|------|--------------|-------------|
| POST | `/api/slack` | Slack 알림 전송 |
| GET  | `/api/slack` | Slack 설정 조회 |

</ul>
  </div>
</details>
