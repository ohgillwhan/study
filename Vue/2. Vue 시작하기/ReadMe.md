# Vue는 무엇인가?
MVVM패턴의 ViewModel 레이어에 해당하는 View단 라이브러리  
View[Dom] ViewModel[Vue] Model[Plain Javascript Object]

View에 변화가 있으면 ViewModel에 서 파악후 Model 에 신호를 보내고
Model에서 변화된 데이터에 대해서 ViewModel을 거쳐 View쪽으로 보낸 리엑티브적으로 동작한다.  
이 모든것을 Vue.js다

- 데이터 바인딩과 ***화면 단위를 컴포넌트 형태로 제공***, 관련 API를 지원하는데에 궁극적인 목적이 있다
- Angular에서 지원하는 2way data bindings을 동일하게 제공
    - Model과 View의 데이터가 동일하다 
- Component간 통신의 기본 골격은 React의 1Way Data Flow (부모 -> 자식)과 유사
- Virtual Dom을 이용한 렌더링 방식이 React와 거의 유사
- 다른 Front-End FW (Angular, React)와 비교했을 때 훨씬 가볍고 빠름
- 간단한 Vue를 적용하는데 있어서도 러닝커브가 낮고, 쉽게 접근 가능
    - JQuery레벨 정도의 러닝커브가 매우 낮음 
# Hello Vue
[직접테스트](./Hello%20Vue/index.html)