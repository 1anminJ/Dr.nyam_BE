# 🍽️ 쩝쩝박사 (Dr.Nyam) — Backend API

> **"사람의 취향이 겹쳐지는 지도"** — 개인의 맛집 기록을 소셜 큐레이션 자산으로 만드는 지도 서비스

<p align="center">
  <a href="https://github.com/1anminJ/Dr.nyam_FE">💻 Frontend Repo</a>
</p>

---

## 📖 프로젝트 소개

쩝쩝박사(Dr.Nyam)는 나만의 맛집을 **지도 위에 기록**하고, **링크 하나로 공유**하며, **친구의 취향과 겹쳐보는** 소셜 맛집 지도 서비스입니다.

이 레포지토리는 Dr.Nyam 서비스의 **Spring Boot 백엔드 API 서버**입니다.

---

## 🤔 문제 정의 (Problem Statement)

- 네이버/카카오 지도 즐겨찾기는 **개인 메모와 맥락 기록이 제한적**
- 맛집 추천이 **카톡방 대화 속에서 휘발**되고, 체계적으로 축적되지 않음
- 친구의 취향을 신뢰하지만 이를 **구조적으로 참고할 방법이 부족**

### 💡 해결 방향

| 기능 | 사용자 가치 |
|:----:|:----------:|
| **개인 핀 + 태그** | 기억에 의존하지 않는 관리 |
| **지도 + 리스트** | 탐색 맥락에 맞는 UI |
| **링크 공유** | 추천을 자산처럼 전달 |
| **소셜 맵** | 취향 교집합 발견 |

---

## 👥 타겟 사용자

| Persona | 설명 | 핵심 니즈 |
|:-------:|:----:|:---------:|
| 🍜 **프로 맛집러 민지** (29세) | 맛집 기록/정리를 즐기는 사용자 | 개인 큐레이션 지도 |
| 📋 **총무 재호** (34세) | 모임 장소 선정 빈도가 높은 사용자 | 검증된 리스트, 빠른 의사결정 |

---

## ✨ 핵심 기능

### P0 (MVP 필수)
- 📍 **나만의 맛집 핀**: 네이버 지도 위에 핀을 찍고 별점/한줄평/태그 기록
- 🔖 **태그 기반 필터링**: 상황별(데이트, 가성비, 혼밥 등) 맛집 탐색
- 🔗 **큐레이션 공유**: 내 지도를 링크 하나로 공유 (비회원도 조회 가능)
- 🔐 **소셜 로그인**: 카카오 OAuth2 + 이메일 회원가입

### P1 (확장)
- 👥 **유저 팔로우**: 친구를 팔로우하면 친구의 핀이 내 지도에 겹쳐서 보임
- 🗺️ **소셜 맵 오버레이**: 내 핀 / 친구 핀 색상 구분

### P2 (고도화)
- 📰 활동 피드
- 🔥 인기 태그

---

## 🛠️ 기술 스택

| 구분 | 기술 |
|:----:|:----:|
| **Framework** | Spring Boot 3.2.2 |
| **Language** | Java 17 |
| **인증/보안** | Spring Security + JWT (jjwt 0.12.3) |
| **소셜 로그인** | Spring OAuth2 Client (카카오) |
| **Database** | MySQL 8.0 + Spring Data JPA |
| **API 문서** | Swagger (Springdoc-openapi 2.3.0) |
| **Validation** | Spring Boot Starter Validation |
| **Build Tool** | Gradle |

> 💡 **PRD 기준 스택과의 차이**: PRD에서는 NestJS + PostgreSQL을 계획했으나, 학습 목적 및 포트폴리오 일관성을 위해 **Spring Boot + MySQL**로 전환하여 개발했습니다.

---

## 🏗️ 시스템 아키텍처

