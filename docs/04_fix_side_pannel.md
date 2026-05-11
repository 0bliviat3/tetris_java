현재 GamePanel UI 구조에서 사이드 패널과 커스텀 렌더링 영역이 충돌하고 있다.
기존 구조를 유지하면서 레이아웃 문제만 최소 수정으로 해결해라.

문제 상황 분석:

현재 GamePanel은:
- BorderLayout 기반
- EAST 영역에 Swing sidePanel 추가
- 동시에 paintComponent(Graphics g) 내부에서 next piece preview를 직접 렌더링

하고 있다.

이로 인해:
- Swing sidePanel이 실제 렌더링 영역 위를 덮고 있음
- drawNextPiece()에서 그린 preview가 sidePanel 뒤에 가려져 보이지 않음

또한:
- Help 버튼이 sidePanel 중앙 부근에 위치
- 하단 고정이 제대로 되지 않음

---

수정 요구사항:

1. next piece preview가 정상 표시되도록 수정

수정 목표:
- next preview 영역이 sidePanel에 가려지지 않아야 함
- 모든 테트로미노가 정상적으로 보일 것
- 기존 preview 렌더링 구조 최대한 유지

권장 해결 방향 (우선 검토):

방법 A:
- preview / score / level 렌더링을
  sidePanel 내부 Swing 컴포넌트로 이동

또는

방법 B:
- sidePanel을 투명 처리(setOpaque(false))
- custom painting 영역과 충돌 제거

또는

방법 C:
- sidePanel 영역을 제외한 위치로 preview 렌더링 좌표 수정

단:
- 기존 구조를 최대한 유지
- 가장 작은 수정으로 해결할 것

특히 확인할 부분:
- Swing 컴포넌트와 custom paintComponent의 z-order 충돌
- BorderLayout EAST가 실제 렌더링을 덮는 구조 문제

---

2. Help 버튼 하단 고정 강화

현재:
- sidePanel.add(Box.createVerticalGlue())
를 사용 중이지만
- Help 버튼이 충분히 아래로 내려가지 않음

수정 목표:
- Help 버튼을 우측 패널 "맨 아래" 근처에 고정
- 하단 padding만 약간 유지
- 창 크기가 변해도 하단 유지

권장 수정 방향:

예시:
- sidePanel에 BorderLayout 사용
- SOUTH 영역에 help button panel 배치

또는:
- vertical glue + rigid area 조정

권장 배치 구조:

TOP:
- score
- level
- next preview

BOTTOM:
- Help 버튼

---

추가 요구사항:

- 기존 drawBoard()/drawNextPiece() 구조 최대한 유지
- 전체 UI를 새로 갈아엎지 말 것
- 변경된 클래스와 수정 이유를 설명할 것
- repaint/revalidate 필요 여부 분석 후 적용할 것

작업 순서:

1. 현재 Swing 레이아웃 구조 분석
2. preview가 가려지는 원인 설명
3. Help 버튼 위치 문제 원인 설명
4. 최소 수정 방식 설계
5. 코드 수정
6. 변경사항 요약
7. 테스트 방법 설명

특히:
현재 문제는 단순 좌표 문제가 아니라
Swing 컴포넌트와 custom rendering의 계층(z-order) 충돌 문제인지 먼저 분석 후 수정할 것.
