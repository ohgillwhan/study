# 부모와 자식 컴포넌트 관계
- 구조상 상-하 관계에 있는 컴포넌트의 통신은
    - 부모 -> 자식 : props down
    - 자식 -> 부모 : events up
# Props 소개
- 모든 컴포넌트는 각 컴포넌트 자체의 스코프를 갖는다.
    - 하위 컴포넌트가 상위 컴포넌트의 값을 바로 참조할 수 없는 형
- 상위에서 하위로 값을 전달하려면 props 속성을 사용한다.
# Props 설명
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
```
```html
<div id='app'>
    <child-component va-bind:passed-data="message"></child-component>
</div>
```