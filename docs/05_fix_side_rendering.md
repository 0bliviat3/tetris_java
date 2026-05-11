Help 버튼 하단 고정 문제는 해결되었지만,
사이드 패널과 custom rendering 충돌 문제는 아직 해결되지 않았다.

현재도 next piece preview가 보이지 않거나 일부 가려지고 있다.

이번에는 단순 레이아웃 수정이 아니라,
Swing 컴포넌트와 paintComponent(Graphics g) 렌더링 구조 자체를 정확히 분석해서 수정해라.

중요:
기존 게임 로직은 건드리지 말고
UI 렌더링 구조만 최소 수정으로 해결할 것.

---

현재 문제 분석

현재 구조:

- GamePanel 자체에서 paintComponent(Graphics g) 수행
- drawNextPiece(g)로 preview를 직접 그림
- 동시에 BorderLayout.EAST 영역에 Swing sidePanel 존재

문제:
Swing sidePanel이 실제로 EAST 영역 전체를 점유하면서
custom rendering 영역(drawNextPiece)이 sidePanel 뒤에 가려진다.

즉:
preview 좌표는 존재하지만
Swing 컴포넌트 레이어 아래에 렌더링되고 있음.

현재 BorderLayout 변경만으로는 해결되지 않았다.

---

수정 목표

1. next piece preview가 항상 정상적으로 보이게 수정
2. score / level / preview 영역이 sidePanel과 충돌하지 않게 수정
3. Help 버튼은 현재처럼 하단 고정 유지
4. 기존 게임 로직(Board/GameLoop/Input)은 수정 금지

---

중요 요구사항

현재 문제는:
"좌표 문제"가 아니라
"Swing component와 custom painting의 계층(z-order) 충돌 문제"다.

따라서:
preview를 단순히 다른 좌표에 그리는 방식으로 해결하지 말 것.

---

권장 해결 방식 (우선 검토)

다음 방식 중 하나로 해결할 것:

방법 A (권장):
- preview / score / level 렌더링을
  별도의 Swing JPanel(side panel) 내부 paintComponent로 이동

즉:
- 게임 보드 영역만 GamePanel이 그림
- 우측 UI는 별도 JPanel이 그림

이 방식이 가장 안정적이다.

또는

방법 B:
- GamePanel 전체를 custom rendering 기반으로 변경
- Swing sidePanel 제거

하지만:
- Help 버튼 유지가 복잡해질 수 있음

따라서 방법 A 우선 검토.

---

방법 A 요구사항

우측 SidePanel 클래스를 별도로 분리 가능:

예시:
- SidePanel extends JPanel

역할:
- score 표시
- level 표시
- next preview 표시
- help button 표시

그리고:
- drawNextPiece()를 SidePanel.paintComponent()로 이동

즉:
현재 GamePanel.drawNextPiece() 제거 가능.

---

필수 요구사항

- next preview 정상 표시
- 모든 테트로미노 정상 표시
- 중앙 정렬 유지
- 점수/레벨 정상 표시
- Help 버튼 하단 유지
- 다크 테마 유지
- repaint 정상 동작

---

추가 요구사항

- 기존 Board/GameLoop/InputHandler 로직 수정 금지
- 기존 public method 최대한 유지
- 변경된 클래스와 수정 이유 설명
- 최소 수정 원칙 유지
- Swing 권장 구조 준수

---

작업 순서

1. 현재 렌더링 계층 구조 분석
2. 왜 preview가 계속 가려지는지 설명
3. 해결 구조 설계
4. 필요한 클래스 분리 여부 설명
5. 코드 수정
6. 변경사항 요약
7. 테스트 방법 설명

특히:
이번 수정은 단순 좌표 수정이 아니라
"custom painting과 Swing component 혼합 구조 문제 해결"이라는 점을 인지하고 수정할 것.