```
┌─────────────────┐                    ┌──────────────────┐
│   React (SPA)   │   REST API         │  Spring Boot     │
│   + Zustand     │ ◄──────────────►   │  Backend API     │
│   + Naver Maps  │                    │                  │
└─────────────────┘                    └────────┬─────────┘
                                                │
                                   ┌────────────┼────────────┐
                                   ▼            ▼            ▼
                           ┌────────────┐ ┌──────────┐ ┌──────────┐
                           │ Kakao      │ │  Naver   │ │  MySQL   │
                           │ OAuth API  │ │ Maps API │ │  DB      │
                           └────────────┘ └──────────┘ └──────────┘
```

---

## 📁 프로젝트 구조

```
src/main/java/com/project/dr_nyam_be/
├── DrNyamBeApplication.java         # 메인 애플리케이션
│
├── config/                           # 설정
│   └── SecurityConfig.java           # Spring Security (CORS, JWT 필터)
│
├── security/                         # JWT 보안
│   ├── JwtTokenProvider.java         # JWT 토큰 생성/검증
│   └── JwtAuthenticationFilter.java  # JWT 인증 필터
│
├── controller/                       # API 컨트롤러
│   ├── AuthController.java           # 인증 (회원가입, 로그인, 카카오)
│   ├── UserController.java           # 사용자 관리
│   ├── PinController.java            # 맛집 핀 CRUD
│   └── ShareController.java          # 공유 링크
│
├── service/                          # 비즈니스 로직
│   ├── AuthService.java              # 인증 (일반 + 카카오 OAuth)
│   ├── KakaoService.java             # 카카오 API 연동
│   ├── UserService.java
│   ├── PinService.java
│   ├── PlaceService.java
│   └── ShareLinkService.java
│
├── repository/                       # 데이터 접근 계층
│   ├── UserRepository.java
│   ├── PinRepository.java
│   ├── PlaceRepository.java
│   └── ShareLinkRepository.java
│
├── entity/                           # JPA 엔티티
│   ├── User.java                     # 사용자 (소셜 로그인 지원)
│   ├── Pin.java                      # 맛집 핀 (별점, 코멘트, 태그)
│   ├── Place.java                    # 장소 (네이버 지도 기반, lat/lng)
│   └─��� ShareLink.java                # 공유 링크 (UUID, 만료일)
│
└── dto/                              # 데이터 전송 객체
    ├── SignUpRequest.java
    ├── LoginRequest.java
    ├── TokenResponse.java
    ├── TokenRefreshRequest.java
    ├── UserInfoResponse.java
    ├── PasswordChangeRequest.java
    ├── KakaoLoginRequest.java
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
│ provider_id │       │ comment     │       │ latitude    │
│ role        │       │ tags        │       │ longitude   │
│ created_at  │       │ created_at  │       │ address     │
└─────────────┘       └─────────────┘       └─────────────┘
       │
       │        ┌──────────────┐
       └───────►│  ShareLinks  │
                ├──────────────┤
                │link_uuid(PK) │
                │ user_id(FK)  │
                │ expired_at   │
                │ created_at   │
                └──────────────┘
```

---

## 📡 API 엔드포인트

### Auth API (인증)
| Method | Endpoint | Description | 인증 |
|:------:|:--------:|:-----------:|:----:|
| POST | `/api/auth/signup` | 이메일 회원가입 | ❌ |
| POST | `/api/auth/login` | 이메일 로그인 | ❌ |
| POST | `/api/auth/refresh` | 토큰 갱신 | ❌ |
| GET | `/api/auth/me` | 내 정보 조회 | ✅ |
| PUT | `/api/auth/password` | 비밀번호 변경 | ✅ |
| DELETE | `/api/auth/me` | 회원 탈퇴 | ✅ |
| GET | `/api/auth/kakao/url` | 카카오 로그인 URL | ❌ |
| POST | `/api/auth/kakao` | 카카오 로그인 | ❌ |

### Pin API (맛집 핀)
| Method | Endpoint | Description | 인증 |
|:------:|:--------:|:-----------:|:----:|
| GET | `/api/pins/user/{userId}` | 사용자별 핀 목록 | ❌ |
| GET | `/api/pins/{id}` | 핀 상세 조회 | ❌ |
| POST | `/api/pins` | 핀 저장 | ✅ |
| PUT | `/api/pins/{id}` | 핀 수정 | ✅ |
| DELETE | `/api/pins/{id}` | 핀 삭제 | ✅ |

