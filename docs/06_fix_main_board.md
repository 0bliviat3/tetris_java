````text id="n7v4qx"
현재 우측 SidePanel 렌더링 문제는 해결되었지만,
이후 메인 게임 보드에서 테트로미노가 깨져서 생성되는 문제가 발생했다.

중요:
이번 수정은 UI 문제가 아니라
"테트로미노 렌더링/좌표/shape 처리 문제"를 분석하고 수정하는 작업이다.

기존 구조를 최대한 유지하면서
문제 원인만 정확히 분석 후 최소 수정으로 해결할 것.

전체 게임 로직을 전면 재작성하지 말 것.

---

현재 증상

메인 게임 보드에서:
- 테트로미노 일부 블록이 잘려 보임
- 블록 위치가 어긋남
- shape 배열이 깨져 보임
- 생성 직후부터 비정상 형태인 경우 존재

특히:
- I/T/L/J 계열에서 더 자주 발생 가능성 있음

우측 preview는 정상 표시되지만,
실제 게임 보드 렌더링만 깨진 상태다.

---

중요 분석 포인트

최근 구조 변경으로 인해:
- GamePanel 렌더링 구조
- 좌표 계산
- BLOCK_SIZE 사용 위치
- paintComponent 분리
등이 변경되었다.

따라서:
단순히 "shape 데이터 문제"라고 가정하지 말고
렌더링 좌표와 보드 좌표계를 먼저 검증할 것.

---

우선 확인해야 할 항목

1. drawCurrentTetromino()

다음 항목 검증:
- row/col 계산 정상 여부
- x/y 계산 정상 여부
- BLOCK_SIZE 곱셈 위치
- currentTetromino.getRow()/getCol() 값 정상 여부

특히:
```java
x = (col + tetrominoCol) * BLOCK_SIZE
y = (row + tetrominoRow) * BLOCK_SIZE
````

계산이 올바른지 검증.

---

2. Tetromino.getShape()

검증 항목:

* 회전 후 shape 배열 정상 여부
* 배열 크기 일관성
* rotation index 정상 여부

특히:

* shape[][] 크기와 실제 렌더링 루프 범위 일치 여부 확인

---

3. Board 현재 블록 상태

검증 항목:

* spawn 위치 정상 여부
* currentRow/currentCol 음수 여부
* 회전 직후 위치 보정 문제 존재 여부

---

4. paintComponent() clipping 문제

확인 항목:

* graphics clipping 발생 여부
* board 영역 offset 문제
* SidePanel 추가 이후 board width 계산 오류 여부

---

수정 목표

* 모든 테트로미노가 정상 형태로 생성되어야 함
* 블록이 잘리거나 어긋나면 안 됨
* 회전 후에도 shape 유지
* preview와 실제 board shape가 일치해야 함

---

중요 요구사항

* 기존 게임 로직 최대한 유지
* Tetromino 데이터 구조 전면 수정 금지
* Board 로직 전면 수정 금지
* 문제 원인 먼저 분석 후 최소 수정

---

권장 디버깅 방식

다음 정보를 먼저 출력/검증:

* current row/col
* shape dimensions
* x/y rendering positions
* rotation state
* shape[][] contents

예시:

```java
System.out.println(Arrays.deepToString(shape));
```

등 사용 가능.

---

작업 순서

1. 현재 깨지는 현상 원인 분석
2. 렌더링 좌표 vs shape 데이터 문제 구분
3. 실제 문제 위치 설명
4. 최소 수정 설계
5. 코드 수정
6. 변경사항 요약
7. 테스트 방법 설명

특히:
이번 문제는
"렌더링 구조 변경 이후 좌표계 충돌인지"
아니면
"shape rotation 데이터 문제인지"
먼저 명확히 구분 후 수정할 것.

```
```

