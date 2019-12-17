# Vue Instacne
Vue.js를 이용하여 UI 화면을 개발시 아래 절차를 따른다
- Vue.js 라이브러리를 로딩했을 때 존재하는 Vue 생성자로 인스턴스를 생성해야한다
`
var vm = new Vue({});
`
- 위의 소스는 라이브러리 로딩 후 접근 가능한 Vue라는 기존객체에, 화면에서 사용할 옵션(데이터, 속성, 메서드, 등등)을 포함하여 화면의 단위를 생성한다
#Vue Instance 생성자
- Vue 생성자로 인스턴스를 만들떄 옵션은 template,el,methods,created등life cycle callback 등이 있다
## template
화면에 그리는 String [템플릿](./인스턴스%20생성/template.html)
`
<div id="test">
<my-component></my-component>
</div>
Vue.component('my-component', {
  template: '<span>{{ message }}</span>',
  data: {
    message: 'hello'
  }
})
`

## el
화면에 그려진 템플릿에 대한 el
# Vue Instance 라이프싸이클 초기화
Vue 객체가 생성될 대 아래의 초기화 작업을 수행한다.
- 데이터 관찰
- 템플릿 컴파일
- DOM 에 객체 연결
- 데이터 변경시 시 DOM 업데이트
이 초기화 외에도 개발자가 [커스텀](Vue%20라이프사이클%20소개/LifeCycle.html)으로 가능하다. (mounted, updated, destroyed)  
`
    var vm = new Vue({  
    data: {  
        a: 1  
    },  
    created : function() {  
        console.log('a is' + this.a)  
    }  
})
`
![라이프사이클](https://kr.vuejs.org/images/lifecycle.png)