### Share API (공유 링크)
| Method | Endpoint | Description | 인증 |
|:------:|:--------:|:-----------:|:----:|
| POST | `/api/share` | 공유 링크 생성 | ✅ |
| GET | `/api/share/{linkUuid}` | 공유된 핀 조회 | ❌ |
| GET | `/api/share/user/{userId}` | 내 공유 링크 목록 | ✅ |
| DELETE | `/api/share/{linkUuid}` | 공유 링크 삭제 | ✅ |

### User API (사용자)
| Method | Endpoint | Description | 인증 |
|:------:|:--------:|:-----------:|:----:|
| GET | `/api/users` | 사용자 목록 | ❌ |
| GET | `/api/users/{id}` | 사용자 조회 | ❌ |
| POST | `/api/users` | 사용자 생성 | ❌ |
| DELETE | `/api/users/{id}` | 사용자 삭제 | ✅ |

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

## 🚀 시작하기

### 사전 요구사항
- Java 17 이상
- MySQL 8.0
- Gradle

### 데이터베이스 설정

```sql
CREATE DATABASE drnyam DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 설정 파일 수정

`src/main/resources/application.properties`:

```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/drnyam
spring.datasource.username=your_username
spring.datasource.password=your_password

# JWT
jwt.secret=your_jwt_secret_key
jwt.access-token-validity=3600000
jwt.refresh-token-validity=604800000

# Kakao OAuth
kakao.client-id=your_kakao_client_id
kakao.redirect-uri=your_redirect_uri
```

### 실행

```bash
./gradlew bootRun
```

### Swagger API 문서

```
http://localhost:8080/swagger-ui/index.html
```

---

## 🗓️ 개발 로드맵

### Week 1: 기본 지도 + 핀 저장
- [x] Spring Boot 프로젝트 초기 세팅
- [x] MySQL DB 연동 및 Entity 설계
- [x] 기본 CRUD API 개발
- [x] Swagger API 문서화

### Week 2: 공유 링크 + 인증
- [x] JWT 기반 인증 시스템
- [x] 일반 로그인 (이메일/비밀번호)
- [x] 카카오 OAuth2 로그인
- [x] 토큰 갱신 / 내 정보 / 비밀번호 변경 / 회원 탈퇴 API
- [x] 공유 링크 CRUD API

### Week 3: 프론트엔드 연동 + 안정화
- [ ] 프론트엔드 연동
- [ ] 소셜 기능 (팔로우, 오버레이)
- [ ] AWS 배포

---

## ⚠️ 리스크 & 대응

| 리스크 | 대응 전략 |
|:------:|:---------:|
| 초기 콘텐츠 부족 | 공유 링크 중심 바이럴 성장 |
| 지도 API 제약 | 최소 기능 사용, 이용 약관 준수 |
| 일정 초과 | P0 외 기능 과감히 제외 |

---

## 📊 성공 지표 (Success Metrics)

- MAU / WAU (월간/주간 활성 사용자)
- 유저당 평균 핀 저장 개수
- 공유 링크 생성 수
- 공유 링크 → 회원가입 전환율
- 팔로우 관계 생성률

---

## 👤 개발자

**한민정** — 기획, 설계, 백엔드/프론트엔드 개발

[![GitHub](https://img.shields.io/badge/GitHub-1anminJ-181717?style=flat-square&logo=github)](https://github.com/1anminJ)
[![Email](https://img.shields.io/badge/Email-mjeoung413@gmail.com-EA4335?style=flat-square&logo=gmail)](mailto:mjeoung413@gmail.com)

---

## 📄 Related Repositories

| Repository | Description |
|:----------:|:-----------:|
| [Dr.nyam_FE](https://github.com/1anminJ/Dr.nyam_FE) | React 프론트엔드 |

---

## 📄 License

This project is licensed under the MIT License.
