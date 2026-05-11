````text id="c5q8mx"
이제부터는 추측 기반 수정이 아니라,
실제 컴파일 및 실행 결과를 기반으로 문제를 수정해라.

현재 환경에는 JDK가 설치되어 있으므로:
- 실제 build 수행
- compile error 확인
- runtime error 확인
- 로그 분석
을 기반으로 수정 작업을 진행할 것.

중요:
이전처럼 “코드를 보기엔 맞아 보인다” 수준으로 판단하지 말고,
반드시 실제 컴파일 결과를 확인하면서 수정할 것.

---

작업 목표

1. 프로젝트 실제 컴파일 수행
2. compile error 발생 시 수정
3. 실행 가능한 상태까지 안정화
4. runtime error 발생 시 수정
5. key input / rendering 정상 동작 검증

---

필수 작업 절차

# 1. 실제 build 수행

우선 다음 중 가능한 방식으로 build:

## Gradle 사용 시

```bash
./gradlew build
````

또는 Windows:

```bash
gradlew.bat build
```

---

## Gradle 없으면 javac 직접 사용

예시:

```bash
javac -d out $(find src/main/java -name "*.java")
```

Windows 예시:

```cmd
javac -d out src\main\java\game\*.java src\main\java\game\constants\*.java
```

---

# 2. compile error 발생 시

반드시:

* 실제 에러 메시지 확인
* 원인 분석
* 최소 수정 적용

후 다시 build 수행.

중요:
한 번 수정 후 끝내지 말고
build 통과할 때까지 반복.

---

# 3. build 성공 후 실행

실행 가능하면 실제 실행:

```bash
./gradlew run
```

또는:

```bash
java -cp out game.Main
```

---

# 4. runtime 검증

다음 항목 실제 확인:

## 입력

* 방향키 이동
* 회전
* Space hard drop
* P pause
* R restart

---

## 렌더링

* active tetromino 정상 표시
* fixed blocks 정상 표시
* preview 정상 표시
* score/level 정상 표시

---

## UI

* Help 버튼 정상
* focus 정상 유지
* Game Over 정상

---

# 매우 중요한 요구사항

이제부터는:

❌ "코드를 보니 맞아 보인다"

가 아니라

✅ "실제 컴파일 및 실행 결과"

기준으로 수정할 것.

---

# 오류 수정 원칙

* 최소 수정 원칙 유지
* 기존 게임 로직 최대한 유지
* 문제 원인 먼저 분석
* build → 수정 → rebuild 반복

---

# 디버깅 요구사항

오류 발생 시 반드시 포함:

1. 실제 에러 메시지
2. 발생 파일/라인
3. 원인 분석
4. 수정 내용
5. 수정 후 build 결과

---

# 작업 순서

1. 실제 build 수행
2. compile error 수정
3. rebuild
4. 실행 테스트
5. runtime 문제 수정
6. 최종 build 성공 확인
7. 변경사항 요약

---

특히:
이제는 "추측성 수정" 금지.
반드시 실제 빌드/실행 결과 기반으로 수정할 것.

```
```

