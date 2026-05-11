````text id="p3k8qx"
현재 테트리스 프로젝트는 compile은 되지만,
runtime 상태가 매우 불안정하다.

기존 상태 보고는 신뢰하지 않겠다.

반드시:
실제 실행 기준으로 문제를 재현하고,
실제 source 수정 후,
실제 diff와 실행 결과 기반으로만 작업할 것.

추측 금지.
설명 위주 금지.
실행 없이 "해결 완료" 선언 금지.

---

# 현재 실제 확인된 문제

## 1. 메인패널과 사이드패널 영역 겹침

증상:
- SidePanel이 메인보드 위를 침범
- UI가 일부 겹쳐서 렌더링됨

수정 목표:
- 메인 보드와 SidePanel 완전 분리
- BorderLayout 정상화
- 메인 보드 영역 계산 재검증

반드시:
```java id="jlwmg1"
add(sidePanel, BorderLayout.EAST);
````

구조 유지.

그리고:
board rendering이
SidePanel 영역까지 그리지 않도록 수정.

---

# 2. 테르노미노 회전 시 다른 블록으로 변경되는 버그

증상:

* rotate 시 shape/type이 바뀜
* I → T 같은 현상 발생

원인 의심:

* shape array reference 공유
* shallow copy
* rotation 시 원본 mutate

수정 목표:

* deep copy 기반 회전
* 원본 shape immutable 유지
* currentTetromino / nextTetromino reference 분리

특히:

```java id="jlwmg2"
Tetromino.rotate()
```

집중 분석.

---

# 3. 블록 고정 시 색상 변경

증상:

* falling 상태와 fixed 상태 색상 다름
* 일부 block이 검게 변함

수정 목표:

* grid 저장값 검증
* type/color mapping 통일
* drawPlacedBlocks / drawCurrentTetromino
  동일 color mapping 사용

반드시:

```java id="jlwmg3"
getBlockColor()
```

하나만 기준으로 사용.

---

# 4. Help 버튼 사라짐

증상:

* Help 버튼 안 보임

수정 목표:

* SidePanel 내부에 정상 배치
* BorderLayout.SOUTH 고정
* 버튼 size/visibility 확인

반드시:

```java id="jlwmg4"
helpButton.setFocusable(false);
```

유지.

---

# 5. 메인패널 격자 안 보임

증상:

* dark theme 때문에 grid visibility 부족

수정 목표:

* grid color contrast 개선

현재:

```java id="jlwmg5"
new Color(50, 50, 50)
```

수준이면 더 밝게 수정.

예:

```java id="jlwmg6"
new Color(80, 80, 80)
```

이상.

---

# 6. Preview 테르노미노 색상 깨짐

증상:

* 일부 block만 어둡게 렌더링됨

수정 목표:

* Graphics color state 복원
* drawNextPiece 내부 color 처리 정리

반드시:
각 block마다
fillRect 이후 color restore 수행.

---

# 7. 키 입력 재검증

현재:
입력이 다시 깨졌을 가능성 존재.

반드시 확인:

* 방향키
* 회전
* hard drop
* pause
* restart

전부 실제 실행 테스트.

---

# 8. repaint 흐름 재검증

반드시 확인:

* GameLoop tick 발생
* repaint 호출
* paintComponent 호출
* drawCurrentTetromino 호출

실제 로그로 검증.

---

# 매우 중요한 제한사항

절대 금지:

* 전체 프로젝트 재작성
* Board/GameLoop 재설계
* 구조 갈아엎기
* 임시 하드코딩
* "수정했다고 가정"

반드시:
현재 구조 유지 + regression fix만 수행.

---

# 작업 절차

1. 실제 실행
2. 문제 재현
3. 원인 분석
4. 최소 수정
5. rebuild
6. 재실행
7. regression 테스트

---

# 반드시 출력해야 하는 것

## 수정된 파일 목록

예:

```text id="jlwmg7"
GamePanel.java
Tetromino.java
...
```

---

## 실제 diff

반드시:

```bash id="jlwmg8"
git diff
```

출력.

---

## compile 결과

```bash id="jlwmg9"
./gradlew build
```

실제 output 출력.

---

## runtime 테스트 결과

반드시 항목별 작성:

* SidePanel:
* HelpButton:
* Tetromino Drop:
* Rotation:
* Fixed Block Color:
* Grid Visibility:
* Preview Rendering:
* Key Input:

---

# 중요

이번 작업의 목표는:

```text id="jlwmga"
"실제 플레이 가능한 안정 상태 복구"
```

이다.

말로 설명하지 말고,
실제 source 수정과 실제 실행 결과로 증명할 것.

```

