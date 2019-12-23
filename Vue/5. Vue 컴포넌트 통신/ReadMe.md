# 부모와 자식 컴포넌트 관계
- 구조상 상-하 관계에 있는 컴포넌트의 통신은
    - 부모 -> 자식 : props down
    - 자식 -> 부모 : events up
# Props 소개
- 모든 컴포넌트는 각 컴포넌트 자체의 스코프를 갖는다.
    - 하위 컴포넌트가 상위 컴포넌트의 값을 바로 참조할 수 없는 형
- 상위에서 하위로 값을 전달하려면 props 속성을 사용한다.
# Props 설명
[테스트](Props%20설명/index.html)
- props 변수 명명을 카멜기법으로 하면 html에서 접근은 케밥 기법(-) 으로 가야한다.  
```javascript
Vue.componenet('child-component', {
    props: ['passedData'],  
    template: '<p>{{passedData}}</p>'  
});
var app = new Vue({
    el: '#app',
    data: {
        message: 'Hello Vue! from Parent Componenet'
    }
})
// 부모(app)가 자식(component)에게 message라는값을 전
```
```html
<div id='app'>
    <child-component v-bind:passed-data="message"></child-component>
</div>
<!-- v:bind는 변수를 맵핑하는것이고 v-bind를 제외해서 입력하게되면 텍스트가 맵핑된다
```
# Non Parent - Child 컴포넌트 간 통신
동일한 부모를 가지고 같은 레벨에 있는 컴포넌트끼리는 부모를 거쳐서 통신을해야한다

자식 <-> 자식 은 불가능하며 자식<->부모<->자식 으로 해야한다.
# Event Bus
Non Parent - Child 컴포넌트 간의 통신을 위해 EventBus를 사용이 가능하다.  
어떠한 컴포넌트간에 통신이 가능하다.