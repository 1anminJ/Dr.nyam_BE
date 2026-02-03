# 🍽️ 쩝쩝박사 (Dr.Nyam) - Backend

> **"사람의 취향이 겹쳐지는 지도"** — 개인의 맛집 기록을 소셜 큐레이션 자산으로 만드는 지도 서비스

## 📖 프로젝트 소개

쩝쩝박사는 나만의 맛집을 기록하고, 친구들과 공유할 수 있는 소셜 맛집 지도 서비스입니다.

### 해결하고자 하는 문제
- 네이버/카카오 지도 즐겨찾기는 개인 메모와 맥락 기록이 제한적
- 맛집 추천이 카톡방 대화 속에서 휘발됨
- "믿을만한 친구의 맛집 리스트"를 한눈에 볼 방법이 없음

### 핵심 기능
- **나만의 지도**: 네이버 지도 위에 내 핀(Pin)을 찍고 태그/별점/코멘트 기록
- **큐레이션 공유**: 내 지도를 링크 하나로 친구에게 공유 (비회원도 조회 가능)
- **소셜 팔로우** (예정): 친구를 팔로우하면 친구의 핀이 내 지도에 겹쳐서 보임

---

## 🛠️ 기술 스택

| 구분 | 기술 |
|------|------|
| **Framework** | Spring Boot 3.x |
| **Language** | Java 17+ |
| **Database** | MySQL 8.0 |
| **ORM** | Spring Data JPA |
| **API 문서** | Swagger (Springdoc-openapi) |
| **Build Tool** | Gradle |

---

## 📁 프로젝트 구조

```
src/main/java/com/project/dr_nyam_be/
├── DrNyamBeApplication.java    # 메인 애플리케이션
│
├── controller/                  # API 컨트롤러
│   ├── UserController.java      # 사용자 API
│   ├── PinController.java       # 맛집 핀 API
│   └── ShareController.java     # 공유 링크 API
│
├── service/                     # 비즈니스 로직
│   ├── UserService.java
│   ├── PinService.java
│   ├── PlaceService.java
│   └── ShareLinkService.java
│
├── repository/                  # 데이터 접근 계층
│   ├── UserRepository.java
│   ├── PinRepository.java
│   ├── PlaceRepository.java
│   └── ShareLinkRepository.java
│
├── entity/                      # JPA 엔티티
│   ├── User.java                # 사용자
│   ├── Pin.java                 # 맛집 핀 (별점, 코멘트, 태그)
│   ├── Place.java               # 장소 (네이버 지도 기반)
│   └── ShareLink.java           # 공유 링크
│
└── dto/                         # 데이터 전송 객체
    ├── UserCreateRequest.java
    ├── PinCreateRequest.java
    ├── PinUpdateRequest.java
    ├── PlaceRequest.java
    └── ShareLinkCreateRequest.java
```

---

## 📊 ERD (Entity Relationship Diagram)

```
┌─────────────┐       ┌─────────────┐       ┌─────────────┐
│    Users    │       │    Pins     │       │   Places    │
├─────────────┤       ├─────────────┤       ├─────────────┤
│ user_id(PK) │◄──┐   │ pin_id(PK)  │   ┌──►│place_id(PK) │
│ email       │   └───│ user_id(FK) │   │   │naver_place_id│
│ nickname    │       │ place_id(FK)│───┘   │ name        │
│ provider    │       │ rating      │       │ category    │
│ role        │       │ comment     │       │ latitude    │
│ created_at  │       │ tags        │       │ longitude   │
└─────────────┘       └─────────────┘       └─────────────┘
       │
       │        ┌──────────────┐
       └───────►│  ShareLinks  │
                ├──────────────┤
                │link_uuid(PK) │
                │ user_id(FK)  │
                │ expired_at   │
                └──────────────┘
```

---

## 🚀 시작하기

### 1. 사전 요구사항
- Java 17 이상
- MySQL 8.0
- Gradle

### 2. 데이터베이스 설정

```sql
CREATE DATABASE drnyam DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. 설정 파일 수정

`src/main/resources/application.properties`에서 DB 정보를 수정하세요:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/drnyam
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 4. 애플리케이션 실행

```bash
./gradlew bootRun
```

### 5. API 문서 확인

서버 실행 후 아래 URL에서 Swagger UI를 확인할 수 있습니다:

```
http://localhost:8080/swagger-ui/index.html
```

---

## 📡 API 엔드포인트

### User API
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/users` | 모든 사용자 조회 |
| GET | `/api/users/{id}` | 특정 사용자 조회 |
| POST | `/api/users` | 사용자 생성 |
| DELETE | `/api/users/{id}` | 사용자 삭제 |

### Pin API
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/pins/user/{userId}` | 사용자별 핀 목록 조회 |
| GET | `/api/pins/{id}` | 핀 상세 조회 |
| POST | `/api/pins` | 핀 저장 |
| PUT | `/api/pins/{id}` | 핀 수정 |
| DELETE | `/api/pins/{id}` | 핀 삭제 |

### Share API
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/share` | 공유 링크 생성 |
| GET | `/api/share/{linkUuid}` | 공유된 핀 조회 |
| GET | `/api/share/user/{userId}` | 내 공유 링크 목록 |
| DELETE | `/api/share/{linkUuid}` | 공유 링크 삭제 |

---

## 📝 API 요청 예시

### 핀 저장
```json
POST /api/pins
{
  "userId": 1,
  "place": {
    "naverPlaceId": "1234567890",
    "name": "을지로 골목식당",
    "category": "한식",
    "latitude": 37.5665,
    "longitude": 126.9780
  },
  "rating": 5,
  "comment": "분위기 좋고 맛있어요!",
  "tags": "데이트,분위기좋은,가성비"
}
```

### 공유 링크 생성
```json
POST /api/share
{
  "userId": 1,
  "expirationDays": 7
}
```

---

## 🗓️ 개발 로드맵

- [x] Spring Boot 프로젝트 초기 세팅
- [x] MySQL DB 연동 및 Entity 설계
- [x] 기본 CRUD API 개발
- [x] Swagger API 문서화
- [ ] 카카오 OAuth2 로그인 (JWT)
- [ ] 프론트엔드 연동
- [ ] AWS 배포

---

## 👤 개발자

- **한민정**

---

## 📄 License

This project is licensed under the MIT License.
